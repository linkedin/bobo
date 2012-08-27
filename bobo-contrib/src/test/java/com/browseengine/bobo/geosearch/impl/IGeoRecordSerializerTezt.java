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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;

import com.browseengine.bobo.geosearch.IGeoRecordSerializer;
import com.browseengine.bobo.geosearch.bo.IGeoRecord;

/**
 * 
 * @author gcooney
 *
 * @param <T>
 */
public abstract class IGeoRecordSerializerTezt<T extends IGeoRecord> {
    
    protected IGeoRecordSerializer<T> geoRecordSerializer;
    
    protected Directory directory;
    protected String testFileName;
    
    @Before
    public void setUp() {
        testFileName = UUID.randomUUID().toString();
        directory = new RAMDirectory(); 
        
        geoRecordSerializer = getGeoRecordSerializer();
    }
    
    @After
    public void tearDown() throws IOException {
        if (directory.fileExists(testFileName)) {
            directory.deleteFile(testFileName);
        }
    }
    
    public abstract IGeoRecordSerializer<T> getGeoRecordSerializer();
    
    public void serializeAndDeserialize(T expectedRecord, int byteCount) throws IOException {
        String fileName = UUID.randomUUID().toString();
        
        IndexOutput output = directory.createOutput(fileName);
        geoRecordSerializer.writeGeoRecord(output, expectedRecord, byteCount);
        output.close();
        
        IndexInput input = directory.openInput(fileName);
        T actualRecord = geoRecordSerializer.readGeoRecord(input, byteCount);
        input.close();
        
        assertEquals(expectedRecord, actualRecord);
        
        directory.deleteFile(fileName);
    }
}
