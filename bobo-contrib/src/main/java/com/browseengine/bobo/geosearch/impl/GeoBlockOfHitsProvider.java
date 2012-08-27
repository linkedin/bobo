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

import com.browseengine.bobo.geosearch.IDeletedDocs;
import com.browseengine.bobo.geosearch.IGeoBlockOfHitsProvider;
import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.bo.DocsSortedByDocId;
import com.browseengine.bobo.geosearch.bo.GeoRecord;
import com.browseengine.bobo.geosearch.bo.GeoRecordAndLongitudeLatitudeDocId;
import com.browseengine.bobo.geosearch.bo.LatitudeLongitudeDocId;
import com.browseengine.bobo.geosearch.index.impl.GeoSegmentReader;

/**
 * @author Ken McCracken
 *
 */
public class GeoBlockOfHitsProvider implements IGeoBlockOfHitsProvider {
    
    private IGeoConverter geoConverter;
    
    public GeoBlockOfHitsProvider(IGeoConverter geoConverter) {
        this.geoConverter = geoConverter;
    }

    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    public DocsSortedByDocId getBlock(GeoSegmentReader geoSegmentReader, IDeletedDocs deletedDocsWithinSegment,
            final double minimumLongitude, final double minimumLatitude, final int minimumDocid, 
            final double maximumLongitude, final double maximumLatitude, final int maximumDocid) throws IOException {
        final byte filterByte = GeoRecord.DEFAULT_FILTER_BYTE;
        
        LatitudeLongitudeDocId minRaw = new LatitudeLongitudeDocId(minimumLatitude, minimumLongitude, minimumDocid);
        GeoRecord minValue = geoConverter.toGeoRecord(filterByte, minRaw);
        LatitudeLongitudeDocId maxRaw = new LatitudeLongitudeDocId(maximumLatitude, maximumLongitude, maximumDocid);
        GeoRecord maxValue = geoConverter.toGeoRecord(filterByte, maxRaw);
        Iterator<GeoRecord> iterator = geoSegmentReader.getIterator(minValue, maxValue);
        DocsSortedByDocId docs = new DocsSortedByDocId();
        while (iterator.hasNext()) {
            GeoRecord geoRecord = iterator.next();
            LatitudeLongitudeDocId longitudeLatitudeDocId = geoConverter.toLongitudeLatitudeDocId(geoRecord);
            if (minimumLongitude <= longitudeLatitudeDocId.longitude && longitudeLatitudeDocId.longitude <= maximumLongitude 
                    && minimumLatitude <= longitudeLatitudeDocId.latitude && longitudeLatitudeDocId.latitude <= maximumLatitude 
                    && minimumDocid <= longitudeLatitudeDocId.docid && longitudeLatitudeDocId.docid <= maximumDocid
                    ) {
                // (AT LEAST ALMOST A) HIT!
                GeoRecordAndLongitudeLatitudeDocId both = new GeoRecordAndLongitudeLatitudeDocId(geoRecord, longitudeLatitudeDocId);
                docs.add(longitudeLatitudeDocId.docid, both);
            }
        }
        return docs;
    }

    
}
