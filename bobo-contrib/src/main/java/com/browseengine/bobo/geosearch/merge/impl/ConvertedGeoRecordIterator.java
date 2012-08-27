/**
 * This software is licensed to you under the Apache License, Version 2.0 (the
 * "Apache License").
 *
 * LinkedIn's contributions are made under the Apache License. If you contribute
 * to the Software, the contributions will be deemed to have been made under the
 * Apache License, unless you expressly indicate otherwise. Please do not make any
 * contributions that would be inconsistent with the Apache License.
 *
 * You may obtain a copy of the Apache License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, this software
 * distributed under the Apache License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Apache
 * License for the specific language governing permissions and limitations for the
 * software governed under the Apache License.
 *
 * © 2012 LinkedIn Corp. All Rights Reserved.  
 */

package com.browseengine.bobo.geosearch.merge.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.apache.lucene.util.BitVector;

import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.bo.GeoRecord;
import com.browseengine.bobo.geosearch.bo.LatitudeLongitudeDocId;
import com.browseengine.bobo.geosearch.impl.BTree;

/**
 * Returns GeoRecords in some order.
 * The returned GeoRecords have already been translated into the new merged docid 
 * space, that is, their values have been shifted.  
 * However, it is possible to have local out-of-order regions of the iterator, 
 * for the purposes of the shifted/merged docid dimension, since the 
 * order of records is the derived from their ordering in the oridinal docid dimension.
 * 
 * <p>
 * Said another way, for all returned <tt>GeoRecord</tt> instances, their <tt>.docid</tt> 
 * property will be properly shifted to the after-merge value, but the <tt>GeoRecord</tt> 
 * returned by <tt>next()</tt> is not guaranteed greater than all those that preceded it.
 * 
 * @author Ken McCracken
 *
 */
public class ConvertedGeoRecordIterator implements Iterator<GeoRecord> {

    private static final Logger LOGGER = Logger.getLogger(ConvertedGeoRecordIterator.class);

    private final IGeoConverter geoConverter;
    private final  int absoluteDocidOffset;
    private final  BitVector deletedDocsThisPartition;

    private GeoRecord next;

    private final Iterator<GeoRecord> iteratorWithOriginalDocIds;
    
    public ConvertedGeoRecordIterator(
            IGeoConverter geoConverter,
            BTree<GeoRecord> geoRecords,
            int absoluteDocidOffset,
            BitVector deletedDocsThisPartition) throws IOException {
        this.absoluteDocidOffset = absoluteDocidOffset;

        this.geoConverter = geoConverter;

        this.deletedDocsThisPartition = deletedDocsThisPartition;

        // we need to shift docid from what is requested for the merged partition 
        // and what we know in this tree
        
        iteratorWithOriginalDocIds = geoRecords.getIterator(GeoRecord.MIN_VALID_GEORECORD, GeoRecord.MAX_VALID_GEORECORD);
        advance();
    }
    
    private void printInOriginalRange(GeoRecord candidate, LatitudeLongitudeDocId raw) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("candidate in original range, geoRecord "+candidate+", raw "+raw);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return next != null;
    }
    
    private void advance() {
        if (!iteratorWithOriginalDocIds.hasNext()) {
            next = null;
            return;
        }
        GeoRecord geoRecordInNewRange = null;
        GeoRecord candidate = null;
        do {
            if (iteratorWithOriginalDocIds.hasNext()) {
                candidate = iteratorWithOriginalDocIds.next();
                if (null != candidate) {
                    LatitudeLongitudeDocId rawInOriginalDocId = geoConverter.toLongitudeLatitudeDocId(candidate);
                    if (deletedDocsThisPartition.get(rawInOriginalDocId.docid)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("skipping deleted docid before merge for raw "+rawInOriginalDocId);
                        }
                    } else {
                        printInOriginalRange(candidate, rawInOriginalDocId);
                        geoRecordInNewRange = getGeoRecordInNewRange(candidate, rawInOriginalDocId);
                    }
                }
            } else {
                candidate = null;
            }
            if (null != candidate && null == geoRecordInNewRange) {
                String msg = "skipping candidate "+candidate+", geoRecordInNewRange null";
                LOGGER.warn(msg);
            }
        } while (null != candidate && null == geoRecordInNewRange);
        if (null != geoRecordInNewRange) {
            next = geoRecordInNewRange;
        } else {
            next = null;
        }
    }
    
    private GeoRecord getGeoRecordInNewRange(GeoRecord candidate, 
            LatitudeLongitudeDocId rawInOriginalDocId) {
        // translate into merged space
        LatitudeLongitudeDocId merged = rawInOriginalDocId.clone();
        int mergedDocId =
            findDocIdInMergedIndex(absoluteDocidOffset,
                    merged.docid, 
                deletedDocsThisPartition);
        if (mergedDocId >= 0) {
            merged.docid = mergedDocId;
            return geoConverter.toGeoRecord(candidate.filterByte, merged);
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public GeoRecord next() {
        GeoRecord next = this.next;
        advance();
        if (null != next) {
            return next;
        }
        throw new NoSuchElementException();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
        
    
    private int findDocIdInMergedIndex(int absoluteDocidOffset,
            int docIdInThisPartition, 
            BitVector deletedDocsThisPartition) {
        if (0 == deletedDocsThisPartition.count()) {
            // base case
            return absoluteDocidOffset + docIdInThisPartition;
        }
        if (deletedDocsThisPartition.get(docIdInThisPartition)) {
            // the current document has been deleted
            return -1;
        }
        int countOfDeletedDocs = countDeletedDocs(docIdInThisPartition, deletedDocsThisPartition);
        return absoluteDocidOffset + docIdInThisPartition - countOfDeletedDocs;
    }
    
    private int countDeletedDocs(int toThisDocid, BitVector deletedDocsThisPartition) {
        int count = 0;
        for (int i = 0; i < toThisDocid; i++) {
            if (deletedDocsThisPartition.get(i)) {
                count++;
            }
        }
        return count;
    }


}
