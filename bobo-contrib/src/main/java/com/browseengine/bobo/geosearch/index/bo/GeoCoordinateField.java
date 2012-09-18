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

package com.browseengine.bobo.geosearch.index.bo;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.FieldInfo.IndexOptions;

/**
 * 
 * @author Geoff Cooney
 * @author Shane Detsch
 *
 */
public class GeoCoordinateField implements Fieldable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String fieldName;
    private GeoCoordinate geoCoordinate;
    private float boost = 1.0f;
    
    public GeoCoordinateField(String fieldName, GeoCoordinate geoCoordinate) {
        this.fieldName = fieldName;
        this.geoCoordinate = geoCoordinate;
    }
    
    public GeoCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }
    
    public void setGeoCoordinate(GeoCoordinate geoCoordinate) {
        this.geoCoordinate = geoCoordinate;
    }
    
    @Override
    public String stringValue() {
        return geoCoordinate.getLatitude() + ", " + geoCoordinate.getLongitude();
    }

    /** 
     *  Returns always <code>null</code> for GeoCoordinate fields 
     *  Use getGeoCoordinate to retrieve results instead. 
     */
    @Override
    public byte[] getBinaryValue(byte[] result){
      return null;
    }
    
    /** 
     *  Returns always <code>null</code> for GeoCoordinate fields 
     *  Use getGeoCoordinate to retrieve results instead. 
     */
    @Override
    public Reader readerValue() {
        return null;
    }

    @Override
    public TokenStream tokenStreamValue() {
        return null;
    }

    @Override
    public void setBoost(float boost) {
        this.boost = boost;
    }

    @Override
    public float getBoost() {
        return boost;
    }

    @Override
    public String name() {
        return fieldName;
    }

    @Override
    public boolean isStored() {
        return false;
    }

    @Override
    public boolean isIndexed() {
        return true;
    }

    @Override
    public boolean isTokenized() {
        return false;
    }

    @Override
    public boolean isTermVectorStored() {
        return false;
    }

    @Override
    public boolean isStoreOffsetWithTermVector() {
        return false;
    }

    @Override
    public boolean isStorePositionWithTermVector() {
        return false;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean getOmitNorms() {
        return true;
    }

    @Override
    public void setOmitNorms(boolean omitNorms) {
        if (omitNorms != getOmitNorms()) {
            throw new IllegalArgumentException("GeoCoordinate fields only support " + getOmitNorms()
                    + " for omitNorms");
        }
    }

    @Override
    public boolean isLazy() {
        return false;
    }

    @Override
    public int getBinaryOffset() {
        return 0;
    }

    @Override
    public int getBinaryLength() {
        return 0;
    }

    @Override
    /** 
     *  Returns always <code>null</code> for GeoCoordinate fields 
     *  Use getGeoCoordinate to retrieve results instead. 
     */
    public byte[] getBinaryValue() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IndexOptions getIndexOptions() {
        return IndexOptions.DOCS_ONLY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndexOptions(IndexOptions indexOptions) {
        if (indexOptions != getIndexOptions()) {
            throw new IllegalArgumentException("GeoCoordinate fields only support " + getIndexOptions()
                    + " for indexOptions");

        }
    }

}
