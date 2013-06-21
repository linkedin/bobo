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

package com.browseengine.bobo.facets.impl;

import java.io.IOException;
import java.util.Properties;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.BoboIndexReader.WorkArea;
import com.browseengine.bobo.facets.data.MultiValueWithWeightFacetDataCache;
import com.browseengine.bobo.facets.data.TermListFactory;
import com.browseengine.bobo.facets.filter.AdaptiveFacetFilter;
import com.browseengine.bobo.facets.filter.MultiValueFacetFilter;
import com.browseengine.bobo.facets.filter.RandomAccessFilter;
import com.browseengine.bobo.facets.range.MultiDataCacheBuilder;

public class MultiValueWithWeightFacetHandler extends MultiValueFacetHandler
{
  public MultiValueWithWeightFacetHandler(String name, String indexFieldName, TermListFactory termListFactory, int invertedIndexPenalty)
  {
    super(name, indexFieldName, termListFactory, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name, String indexFieldName, int invertedIndexPenalty)
  {
    super(name, indexFieldName, null, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name, int invertedIndexPenalty)
  {
    super(name, name, null, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name,
                                          String indexFieldName,
                                          TermListFactory termListFactory)
  {
    super(name, indexFieldName, termListFactory, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  public MultiValueWithWeightFacetHandler(String name, String indexFieldName)
  {
    super(name, indexFieldName, null, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  public MultiValueWithWeightFacetHandler(String name)
  {
    super(name, name, null, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  @Override
  public RandomAccessFilter buildRandomAccessFilter(String value, Properties prop) throws IOException
  {
    MultiValueFacetFilter f= new MultiValueFacetFilter(new MultiDataCacheBuilder(getName(), _indexFieldName), value);
    return f;
  }
  @Override
  public MultiValueWithWeightFacetDataCache load(BoboIndexReader reader, WorkArea workArea) throws IOException
  {
    MultiValueWithWeightFacetDataCache dataCache = new MultiValueWithWeightFacetDataCache();
      
    dataCache.setMaxItems(_maxItems);

    if(_sizePayloadTerm == null)
    {
      dataCache.load(_indexFieldName, reader, _termListFactory, workArea);
    }
    else
    {
      dataCache.load(_indexFieldName, reader, _termListFactory, _sizePayloadTerm);
    }
    return dataCache;
  }
}
