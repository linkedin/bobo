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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CartesianComputeDistanceTest {

    @Test
    public void testComputeDistanceSquared() {
        int x1 = 2, y1 = 3, z1 = 6;
        
        double distancesq = CartesianComputeDistance.computeDistanceSquared(x1, y1, z1, 0, 0, 0);
        assertEquals(49, distancesq, 0.001);
    }
 
    @Test
    public void testComputeDistance() {
        int x1 = 2, y1 = 3, z1 = 6;
        
        double distancesq = CartesianComputeDistance.computeDistance(x1, y1, z1, 0, 0, 0);
        assertEquals(7, distancesq, 0.001);
    }
    
    @Test
    public void testComputeDistance_offset() {
        int x1 = 2, y1 = 3, z1 = 6;
        int offset = 500;
        
        double distancesq = CartesianComputeDistance.computeDistance(x1 + offset, y1 + offset, z1 + offset, offset, offset, offset);
        assertEquals(7, distancesq, 0.001);
    }
    
    @Test
    public void testComputeDistance_switchparameterOrder() {
        int x1 = 2, y1 = 3, z1 = 6;
        
        double distancesq = CartesianComputeDistance.computeDistance(0, 0, 0, x1, y1, z1);
        assertEquals(7, distancesq, 0.001);
    }
    
    @Test
    public void testComputeDistance_decimalResult() {
        int x1 = 2, y1 = 3, z1 = 7;
        
        double distancesq = CartesianComputeDistance.computeDistance(x1, y1, z1, 0, 0, 0);
        assertEquals(7.874007874, distancesq, 0.000001);
    }
    
    @Test
    public void testComputeDistance_max() {
        int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, z1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE, z2 = Integer.MIN_VALUE;
        
        double distancesq = CartesianComputeDistance.computeDistance(x1, y1, z1, x2, y2, z2);
        assertEquals(7439101571.787, distancesq, 1000);
    }
}
