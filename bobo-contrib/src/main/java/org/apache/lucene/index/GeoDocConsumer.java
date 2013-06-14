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
import java.util.Collection;
import java.util.HashSet;

import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;
import com.browseengine.bobo.geosearch.index.IGeoIndexer;
import com.browseengine.bobo.geosearch.index.impl.GeoIndexer;

/**
 * 
 * @author Geoff Cooney
 *
 */
public class GeoDocConsumer extends DocConsumer {

    DocConsumer defaultDocConsumer;
    IGeoIndexer geoIndexer;
    
    public GeoDocConsumer(GeoSearchConfig config, DocConsumer defaultDocConsumer) { 
        this.defaultDocConsumer = defaultDocConsumer;
        this.geoIndexer = new GeoIndexer(config);
    }
    
    public void setGeoIndexer(IGeoIndexer geoIndexer) {
        this.geoIndexer = geoIndexer;
    }
    
    @Override
    DocConsumerPerThread addThread(DocumentsWriterThreadState perThread) throws IOException {
        DocConsumerPerThread defaultDocConsumerPerThread = defaultDocConsumer.addThread(perThread);
        return new GeoDocConsumerPerThread(defaultDocConsumerPerThread, perThread, geoIndexer);
    }

    @Override
    //TODO:  Do we need to do anything for documents that have not yet been flushed but are deleted
    //We should make sure to test this case and see if lucene keeps the document or not
    void flush(Collection<DocConsumerPerThread> threads, SegmentWriteState state) throws IOException {
        //flush synchronously for now, we may later wish to perform this two flushes Asynchronously
        
        //because Lucene's DocConsumer implementation performs an unchecked cast and relies on methods
        //that only exist in a specific implementation, we need to build a list of the
        //defaultDocConsumerPerThreads and pass that into the defaultDocConsumer 
        Collection<DocConsumerPerThread> defaultDocConsumerThreads = 
            new HashSet<DocConsumerPerThread>(threads.size());
        
        for (DocConsumerPerThread thread: threads) {
            if (thread instanceof GeoDocConsumerPerThread) {
                GeoDocConsumerPerThread geoThread = (GeoDocConsumerPerThread)thread;
                defaultDocConsumerThreads.add(geoThread.getDefaultDocConsumerPerThread());
            } else {
                defaultDocConsumerThreads.add(thread);
            }
        }
        
        defaultDocConsumer.flush(defaultDocConsumerThreads, state);
        geoIndexer.flush(state.directory, state.segmentName);
    }

    @Override
    void abort() {
        defaultDocConsumer.abort();
        geoIndexer.abort();
    }

    @Override
    boolean freeRAM() {
        //for now just ask the default DocConsumer to freeRAM  
        return defaultDocConsumer.freeRAM();
    }

    public DocConsumer getDefaultDocConsumer() {
        return defaultDocConsumer;
    }
    
}
