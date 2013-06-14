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
import org.apache.lucene.util.OpenBitSet;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.docidset.EmptyDocIdSet;
import com.browseengine.bobo.docidset.RandomAccessDocIdSet;
import com.browseengine.bobo.facets.FacetHandler;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.MultiValueFacetDataCache;
import com.browseengine.bobo.facets.filter.FacetOrFilter.FacetOrDocIdSetIterator;
import com.browseengine.bobo.util.BigNestedIntArray;

public class MultiValueORFacetFilter extends RandomAccessFilter
{

  private static final long serialVersionUID = 1L;
  private final FacetHandler<?> _facetHandler;
  private final String[] _vals;
  private final boolean _takeCompliment;
  private final FacetValueConverter _valueConverter;

  public MultiValueORFacetFilter(FacetHandler<?> facetHandler,String[] vals,boolean takeCompliment){
	this(facetHandler,vals,FacetValueConverter.DEFAULT,takeCompliment);  
  }
  
  public MultiValueORFacetFilter(FacetHandler<?> facetHandler,String[] vals,FacetValueConverter valueConverter,boolean takeCompliment)
  {
	_facetHandler = facetHandler;
	_vals = vals;
	_valueConverter = valueConverter;
	_takeCompliment = takeCompliment;
  }
  
  public double getFacetSelectivity(BoboIndexReader reader)
  {
    double selectivity = 0;
    MultiValueFacetDataCache dataCache = (MultiValueFacetDataCache)_facetHandler.getFacetData(reader);
    int[] idxes = _valueConverter.convert(dataCache, _vals);
    if(idxes == null)
    {
      return 0.0;
    }
    int accumFreq=0;
    for(int idx : idxes)
    {
      accumFreq +=dataCache.freqs[idx];
    }
    int total = reader.maxDoc();
    selectivity = (double)accumFreq/(double)total;
    if(selectivity > 0.999) 
    {
      selectivity = 1.0;
    }
    return selectivity;
  }
  
  public final static class MultiValueOrFacetDocIdSetIterator extends FacetOrDocIdSetIterator
  {
      private final BigNestedIntArray _nestedArray;
      public MultiValueOrFacetDocIdSetIterator(MultiValueFacetDataCache dataCache, OpenBitSet bs) 
      {
        super(dataCache,bs);
        _nestedArray = dataCache._nestedArray;
      }
      
      @Override
      final public int nextDoc() throws IOException
      {
        return (_doc = (_doc < _maxID ? _nestedArray.findValues(_bitset, (_doc + 1), _maxID) : NO_MORE_DOCS));
      }

      @Override
      final public int advance(int id) throws IOException
      {
        if (_doc < id)
        {
          return (_doc = (id <= _maxID ? _nestedArray.findValues(_bitset, id, _maxID) : NO_MORE_DOCS));
        }
        return nextDoc();
      }
  }
  
  @Override
  public RandomAccessDocIdSet getRandomAccessDocIdSet(BoboIndexReader reader) throws IOException
  {
    final MultiValueFacetDataCache dataCache = (MultiValueFacetDataCache)_facetHandler.getFacetData(reader);
    final int[] index = _valueConverter.convert(dataCache, _vals);
    final BigNestedIntArray nestedArray = dataCache._nestedArray;
    final OpenBitSet bitset = new OpenBitSet(dataCache.valArray.size());
  
    for (int i : index)
    {
      bitset.fastSet(i);
    } 
  
    if (_takeCompliment)
    {
      // flip the bits
      int size = dataCache.valArray.size();
      for (int i=0;i<size;++i){
        bitset.fastFlip(i);
      }
    }
  
    long count = bitset.cardinality();
  
    if (count == 0)
    {
      final DocIdSet empty = EmptyDocIdSet.getInstance();
      return new RandomAccessDocIdSet()
      {
        @Override
        public boolean get(int docId)
        {
          return false;
        }
    
        @Override
        public DocIdSetIterator iterator() throws IOException
        {
          return empty.iterator();
        }         
      };
    }
    else
    {
      return new RandomAccessDocIdSet()
      {
        @Override
        public DocIdSetIterator iterator() 
        {
          return new MultiValueOrFacetDocIdSetIterator(dataCache,bitset);
        }

        @Override
        final public boolean get(int docId)
        {
          return nestedArray.contains(docId,bitset);
        }
      };
    }
  }

}
