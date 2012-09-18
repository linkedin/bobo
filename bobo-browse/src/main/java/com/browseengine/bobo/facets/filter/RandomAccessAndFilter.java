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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.docidset.RandomAccessDocIdSet;
import com.kamikaze.docidset.impl.AndDocIdSet;

public class RandomAccessAndFilter extends RandomAccessFilter
{
  private static final long serialVersionUID = 1L;
 
  protected List<RandomAccessFilter> _filters;
  
  public RandomAccessAndFilter(List<RandomAccessFilter> filters)
  {
    _filters = filters;
  }
  
  public double getFacetSelectivity(BoboIndexReader reader)
  {
    double selectivity = Double.MAX_VALUE;
    for(RandomAccessFilter filter : _filters)
    {
      double curSelectivity = filter.getFacetSelectivity(reader);
      if(selectivity > curSelectivity)
      {
        selectivity = curSelectivity;
      }
    }
    if(selectivity > 0.999)
    {
      selectivity = 1.0;
    }
    return selectivity;
  }
  
  @Override
  public RandomAccessDocIdSet getRandomAccessDocIdSet(BoboIndexReader reader) throws IOException
  {
    if(_filters.size() == 1)
    {
      return _filters.get(0).getRandomAccessDocIdSet(reader);
    }
    else
    {
      List<DocIdSet> list = new ArrayList<DocIdSet>(_filters.size());
      List<RandomAccessDocIdSet> randomAccessList = new ArrayList<RandomAccessDocIdSet>(_filters.size());
      for (RandomAccessFilter f : _filters)
      {
        RandomAccessDocIdSet s = f.getRandomAccessDocIdSet(reader);
        list.add(s);
        randomAccessList.add(s);
      }
      final RandomAccessDocIdSet[] randomAccessDocIdSets = randomAccessList.toArray(new RandomAccessDocIdSet[randomAccessList.size()]);
      final DocIdSet andDocIdSet = new AndDocIdSet(list);
      return new RandomAccessDocIdSet()
      {
        @Override
        public boolean get(int docId)
        {
          for(RandomAccessDocIdSet s : randomAccessDocIdSets)
          {
            if(!s.get(docId)) return false;
          }
          return true;
        }

        @Override
        public DocIdSetIterator iterator() throws IOException
        {
          return andDocIdSet.iterator();
        }
      };
    }
  }

}
