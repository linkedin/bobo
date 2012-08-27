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

package com.browseengine.bobo.util.test;

import com.browseengine.bobo.util.BigIntArray;

import junit.framework.TestCase;

public class BigIntArrayTest extends TestCase
{
  public static void testBigIntArray()
  {
    int count = 5000000;
    BigIntArray test = new BigIntArray(count);
    int[] test2 = new int[count];
    for (int i = 0; i < count; i++)
    {
      test.add(i, i);
      test2[i]=i;
    }
    
    for (int i = 0; i< count; i++)
    {
      assertEquals(0, test.get(0));
    }
    
    int k = 0;
    long start = System.currentTimeMillis();
    for (int i = 0; i < count; i++)
    {
      k = test.get(i);
    }
    long end = System.currentTimeMillis();
    System.out.println("Big array took: "+(end-start));
    
    start = System.currentTimeMillis();
    for (int i = 0; i < count; i++)
    {
      k = test2[i];
    }
    end=System.currentTimeMillis();
    System.out.println("int[] took: "+(end-start));
  }
}
