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

package com.browseengine.bobo.geosearch.score.impl;

import com.browseengine.bobo.geosearch.score.IComputeDistance;

/**
 * @author Ken McCracken
 *
 */
public class HaversineComputeDistance implements IComputeDistance {

    /**
     * {@inheritDoc}
     */
    @Override
    public float getDistanceInMiles(double longitudeInDegreesA, double latitudeInDegreesA, 
            double longitudeInDegreesB,
            double latitudeInDegreesB) {
        double longitudeInRadiansA = Conversions.d2r(longitudeInDegreesA);
        double latitudeInRadiansA = Conversions.d2r(latitudeInDegreesA);
        double longitudeInRadiansB = Conversions.d2r(longitudeInDegreesB);
        double latitudeInRadiansB = Conversions.d2r(latitudeInDegreesB);
        
        return HaversineFormula.computeHaversineDistanceMiles(longitudeInRadiansA, 
                latitudeInRadiansA, longitudeInRadiansB, latitudeInRadiansB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double computeLatBoundary(float radiusInMiles) {
        double latBoundaryRadians = HaversineFormula.computeLatBoundary(radiusInMiles);
        
        return Conversions.r2d(latBoundaryRadians);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double computeLonBoundary(double latitudeInDegrees, 
            float radiusInMiles) {
        double latitudeInRadians = Conversions.d2r(latitudeInDegrees);
        double lonBoundaryRadians = HaversineFormula.computeLonBoundary(latitudeInRadians, 
                radiusInMiles);
        
        return Conversions.r2d(lonBoundaryRadians);
    }
    
    private final float ONEKMDIFFX =  82694f;  // 16 powers of 2 is 65,536
    private final float ONEKMDIFFY = 224679f;  // 18 powers of 2 is 262,144
    private final float ONEKMDIFFZ = 234124f;
    
}
