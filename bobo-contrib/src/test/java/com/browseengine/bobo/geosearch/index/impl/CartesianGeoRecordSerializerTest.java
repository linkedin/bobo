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

package com.browseengine.bobo.geosearch.index.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.junit.Test;
import org.springframework.test.annotation.IfProfileValue;

import com.browseengine.bobo.geosearch.IGeoRecordSerializer;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.bo.GeoSegmentInfo;
import com.browseengine.bobo.geosearch.impl.CartesianGeoRecordSerializer;
import com.browseengine.bobo.geosearch.impl.IGeoRecordSerializerTezt;

@IfProfileValue(name = "test-suite", values = { "unit", "all" })
public class CartesianGeoRecordSerializerTest extends IGeoRecordSerializerTezt<CartesianGeoRecord> {

    @Override
    public IGeoRecordSerializer<CartesianGeoRecord> getGeoRecordSerializer() {
        return new CartesianGeoRecordSerializer();
    }
    
    @Test
    public void testSerializeAndDeserialize() throws IOException {
        CartesianGeoRecord geoRecord = new CartesianGeoRecord(Long.MAX_VALUE, Long.MAX_VALUE, Byte.MAX_VALUE);
        serializeAndDeserialize(geoRecord, GeoSegmentInfo.BYTES_PER_RECORD_V1);
        
        geoRecord = new CartesianGeoRecord(0, 0, (byte)0);
        serializeAndDeserialize(geoRecord, GeoSegmentInfo.BYTES_PER_RECORD_V1);
        
        geoRecord = new CartesianGeoRecord(0, Long.MAX_VALUE, (byte)0);
        serializeAndDeserialize(geoRecord, GeoSegmentInfo.BYTES_PER_RECORD_V1);
        
        geoRecord = new CartesianGeoRecord(Long.MAX_VALUE, 0, (byte)(Byte.MAX_VALUE / 2));
        serializeAndDeserialize(geoRecord, GeoSegmentInfo.BYTES_PER_RECORD_V1);
    }
    
    @Test
    public void testSerializeAndDeserialize_multipleRecords() throws IOException {
        IndexOutput output = directory.createOutput(testFileName);
        
        for (long highIdx = 0; highIdx < 10; highIdx++) {
            for (int lowIdx = 0; lowIdx < 10; lowIdx++) {
                for (byte byteIdx = 0; byteIdx < 10; byteIdx++) {
                    CartesianGeoRecord geoRecord = new CartesianGeoRecord(highIdx, lowIdx, byteIdx);
                    
                    geoRecordSerializer.writeGeoRecord(output, geoRecord, GeoSegmentInfo.BYTES_PER_RECORD_V1);
                }
            }
        }
        
        output.close();
        
        IndexInput input = directory.openInput(testFileName);
        
        for (long highIdx = 0; highIdx < 10; highIdx++) {
            for (int lowIdx = 0; lowIdx < 10; lowIdx++) {
                for (byte byteIdx = 0; byteIdx < 10; byteIdx++) {
                    CartesianGeoRecord expectedRecord = new CartesianGeoRecord(highIdx, lowIdx, byteIdx);
                    CartesianGeoRecord actualRecord = geoRecordSerializer.readGeoRecord(input, GeoSegmentInfo.BYTES_PER_RECORD_V1);
                    assertEquals(expectedRecord, actualRecord);
                }
            }
        }

        input.close();
    }

}
