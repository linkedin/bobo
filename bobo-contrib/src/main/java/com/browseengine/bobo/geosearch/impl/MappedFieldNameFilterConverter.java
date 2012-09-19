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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.DataOutput;
import org.springframework.stereotype.Component;

import com.browseengine.bobo.geosearch.IFieldNameFilterConverter;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;

/**
 * Provides a map backed implementation of a File name to filter converter
 * 
 * @author Geoff Cooney
 *
 */
@Component
public class MappedFieldNameFilterConverter implements IFieldNameFilterConverter {
    public static final int FIELD_FILTER_VERSION = 0;
    
    Map<String, Byte> bitmasks;
    
    public MappedFieldNameFilterConverter() {
        bitmasks = new HashMap<String, Byte>();
    }
    
    public void addFieldBitMask(String fieldName, byte bitMask) {
        bitmasks.put(fieldName, bitMask);
    }
    
    @Override
    public byte getFilterValue(String[] fieldNames) {
        if (fieldNames == null || fieldNames.length == 0) {
            return CartesianGeoRecord.DEFAULT_FILTER_BYTE;
        }
            
        byte filterByte = (byte)0;
        for (String fieldName: fieldNames) {
            Byte bitmask = bitmasks.get(fieldName);
            if (bitmask != null) {
                filterByte = (byte) (filterByte | bitmask.byteValue());
            }
        }
            
        return filterByte;
    }

    @Override
    public List<String> getFields(byte filterValue) {
        List<String> filterFields = new Vector<String>(); 
        
        for (Map.Entry<String, Byte> bitmaskEntry : bitmasks.entrySet()) {
            String field = bitmaskEntry.getKey();
            Byte bitmask = bitmaskEntry.getValue();
            
            if ((filterValue & bitmask.byteValue()) != 0) {
                filterFields.add(field);
            }
        }
        
        return filterFields;
    }

    @Override
    public boolean fieldIsInFilter(String fieldName, byte filterValue) {
        Byte bitmask = bitmasks.get(fieldName);
        
        return bitmask != null && (filterValue & bitmask.byteValue()) != 0;
    }

    @Override
    public void writeToOutput(DataOutput output) throws IOException {
        output.writeVInt(FIELD_FILTER_VERSION);
        
        if (bitmasks != null) {
            output.writeVInt(bitmasks.size());
            for (Map.Entry<String, Byte> filterEntry: bitmasks.entrySet()) {
                output.writeString(filterEntry.getKey());
                output.writeByte(filterEntry.getValue());
            }
        } else {
            output.writeVInt(0);
        }
    }

    @Override
    public void loadFromInput(DataInput input) throws IOException {
        int version = input.readVInt();  //read version
        
        int mapSize = input.readVInt();
        bitmasks = new HashMap<String, Byte>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            String fieldName = input.readString();
            Byte filterByte = input.readByte();
            
            bitmasks.put(fieldName, filterByte);
        }
    }
    
}
