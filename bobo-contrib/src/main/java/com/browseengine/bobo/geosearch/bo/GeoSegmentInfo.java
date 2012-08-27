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

package com.browseengine.bobo.geosearch.bo;

import com.browseengine.bobo.geosearch.IFieldNameFilterConverter;
import com.browseengine.bobo.geosearch.impl.MappedFieldNameFilterConverter;

public class GeoSegmentInfo {
    public static final int BYTES_PER_RECORD_V1 = 13;
    
    private String segmentName;
    private IFieldNameFilterConverter fieldNameFilterConverter = new MappedFieldNameFilterConverter();
    private int geoVersion;
    private int bytesPerRecord = BYTES_PER_RECORD_V1;
    
    public String getSegmentName() {
        return segmentName;
    }
    
    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }
    
    public IFieldNameFilterConverter getFieldNameFilterConverter() {
        return fieldNameFilterConverter;
    }
    
    public void setFieldNameFilterConverter(IFieldNameFilterConverter fieldNameFilterConverter) {
        this.fieldNameFilterConverter = fieldNameFilterConverter;
    }
    
    public int getGeoVersion() {
        return geoVersion;
    }

    public void setGeoVersion(int geoVersion) {
        this.geoVersion = geoVersion;
    }
    
    public int getBytesPerRecord() {
        return bytesPerRecord;
    }
    
    public void setBytesPerRecord(int bytesPerRecord) {
        this.bytesPerRecord = bytesPerRecord;
    }
}
