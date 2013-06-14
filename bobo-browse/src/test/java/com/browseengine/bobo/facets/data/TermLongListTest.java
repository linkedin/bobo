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

package com.browseengine.bobo.facets.data;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class TermLongListTest extends TestCase {

  @Test
  public void test1TwoNegativeValues() {
    TermLongList list = new TermLongList();       
    list.add(null);
    list.add("-1");
    list.add("-2");   
    list.add("0");
    list.add("1");
   
    list.seal();
    assertTrue( Arrays.equals(new long[] {0, -2, -1, 0, 1 }, list.getElements()));
  }
  @Test
  public void test2ThreeNegativeValues() {
    TermLongList list = new TermLongList();       
    list.add(null);
    list.add("-1");
    list.add("-2"); 
    list.add("-3"); 
    list.add("0");
    list.add("1");
   
    list.seal();
    assertTrue( Arrays.equals(new long[] {0, -3, -2, -1, 0, 1 }, list.getElements()));
  }
  @Test
  public void test2aThreeNegativeValuesInt() {
    TermIntList list = new TermIntList();       
    list.add(null);
    list.add("-1");
    list.add("-2"); 
    list.add("-3"); 
    list.add("0");
    list.add("1");
   
    list.seal();
    assertTrue( Arrays.equals(new int[] {0, -3, -2, -1, 0, 1 }, list.getElements()));
  }
  @Test
  public void test2bThreeNegativeValuesShort() {
    TermShortList list = new TermShortList();       
    list.add(null);
    list.add("-1");
    list.add("-2"); 
    list.add("-3"); 
    list.add("0");
    list.add("1");
   
    list.seal();
    assertTrue( Arrays.equals(new short[] {0, -3, -2, -1, 0, 1 }, list.getElements()));
  }
  @Test
  public void test3ThreeNegativeValuesWithoutDummy() {
    TermLongList list = new TermLongList();      
    
    list.add("-1");
    list.add("-2"); 
    list.add("-3"); 
    list.add("0");
    list.add("1");
   
    list.seal();
    assertTrue( Arrays.equals(new long[] {-3, -2, -1, 0, 1 }, list.getElements()));
  }
}
