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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.util.BitVector;

import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.impl.BTree;
import com.browseengine.bobo.geosearch.impl.CartesianGeoRecordComparator;

/**
 * Can merge multiple BTreeAsArray&lg;GeoRecord&gt; instances.
 * Implements an Iterator that walks the GeoRecords in ascending 
 * order, with correctly assigned docids for the merged output partition.
 * 
 * @author Ken McCracken
 *
 */
public class ChainedConvertedGeoRecordIterator implements Iterator<CartesianGeoRecord> {
    
    private static final Logger LOGGER = Logger.getLogger(ChainedConvertedGeoRecordIterator.class);

    private static final CartesianGeoRecordComparator geoRecordCompareByBitMag = new CartesianGeoRecordComparator();
    
    protected IGeoConverter geoConverter;
    //protected Iterator<CartesianGeoRecord> mergedIterator;
    protected OrderedIteratorChain<CartesianGeoRecord> orderedIteratorChain;
    
    public ChainedConvertedGeoRecordIterator(IGeoConverter geoConverter, 
            List<BTree<CartesianGeoRecord>> partitions,
            List<BitVector> deletedDocsList, 
            int totalBufferCapacity) throws IOException {
        this.geoConverter = geoConverter;
        
        int numberOfPartitions = partitions.size();
        if (numberOfPartitions != deletedDocsList.size()) {
            throw new RuntimeException("bad input, partitions.size() "
                    + numberOfPartitions + ", deletedDocsList.size() " + deletedDocsList.size());
        }
        
        int docid = 0;
        int bufferCapacityPerIterator = totalBufferCapacity / numberOfPartitions;
        List<Iterator<CartesianGeoRecord>> mergedIterators = new ArrayList<Iterator<CartesianGeoRecord>>(partitions.size());
        
        for (int i = 0; i < partitions.size(); i++) {
            BTree<CartesianGeoRecord> partition = partitions.get(i);
            BitVector deletedDocs = deletedDocsList.get(i);
            Iterator<CartesianGeoRecord> mergedIterator = 
                new ConvertedGeoRecordIterator(geoConverter, partition, 
                        docid, deletedDocs);
            mergedIterator = new BufferedOrderedIterator<CartesianGeoRecord>(mergedIterator, 
                    geoRecordCompareByBitMag, bufferCapacityPerIterator);
            mergedIterators.add(mergedIterator);
            docid += deletedDocs.size() - deletedDocs.count();
        }

        orderedIteratorChain = 
            new OrderedIteratorChain<CartesianGeoRecord>(mergedIterators, geoRecordCompareByBitMag);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return orderedIteratorChain.hasNext();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CartesianGeoRecord next() {
        return orderedIteratorChain.next();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
