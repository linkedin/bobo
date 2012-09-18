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

package com.browseengine.bobo.geosearch;

import java.io.IOException;

import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.bo.DocsSortedByDocId;
import com.browseengine.bobo.geosearch.index.impl.GeoSegmentReader;

/**
 * @author Ken McCracken
 *
 */
public interface IGeoBlockOfHitsProvider {
    
    /**
     * * ALL VALUES RETURNED HAVE RAW DOCID that is the docid WITHIN the 
     * current segment.
     * 
     * <p>
     * Gets a block of results within the specified boundaries.
     * The returned object should contain access to docids and scores 
     * where docids are relative within a partition (docid if that 
     * partition were the only one), and scores should be something that 
     * smooths out the 1/distance or 1/distance^2 curve.
     * 
     * @param geoSegmentReader
     * @param deletedDocsWithinSegment
     * @param minX
     * @param minY
     * @param minZ
     * @param minimumDocid
     * @param maxX
     * @param maxY
     * @param maxZ
     * @param maximumDocid
     * @return
     * @throws IOException
     */
    DocsSortedByDocId getBlock(GeoSegmentReader<CartesianGeoRecord> geoSegmentReader, IDeletedDocs deletedDocsWithinSegment,
            int minX, int minY, int minZ, int minimumDocid, 
            int maxX, int maxY, int maxZ, int maximumDocid) throws IOException;
}
