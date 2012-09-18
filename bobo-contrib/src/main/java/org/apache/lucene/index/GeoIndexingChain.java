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

import org.apache.lucene.index.DocumentsWriter.IndexingChain;

import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;


/**
 * Implementation of lucene IndexingChain class.  The GeoIndexingChain class 
 * takes another indexing chain in the constructor.  It adds a custom GeoDocConsumer
 * which wraps the default indexing chain's consumer to pull out geo components 
 * from the document and index them independently, before returning control to the
 * default Consumer. 
 * 
 * @author Geoff Cooney
 *
 */
public class GeoIndexingChain extends IndexingChain {
    
    IndexingChain defaultIndexingChain;
    GeoSearchConfig config;
    
    public GeoIndexingChain(GeoSearchConfig config, IndexingChain defaultIndexingChain) {
        this.defaultIndexingChain = defaultIndexingChain;
        this.config = config;
    }
    
    @Override
    DocConsumer getChain(DocumentsWriter documentsWriter) {
        DocConsumer defaultDocConsumer = defaultIndexingChain.getChain(documentsWriter);
        return new GeoDocConsumer(config, defaultDocConsumer);
    }
}
