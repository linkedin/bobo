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

package com.browseengine.bobo.geosearch.solo.search.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author gcooney
 *
 */
public class GeoOnlyHitQueueTest {
    
    @Test
    public void testRetrieveInOrder() {
        int queueSize = 10;
        int hitSize = 10;
        
        queueHitsAndVerify(queueSize, hitSize);
    }
    
    @Test
    public void testRetrieveInOrder_FewerHitsThanQueue() {
        int queueSize = 10;
        int hitSize = 5;
        
        queueHitsAndVerify(queueSize, hitSize);
    }
    
    @Test
    public void testRetrieveInOrder_MoreHitsThanQueue() {
        int queueSize = 10;
        int hitSize = 20;
        
        queueHitsAndVerify(queueSize, hitSize);
    }
    
    private void queueHitsAndVerify(int queueSize, int hitSize) {
        GeoOnlyHitQueue hitQueue = new GeoOnlyHitQueue(queueSize);
        
        for (int i = 0; i < hitSize; i++) {
            byte[] uuid = new byte[] {(byte)i};
            double score = i;
            GeoOnlyHit hit = new GeoOnlyHit(score, uuid);
            hitQueue.insertWithOverflow(hit);
        }
        
        GeoOnlyHit lastHit = hitQueue.pop();
        assertEquals(Math.min(hitSize - 1, queueSize - 1), lastHit.score, 0.0000001);
        
        GeoOnlyHit currentHit = hitQueue.pop();
        int hitCount = 1;
        while (currentHit != null) {
            assertTrue("Hits should be in descending order", lastHit.score > currentHit.score);
            hitCount++;
            
            lastHit = currentHit;
            currentHit = hitQueue.pop();
        }

        assertEquals(Math.min(queueSize, hitSize), hitCount);
    }
}
