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
import java.util.List;
import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.DocumentsWriter.DocState;
import org.apache.lucene.index.DocumentsWriter.DocWriter;

import com.browseengine.bobo.geosearch.index.IGeoIndexer;
import com.browseengine.bobo.geosearch.index.bo.GeoCoordinateField;

public class GeoDocConsumerPerThread extends DocConsumerPerThread {

    DocConsumerPerThread defaultDocConsumerPerThread;
    
    DocState docState;
    
    IGeoIndexer geoIndexer;
    
    public GeoDocConsumerPerThread(DocConsumerPerThread defaultDocConsumerPerThread, 
            DocumentsWriterThreadState threadState, IGeoIndexer geoIndexer) {
        this.defaultDocConsumerPerThread = defaultDocConsumerPerThread;
        this.docState = threadState.docState;
        
        this.geoIndexer = geoIndexer;
    }
    
    @Override
    DocWriter processDocument() throws IOException {
        //this is where we process the geo-search components of the document
        Document doc = docState.doc;
        int docID = docState.docID;
        
        List<Fieldable> fields = doc.getFields();
        List<GeoCoordinateField> geoFields = new Vector<GeoCoordinateField>();
        
        for (Fieldable field: fields) {
            if (field instanceof GeoCoordinateField) {
                geoFields.add((GeoCoordinateField)field);
            }
        }

        for (GeoCoordinateField geoField: geoFields) {
            //process field into GeoIndex here
            geoIndexer.index(docID, geoField);
            
            doc.removeFields(geoField.name());
        }
        
        return defaultDocConsumerPerThread.processDocument();
    }

    @Override
    /**
     * This implementation assumes that we are aborting all documents not yet flushed, 
     * as opposed to needing to track specifically which documents have been
     * added by this specific DocConsumerPerThread.  This appears to be consistent with lucene's
     * expectations as of version 3.3 but unfortunately I don't see any documentation on the
     * interface's abort method that specifies this as the desired behavior
     */
    void abort() {
        defaultDocConsumerPerThread.abort();
        geoIndexer.abort();
    }
    
    public DocConsumerPerThread getDefaultDocConsumerPerThread() {
        return defaultDocConsumerPerThread;
    }
}
