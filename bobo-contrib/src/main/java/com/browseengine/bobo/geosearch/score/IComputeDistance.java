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

package com.browseengine.bobo.geosearch.score;

/**
 * @author Ken McCracken
 *
 */
public interface IComputeDistance {

    /**
     * Given two points A and B on the surface of the Earth, 
     * this function computes the distance between these two 
     * points, where inputs are expressed in decimal degrees longitude and 
     * decimal degrees latitude. 
     * 
     * @param longitudeInDegreesA
     * @param latitudeInDegreesA
     * @param longitudeInDegreesB
     * @param latitudeInDegreesB
     * @return the distance between A and B, in miles
     */
    float getDistanceInMiles(double longitudeInDegreesA, double latitudeInDegreesA,
            double longitudeInDegreesB, double latitudeInDegreesB);

    /**
     * The delta in the latitudinal dimension in degrees, to 
     * go radiusInMiles miles from the point (longitudeInDegrees, latitudeInDegrees)
     * on the surface of the Earth.
     * 
     * @param radiusInMiles
     * @return the delta latitude, in decimal degrees
     */
    double computeLatBoundary(float radiusInMiles);
    
    /**
     * The delta in the longitudinal dimension in degrees, 
     * to go radiusInMiles miles from the point (longitudeInDegrees, latitudeInDegrees) 
     * on the surface of the Earth.
     * 
     * @param latitudeInDegrees
     * @param radiusInMiles
     * @return
     */
    double computeLonBoundary(double latitudeInDegrees,
            float radiusInMiles);
    
}
