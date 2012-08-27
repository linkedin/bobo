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

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.OpenBitSet;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.docidset.EmptyDocIdSet;
import com.browseengine.bobo.docidset.RandomAccessDocIdSet;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.MultiValueFacetDataCache;
import com.browseengine.bobo.facets.filter.AdaptiveFacetFilter.FacetDataCacheBuilder;
import com.browseengine.bobo.facets.range.BitSetBuilder;

public class BitSetFilter extends RandomAccessFilter {  
    private static final long serialVersionUID = 1L;
    
    protected final FacetDataCacheBuilder facetDataCacheBuilder;
    protected final BitSetBuilder bitSetBuilder;
    private volatile OpenBitSet bitSet;
    private volatile FacetDataCache lastCache;
    
    public BitSetFilter(BitSetBuilder bitSetBuilder, FacetDataCacheBuilder facetDataCacheBuilder) {
      this.bitSetBuilder = bitSetBuilder;    
      this.facetDataCacheBuilder = facetDataCacheBuilder;      
    }
    public OpenBitSet getBitSet( FacetDataCache dataCache) {
      
      if (lastCache == dataCache) {
        return bitSet;
      }     
      bitSet = bitSetBuilder.bitSet(dataCache);
      lastCache = dataCache;
      return bitSet;
    }
    
    @Override
    public RandomAccessDocIdSet getRandomAccessDocIdSet(final BoboIndexReader reader) throws IOException {
      final FacetDataCache dataCache = facetDataCacheBuilder.build(reader);
      final OpenBitSet openBitSet = getBitSet(dataCache);
      long count = openBitSet.cardinality();
      if (count == 0) {
        return EmptyDocIdSet.getInstance();
      } else {
        final boolean multi = dataCache instanceof MultiValueFacetDataCache;
        final MultiValueFacetDataCache multiCache = multi ? (MultiValueFacetDataCache) dataCache : null;
        
        return new RandomAccessDocIdSet() {        
          public DocIdSetIterator iterator() {          
              
              if (multi) {
                return new MultiValueORFacetFilter.MultiValueOrFacetDocIdSetIterator(multiCache, openBitSet);
              } else {
                return new FacetOrFilter.FacetOrDocIdSetIterator(dataCache, openBitSet);  
                  
            }
          }
          public boolean get(int docId) {
            if (multi) {
              return multiCache._nestedArray.contains(docId, openBitSet);
            } else {
              return openBitSet.fastGet(dataCache.orderArray.get(docId));
            }
          }
        };
      }
    }

    @Override
    public double getFacetSelectivity(BoboIndexReader reader) {
      FacetDataCache dataCache = facetDataCacheBuilder.build(reader);
      final OpenBitSet openBitSet = getBitSet(dataCache);
      int[] frequencies = dataCache.freqs;
      double selectivity = 0;
      int accumFreq = 0;
      int index = openBitSet.nextSetBit(0);
      while (index >= 0) {
        accumFreq += frequencies[index];
        index = openBitSet.nextSetBit(index + 1);
      }
      int total = reader.maxDoc();
      selectivity = (double) accumFreq / (double) total;
      if (selectivity > 0.999) {
        selectivity = 1.0;
      }
      return selectivity;
    }
}
