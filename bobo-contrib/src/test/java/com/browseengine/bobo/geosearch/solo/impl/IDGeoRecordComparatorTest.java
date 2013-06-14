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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.annotation.IfProfileValue;

import com.browseengine.bobo.geosearch.solo.bo.IDGeoRecord;

/**
 * 
 * @author gcooney
 *
 */
@IfProfileValue(name = "test-suite", values = { "unit", "all" })
public class IDGeoRecordComparatorTest {
    final IDGeoRecordComparator comparator = new IDGeoRecordComparator();
    
    @Test
    public void testEqual() {
        long highOrder = 10l;
        int lowOrder = 20;
        byte[] id = new byte[] {(byte) 5};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder, lowOrder, id); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder, lowOrder, id);

        assertEquals(0, comparator.compare(idGeoRecord1, idGeoRecord2));
    }
    
    @Test
    public void testHighOrderGreater() {
        long highOrder1 = 15l;
        long highOrder2 = 10l;
        int lowOrder1 = 10;
        int lowOrder2 = 20;
        byte[] id = new byte[] {(byte) 5};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id);

        assertTrue("Should be greater than 0", comparator.compare(idGeoRecord1, idGeoRecord2) > 0);
    }
    
    @Test
    public void testHighOrderLesser() {
        long highOrder1 = 10l;
        long highOrder2 = 15l;
        int lowOrder1 = 20;
        int lowOrder2 = 10;
        byte[] id = new byte[] {(byte) 5};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id);

        assertTrue("Should be less than 0", comparator.compare(idGeoRecord1, idGeoRecord2) < 0);
    }
    
    @Test
    public void testLowOrderGreater() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 20;
        int lowOrder2 = 10;
        byte[] id = new byte[] {(byte) 5};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id);

        assertTrue("Should be greater than 0", comparator.compare(idGeoRecord1, idGeoRecord2) > 0);
    }
    
    @Test
    public void testLowOrderLesser() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 10;
        int lowOrder2 = 20;
        byte[] id = new byte[] {(byte) 5};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id);

        assertTrue("Should be less than 0", comparator.compare(idGeoRecord1, idGeoRecord2) < 0);
    }
    
    @Test
    public void testIDGreater() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 10;
        int lowOrder2 = 10;
        byte[] id1 = new byte[] {(byte) 12};
        byte[] id2 = new byte[] {(byte) 10};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id1); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id2);

        assertTrue("Should be greater than 0", comparator.compare(idGeoRecord1, idGeoRecord2) > 0);
    }
    
    @Test
    public void testIDLesser() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 10;
        int lowOrder2 = 10;
        byte[] id1 = new byte[] {(byte) 8};
        byte[] id2 = new byte[] {(byte) 10};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id1); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id2);

        assertTrue("Should be less than 0", comparator.compare(idGeoRecord1, idGeoRecord2) < 0);
    }
    
    @Test
    public void testIDLonger() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 10;
        int lowOrder2 = 10;
        byte[] id1 = new byte[] {(byte) 12, (byte) -16};
        byte[] id2 = new byte[] {(byte) 10};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id1); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id2);

        assertTrue("Should be greater than 0", comparator.compare(idGeoRecord1, idGeoRecord2) > 0);
    }
    
    @Test
    public void testIDShorter() {
        long highOrder1 = 15l;
        long highOrder2 = 15l;
        int lowOrder1 = 10;
        int lowOrder2 = 10;
        byte[] id1 = new byte[] {(byte) 8};
        byte[] id2 = new byte[] {(byte) 10, (byte) 12};
        
        IDGeoRecord idGeoRecord1 = new IDGeoRecord(highOrder1, lowOrder1, id1); 
        IDGeoRecord idGeoRecord2 = new IDGeoRecord(highOrder2, lowOrder2, id2);

        assertTrue("Should be less than 0", comparator.compare(idGeoRecord1, idGeoRecord2) < 0);
    }
}
