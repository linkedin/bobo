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

package com.browseengine.bobo.geosearch.impl;

import java.io.IOException;
import java.util.Iterator;

import com.browseengine.bobo.geosearch.CartesianCoordinateDocId;
import com.browseengine.bobo.geosearch.IDeletedDocs;
import com.browseengine.bobo.geosearch.IGeoBlockOfHitsProvider;
import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.bo.DocsSortedByDocId;
import com.browseengine.bobo.geosearch.bo.GeRecordAndCartesianDocId;
import com.browseengine.bobo.geosearch.index.impl.GeoSegmentReader;

/**
 * @author Ken McCracken
 *
 */
public class GeoBlockOfHitsProvider implements IGeoBlockOfHitsProvider {
    
    private final IGeoConverter geoConverter;
    
    public GeoBlockOfHitsProvider(IGeoConverter geoConverter) {
        this.geoConverter = geoConverter;
    }

    /**
     * {@inheritDoc}
     * @throws IOException 
     *
     */
    @Override
    public DocsSortedByDocId getBlock(GeoSegmentReader<CartesianGeoRecord> geoSegmentReader, IDeletedDocs deletedDocsWithinSegment,
            int minX, int maxX, int minY, int maxY, int minZ, int maxZ, int mindocid, int maxdocid)
            throws IOException {

        CartesianCoordinateDocId minccd = new CartesianCoordinateDocId(minX, minY, minZ, mindocid);
        CartesianGeoRecord minValue = geoConverter.toCartesianGeoRecord(minccd, CartesianGeoRecord.DEFAULT_FILTER_BYTE);
        CartesianCoordinateDocId maxccd = new CartesianCoordinateDocId(maxX, maxY, maxZ, maxdocid);
        CartesianGeoRecord maxValue = geoConverter.toCartesianGeoRecord(maxccd, CartesianGeoRecord.DEFAULT_FILTER_BYTE);
        Iterator<CartesianGeoRecord> iterator = geoSegmentReader.getIterator(minValue, maxValue);
        DocsSortedByDocId docs = new DocsSortedByDocId();

        while (iterator.hasNext()) {
            CartesianGeoRecord geoRecord = iterator.next();
            CartesianCoordinateDocId ccd  = geoConverter.toCartesianCoordinateDocId(geoRecord);
            if(minX <= ccd.x  && ccd.x <= maxX && minY <= ccd.y  && ccd.y <= maxY && minZ <= ccd.z  && ccd.z <= maxZ && mindocid <= ccd.docid  && ccd.docid <= maxdocid) {
                GeRecordAndCartesianDocId both = new GeRecordAndCartesianDocId(geoRecord, ccd);
                docs.add(ccd.docid, both);
            }
        }
        
        return docs;
    }

    
}
