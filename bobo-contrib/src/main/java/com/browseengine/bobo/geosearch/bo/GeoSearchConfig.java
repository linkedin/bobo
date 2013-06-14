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

import org.apache.lucene.index.IndexFileNames;
import org.springframework.stereotype.Component;

import com.browseengine.bobo.geosearch.IGeoConverter;
import com.browseengine.bobo.geosearch.IGeoUtil;
import com.browseengine.bobo.geosearch.impl.GeoConverter;
import com.browseengine.bobo.geosearch.impl.GeoUtil;
import com.browseengine.bobo.geosearch.merge.IGeoMerger;
import com.browseengine.bobo.geosearch.merge.impl.BufferedGeoMerger;

/**
 * Class to hold configuration parameters for GeoSearch 
 * 
 * @author Geoff Cooney
 *
 */
@Component
public class GeoSearchConfig {

    public static final String DEFAULT_GEO_FILE_EXTENSION = "geo";
    private IGeoConverter geoConverter = new GeoConverter();
    private IGeoUtil geoUtil = new GeoUtil();
    private IGeoMerger geoMerger = new BufferedGeoMerger();
    private String geoFileExtension = DEFAULT_GEO_FILE_EXTENSION;
    
    private String[] pairedExtensionsForDelete 
        = new String[] {IndexFileNames.COMPOUND_FILE_EXTENSION};
    
    public static final int DEFAULT_ID_BYTE_COUNT = 16;
    private int bytesForId = DEFAULT_ID_BYTE_COUNT;
    private int maxIndexSize = Integer.MAX_VALUE;
    
    /**
     * Sets the extension for geo indices.
     * WARNING:  This should never be changed when reading an existing index as doing so may result in
     * geo search being unable to find indices for existing segments
     * @param fileExtension
     */
    public void setGeoFileExtension(String fileExtension) {
        this.geoFileExtension = fileExtension;
    }
    
    public String getGeoFileExtension() {
        return geoFileExtension;
    }
    
    public IGeoConverter getGeoConverter() {
        return geoConverter;
    }
    
    public void setGeoConverter(IGeoConverter geoConverter) {
        this.geoConverter = geoConverter;
    }
    
    public void setGeoUtil(IGeoUtil geoUtil) {
        this.geoUtil = geoUtil;
    }
    
    public void addFieldBitMask(String fieldName, byte bitMask) {
        this.geoConverter.addFieldBitMask(fieldName, bitMask);
    }
    
    public IGeoUtil getGeoUtil() {
        return this.geoUtil;
    }

    
    public IGeoMerger getGeoMerger() {
        return geoMerger;
    }

    public void setGeoMerger(IGeoMerger geoMerger) {
        this.geoMerger = geoMerger;
    }

    public String getGeoFileName(String name) {
        return name + "." + getGeoFileExtension();
    }
    
    public int getBufferSizePerGeoSegmentReader() {
        return 16*1024;
    }

    /**
     * The extension that we should pair off of by delete.  When any extension in this list
     * is deleted by Lucene for any reason, geo search will also delete the corresponding
     * geo file.  By default, this is set to CFS and FNM.
     */
    public void setPairedExtensionPairsForDelete(String... pairedExtensionsForDelete) {
        this.pairedExtensionsForDelete = pairedExtensionsForDelete;
    }

    public String[] getPairedExtensionsForDelete() {
        return pairedExtensionsForDelete;
    }
    
    /**
     * The number of bytes reserved for the id field.  This is only used
     * by GeoOnlySearch
     * @param bytesForId
     */
    public void setBytesForId(int bytesForId) {
        this.bytesForId = bytesForId;
    }
    
    public int getBytesForId() {
        return bytesForId;
    }
    
    /**
     * The maximum size the index should be allowed to grow to.  Any attempts to
     * flush an index larger than this will throw errors.  Applies only to the GeoOnlyIndex. 
     * @param maxIndexSize
     */
    public void setMaxIndexSize(int maxIndexSize) {
        this.maxIndexSize = maxIndexSize;
    }
    
    public int getMaxIndexSize() {
        return maxIndexSize;
    }
}
