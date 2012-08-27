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

package com.browseengine.bobo.geosearch.solo.search.impl;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.store.Directory;

import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.bo.CartesianCoordinateUUID;
import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;
import com.browseengine.bobo.geosearch.impl.GeoConverter;
import com.browseengine.bobo.geosearch.index.impl.GeoSegmentReader;
import com.browseengine.bobo.geosearch.query.GeoQuery;
import com.browseengine.bobo.geosearch.score.impl.CartesianComputeDistance;
import com.browseengine.bobo.geosearch.score.impl.Conversions;
import com.browseengine.bobo.geosearch.solo.bo.IDGeoRecord;
import com.browseengine.bobo.geosearch.solo.impl.IDGeoRecordComparator;
import com.browseengine.bobo.geosearch.solo.impl.IDGeoRecordSerializer;

/**
 * 
 * @author gcooney
 *
 */
public class GeoOnlySearcher {
    
    public static final byte[] EMPTY_UUID = new byte[0];
    GeoSearchConfig config;
    Directory directory;
    String indexName;
    
    IGeoConverter geoConverter;
    IDGeoRecordComparator geoComparator = new IDGeoRecordComparator();
    IDGeoRecordSerializer geoRecordSerializer = new IDGeoRecordSerializer();
    
    public GeoOnlySearcher(GeoSearchConfig config, Directory directory, String indexName) {
        this.config = config;
        this.directory = directory;
        this.indexName = indexName;
        
        geoConverter = new GeoConverter();
    }
    
    public GeoOnlyHits search(GeoQuery query, int start, int count) throws IOException {
        CartesianCoordinateUUID minCoordinate = buildMinCoordinate(query);
        CartesianCoordinateUUID maxCoordinate = buildMaxCoordinate(query);

        GeoSegmentReader<IDGeoRecord> segmentReader = getGeoSegmentReader();

        IDGeoRecord minRecord = geoConverter.toIDGeoRecord(minCoordinate);
        IDGeoRecord maxRecord = geoConverter.toIDGeoRecord(maxCoordinate);
        Iterator<IDGeoRecord> hitIterator = segmentReader.getIterator(minRecord, maxRecord);
        
        return collectHits(query, hitIterator, minCoordinate, maxCoordinate, start, count);
    }

    GeoSegmentReader<IDGeoRecord> getGeoSegmentReader() throws IOException {
        String fileName = indexName + "." + config.getGeoFileExtension();
        GeoSegmentReader<IDGeoRecord> segmentReader = new GeoSegmentReader<IDGeoRecord>(
                directory, fileName, Integer.MAX_VALUE, config.getBufferSizePerGeoSegmentReader(), 
                geoRecordSerializer, geoComparator);
        return segmentReader;
    }
    
    private GeoOnlyHits collectHits(GeoQuery query, Iterator<IDGeoRecord> hitIterator, 
            CartesianCoordinateUUID minCoordinate, CartesianCoordinateUUID maxCoordinate,
            int start, int count) {
        CartesianCoordinateUUID centroidCoordinate = geoConverter.toCartesianCoordinate(
            query.getCentroidLatitude(), query.getCentroidLongitude(), EMPTY_UUID);
        
        int totalHits = 0;
        GeoOnlyHitQueue hitQueue = new GeoOnlyHitQueue(start+count);
        while (hitIterator.hasNext()) {
            IDGeoRecord record = hitIterator.next();
            CartesianCoordinateUUID hitCoordinate = geoConverter.toCartesianCoordinate(record);
            if (minCoordinate.x <= hitCoordinate.x && hitCoordinate.x <= maxCoordinate.x 
                    && minCoordinate.y <= hitCoordinate.y && hitCoordinate.y <= maxCoordinate.y 
                    && minCoordinate.z <= hitCoordinate.z && hitCoordinate.z <= maxCoordinate.z
                    ) {
                totalHits++;
                
                double score = CartesianComputeDistance.computeDistanceSquared(
                        hitCoordinate.x, hitCoordinate.y, hitCoordinate.z, 
                        centroidCoordinate.x, centroidCoordinate.y, centroidCoordinate.z);
                GeoOnlyHit geoHit = new GeoOnlyHit(score, hitCoordinate.uuid);
                hitQueue.insertWithOverflow(geoHit);
            }
        }
        
        int inRangeHits =  count;
        if (inRangeHits > hitQueue.size() - start) {
            inRangeHits = Math.max(0, hitQueue.size() - start);
        }
        GeoOnlyHit[] hits = new GeoOnlyHit[inRangeHits];
        for (int i = inRangeHits - 1; i >= 0; i--) {
            hits[i] = hitQueue.pop();
        }
        
        return new GeoOnlyHits(totalHits, hits);
    }
    
    private CartesianCoordinateUUID buildMinCoordinate(GeoQuery query) {
        double rangeInkm = Conversions.mi2km(query.getRangeInMiles());
        int rangeInUnits = Conversions.radiusMetersToIntegerUnits(rangeInkm * 1000);
        CartesianCoordinateUUID centroidCoordinate = geoConverter.toCartesianCoordinate(query.getCentroidLatitude(), query.getCentroidLongitude(), EMPTY_UUID);
        
        int minX = calculateMinimumCoordinate(centroidCoordinate.x, rangeInUnits);
        int minY = calculateMinimumCoordinate(centroidCoordinate.y, rangeInUnits);
        int minZ = calculateMinimumCoordinate(centroidCoordinate.z, rangeInUnits);
        return new CartesianCoordinateUUID(minX, minY, minZ, EMPTY_UUID);
    }
    
    private CartesianCoordinateUUID buildMaxCoordinate(GeoQuery query){
        double rangeInkm = Conversions.mi2km(query.getRangeInMiles());
        int rangeInUnits = Conversions.radiusMetersToIntegerUnits(rangeInkm * 1000);
        CartesianCoordinateUUID centroidCoordinate = geoConverter.toCartesianCoordinate(query.getCentroidLatitude(), query.getCentroidLongitude(), EMPTY_UUID);
        
        int maxX = calculateMaximumCoordinate(centroidCoordinate.x, rangeInUnits);
        int maxY = calculateMaximumCoordinate(centroidCoordinate.y, rangeInUnits);
        int maxZ = calculateMaximumCoordinate(centroidCoordinate.z, rangeInUnits);
        return new CartesianCoordinateUUID(maxX, maxY, maxZ, EMPTY_UUID);
    }
    
    private int calculateMinimumCoordinate(int originalPoint, int delta) {
        if (originalPoint > 0 || 
                originalPoint > Integer.MIN_VALUE + delta) {
            return originalPoint - delta;
        } else {
            return Integer.MIN_VALUE;
        }
    }
    
    private int calculateMaximumCoordinate(int originalPoint, int delta) {
        if (originalPoint < 0 || 
                originalPoint < Integer.MAX_VALUE - delta) {
            return originalPoint + delta;
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
