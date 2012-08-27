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

package com.browseengine.bobo.geosearch.query;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Weight;

import com.browseengine.bobo.geosearch.impl.GeoUtil;

/**
 * @author Shane Detsch
 * @author Ken McCracken
 *
 */
public class GeoQuery extends Query {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    double centroidLongitude;
    double centroidLatitude;
    float rangeInMiles;
    
    private static final float KM_TO_MILES = (float)(3.1/5);
    private static final float MINIMUM_RANGE_IN_MILES = 0.001f;
    private static final float MAXIMUM_RANGE_IN_MILES = 500f;
    
    public GeoQuery(double centroidLongitude, double centroidLatitude, Float rangeInMiles, Float rangeInKilometers) {
        this.centroidLongitude = centroidLongitude;
        this.centroidLatitude = centroidLatitude;
        if (!(null == rangeInMiles ^ null == rangeInKilometers)) {
            throw new RuntimeException("please specify either rangeInMiles or rangeInKilometers");
        }
        if (null != rangeInKilometers) {
            this.rangeInMiles = KM_TO_MILES * rangeInKilometers;
        } else {
            this.rangeInMiles = rangeInMiles;
        }
        if (this.rangeInMiles < MINIMUM_RANGE_IN_MILES || this.rangeInMiles > MAXIMUM_RANGE_IN_MILES) {
            throw new RuntimeException("rangeInMiles out of range ["+MINIMUM_RANGE_IN_MILES+", "+MAXIMUM_RANGE_IN_MILES+"]: "+this.rangeInMiles);
        }
        if (!GeoUtil.isValidLongitude(centroidLongitude) || !GeoUtil.isValidLatitude(centroidLatitude)) {
            throw new RuntimeException("bad centroidLongitude "+centroidLongitude+" or centroidLatitude "+centroidLatitude);
        }
    }
    
    
    
    /**
     * @return the centroidLongitude
     */
    public double getCentroidLongitude() {
        return centroidLongitude;
    }



    /**
     * @return the centroidLatitude
     */
    public double getCentroidLatitude() {
        return centroidLatitude;
    }



    /**
     * @return the rangeInMiles
     */
    public float getRangeInMiles() {
        return rangeInMiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Weight createWeight(Searcher searcher) throws IOException {
        return new GeoWeight(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GeoQuery [centroidLatitude=" + centroidLatitude + ", centroidLongitude=" + centroidLongitude
                + ", rangeInMiles=" + rangeInMiles + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(String arg0) {
        return toString();
    }

    

}
