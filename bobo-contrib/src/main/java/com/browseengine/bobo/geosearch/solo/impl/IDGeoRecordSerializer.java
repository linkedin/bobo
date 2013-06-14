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

package com.browseengine.bobo.geosearch.solo.impl;

import java.io.IOException;

import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;

import com.browseengine.bobo.geosearch.IGeoRecordSerializer;
import com.browseengine.bobo.geosearch.solo.bo.IDGeoRecord;

/**
 * 
 * @author gcooney
 *
 */
public class IDGeoRecordSerializer implements IGeoRecordSerializer<IDGeoRecord>{

    public static final int INTERLACE_BYTES = 12;
    
    @Override
    public void writeGeoRecord(IndexOutput output, IDGeoRecord record, int recordByteCount) throws IOException {
        if (record.id.length != recordByteCount - INTERLACE_BYTES) {
            throw new IllegalArgumentException("Incorrect number of id bytes given.  " +
                    "This is most likely a bug!  ExpectedBytes=" + 
                    (recordByteCount - INTERLACE_BYTES)  + 
                    "; receivedBytes=" + record.id.length);
        }
        
        output.writeLong(record.highOrder);
        output.writeInt(record.lowOrder);
        output.writeBytes(record.id, record.id.length);
    }

    @Override
    public IDGeoRecord readGeoRecord(IndexInput input, int recordByteCount) throws IOException {
        long highOrder = input.readLong();
        int lowOrder = input.readInt();
        int countIdBytes = recordByteCount - INTERLACE_BYTES;
        byte[] id = new byte[countIdBytes];
        input.readBytes(id, 0, countIdBytes, false);
        return new IDGeoRecord(highOrder, lowOrder, id);
    }

}
