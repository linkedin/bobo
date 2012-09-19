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

package com.browseengine.bobo.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.browseengine.bobo.api.FacetIterator;
import com.browseengine.bobo.api.IntFacetIterator;
import com.browseengine.bobo.facets.data.TermIntList;
import com.browseengine.bobo.facets.data.TermStringList;
import com.browseengine.bobo.facets.impl.CombinedFacetIterator;
import com.browseengine.bobo.facets.impl.CombinedIntFacetIterator;
import com.browseengine.bobo.facets.impl.DefaultFacetIterator;
import com.browseengine.bobo.facets.impl.DefaultIntFacetIterator;
import com.browseengine.bobo.util.BigIntArray;


public class BoboFacetIteratorTest extends TestCase
{
  static Logger log = Logger.getLogger(BoboFacetIteratorTest.class);
  public BoboFacetIteratorTest(String testName){
    super(testName);
    String confdir = System.getProperty("conf.dir");
    if (confdir == null) confdir ="./resource";
    org.apache.log4j.PropertyConfigurator.configure(confdir+"/log4j.properties");
  }
  
  public void testTermStringListAddWrongOrder()
  {
    TermStringList tsl1 = new TermStringList();
    tsl1.add(null);
    tsl1.add("m");
    try
    {
      tsl1.add("a");
    } catch(Exception e)
    {
      assertTrue("There should be an exception and the message contains ascending order", e.getMessage().contains("ascending order"));
      return;
    }
    fail("There should be an exception and the message contains ascending order");
  }

  public void testTermStringListAddCorrectOrder()
  {
    TermStringList tsl1 = new TermStringList();
    tsl1.add(null);
    tsl1.add("");
    try
    {
      tsl1.add("m");
      tsl1.add("s");
      tsl1.add("t");
    } catch(Exception e)
    {
      fail("There should NOT be an exception and the message contains ascending order");
      return;
    }
    tsl1.seal();
    assertEquals("Should skip index 0 which is used for dummy null", 1, tsl1.indexOf(""));
  }

  
  
  public void testTermIntListAddCorrectOrder()
  {
    TermIntList tsl1 = new TermIntList("000");
    tsl1.add(null);
    tsl1.add("0");
    try
    {
      tsl1.add("1");
      tsl1.add("2");
      tsl1.add("3");
    } catch(Exception e)
    {
      fail("There should NOT be an exception and the message contains ascending order");
      return;
    }
    tsl1.seal();
    assertEquals("Should skip index 0 which is used for dummy null", 1, tsl1.indexOf(0));
  }
  
  public void testDefaultFaccetIterator()
  {
    TermStringList tsl1 = new TermStringList();
    tsl1.add("i");
    tsl1.add("m");
    tsl1.seal();
    BigIntArray count = new BigIntArray(2);
    count.add(0, 1);
    count.add(1, 2);
    DefaultFacetIterator itr1 = new DefaultFacetIterator(tsl1, count, 2, false);
    TermStringList tsl2 = new TermStringList();
    tsl2.add("i");
    tsl2.add("m");
    tsl2.seal();
    BigIntArray count2 = new BigIntArray(2);
    count2.add(0, 1);
    count2.add(1, 5);
    DefaultFacetIterator itr2 = new DefaultFacetIterator(tsl2, count2, 2, true);
    List<FacetIterator> list = new ArrayList<FacetIterator>();
    list.add(itr1);
    list.add(itr2);
    CombinedFacetIterator ctr = new CombinedFacetIterator(list);
    String result = "";
    while(ctr.hasNext())
    {
      ctr.next();
      result += ctr.facet;
      result += ctr.count;
    }
    assertEquals("result should be i1m7" , "i1m7", result);
  }


  public void testDefaultIntFacetIterator()
  {
    String format = "00";
    DecimalFormat df = new DecimalFormat(format);
    List<IntFacetIterator> list = new ArrayList<IntFacetIterator>();
    for(int seg = 0; seg < 5; seg++)
    {
      TermIntList tsl1 = new TermIntList(format);
      int limit = 25;
      BigIntArray count = new BigIntArray(limit);
      String[] terms = new String[limit];
      for(int i = limit - 1; i>=0; i--)
      {
        terms[i] = df.format(i); 
      }
      Arrays.sort(terms);
      for(int i = 0; i< limit; i++)
      {
        tsl1.add(terms[i]);
        count.add(i, i);
      }
      tsl1.seal();
      DefaultIntFacetIterator itr1 = new DefaultIntFacetIterator(tsl1, count, limit, true);
      list.add(itr1);
    }
    CombinedIntFacetIterator ctr = new CombinedIntFacetIterator(list);
    String result = "";
    while(ctr.hasNext())
    {
      ctr.next();
      result += (ctr.facet + ":" + ctr.count +" ");
    }
    String expected = "1:5 2:10 3:15 4:20 5:25 6:30 7:35 8:40 9:45 10:50 11:55 12:60 13:65 14:70 15:75 16:80 17:85 18:90 19:95 20:100 21:105 22:110 23:115 24:120 ";
    assertEquals(expected, result);
  }
}
