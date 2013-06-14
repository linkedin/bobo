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

package com.browseengine.bobo.facets.filter;

import java.io.IOException;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.docidset.RandomAccessDocIdSet;
import com.kamikaze.docidset.impl.NotDocIdSet;

public class RandomAccessNotFilter extends RandomAccessFilter
{
  private static final long serialVersionUID = 1L;

  protected final RandomAccessFilter _innerFilter;
  
  public RandomAccessNotFilter(RandomAccessFilter innerFilter)
  {
    _innerFilter = innerFilter;
  }
  
  public double getFacetSelectivity(BoboIndexReader reader)
  {
    double selectivity = _innerFilter.getFacetSelectivity(reader);
    selectivity = selectivity > 0.999 ? 0.0 : (1-selectivity); 
    return selectivity;
  }
  
  @Override
  public RandomAccessDocIdSet getRandomAccessDocIdSet(BoboIndexReader reader) throws IOException
  {
    final RandomAccessDocIdSet innerDocIdSet = _innerFilter.getRandomAccessDocIdSet(reader);
    final DocIdSet notInnerDocIdSet = new NotDocIdSet(innerDocIdSet, reader.maxDoc());
    return new RandomAccessDocIdSet()
    {
      @Override
      public boolean get(int docId)
      {
        return !innerDocIdSet.get(docId);
      }
      @Override
      public DocIdSetIterator iterator() throws IOException
      {
        return notInnerDocIdSet.iterator();
      }
    };
  }

}
