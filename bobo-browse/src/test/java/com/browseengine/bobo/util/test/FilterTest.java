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

import java.util.BitSet;

import junit.framework.TestCase;

import org.apache.lucene.search.DocIdSetIterator;

import com.browseengine.bobo.docidset.FilteredDocSetIterator;
import com.kamikaze.docidset.impl.IntArrayDocIdSet;

public class FilterTest extends TestCase
{
  public void testFilterdDocSetIterator()
  {
    IntArrayDocIdSet set1 = new IntArrayDocIdSet();
    for (int i=0;i<100;++i)
    {
      set1.addDoc(2*i);         // 100 even numbers
    }
    
    DocIdSetIterator filteredIter = new FilteredDocSetIterator(set1.iterator())
    {

      @Override
      protected boolean match(int doc)
      {
        return doc%5 == 0;
      }
    };
    
    BitSet bs = new BitSet();
    for (int i=0;i<100;++i)
    {
      int n = 10*i;
      if (n < 200)
      {
        bs.set(n);
      }
    }
    
    try
    {
      int doc;
      while((doc=filteredIter.nextDoc())!=DocIdSetIterator.NO_MORE_DOCS)
      {
        if (!bs.get(doc)){
          fail("failed: "+doc+" not in expected set");
          return;
        }
        else
        {
          bs.clear(doc);
        }
      }
      if (bs.cardinality()>0)
      {
        fail("failed: leftover cardinatity: "+bs.cardinality());
      }
    }
    catch(Exception e)
    {
      fail(e.getMessage());
    }
  }
}
