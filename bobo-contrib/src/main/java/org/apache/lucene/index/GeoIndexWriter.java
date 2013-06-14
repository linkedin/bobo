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

package org.apache.lucene.index;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;
import com.browseengine.bobo.geosearch.index.impl.DeletePairedExtensionDirectory;
import com.browseengine.bobo.geosearch.merge.IGeoMergeInfo;
import com.browseengine.bobo.geosearch.merge.IGeoMerger;


/**
 * Extends Lucene's IndexWriter to provide functionality for indexing, merging, and deleting
 * Coordinate based fields.
 * 
 * @author Geoff Cooney
 * @see IndexWriter
 */
public class GeoIndexWriter extends IndexWriter {

    GeoSearchConfig geoConfig;
    
    public GeoIndexWriter(Directory d, IndexWriterConfig indexWriterConfig, GeoSearchConfig geoConfig) throws CorruptIndexException, LockObtainFailedException,
            IOException {
        super(buildGeoDirectory(d, geoConfig), setIndexingChain(indexWriterConfig, geoConfig));
        
        this.geoConfig = geoConfig;
    }

    public static Directory buildGeoDirectory(Directory dir, GeoSearchConfig geoConfig) {
        DeletePairedExtensionDirectory pairedDirectory = new DeletePairedExtensionDirectory(dir);
        for (String pairedExtension: geoConfig.getPairedExtensionsForDelete()) {
            pairedDirectory.addExtensionPairing(pairedExtension, geoConfig.getGeoFileExtension());
        }
        
        return pairedDirectory;
    }
    
    public static IndexWriterConfig setIndexingChain(IndexWriterConfig indexWriterConfig, GeoSearchConfig geoConfig) {
        return indexWriterConfig.setIndexingChain(new GeoIndexingChain(geoConfig, indexWriterConfig.getIndexingChain()));
    }
    
    @Override
    public void beforeMergeAfterSetup(MergePolicy.OneMerge merge) throws IOException {
        merge.checkAborted(getDirectory());
        
        IGeoMergeInfo geoMergeInfo = new GeoMergeInfo(merge, getDirectory());

        IGeoMerger geoMerger = geoConfig.getGeoMerger();
        
        geoMerger.merge(geoMergeInfo, geoConfig);
    }
    
}
