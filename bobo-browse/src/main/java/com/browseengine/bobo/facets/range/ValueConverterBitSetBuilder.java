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

package com.browseengine.bobo.facets.range;

import org.apache.lucene.util.OpenBitSet;

import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.filter.FacetValueConverter;

public class ValueConverterBitSetBuilder implements BitSetBuilder {
  private final FacetValueConverter facetValueConverter;
  private final String[] vals;
  private final boolean takeCompliment;

  public ValueConverterBitSetBuilder(FacetValueConverter facetValueConverter, String[] vals,boolean takeCompliment) {
    this.facetValueConverter = facetValueConverter;
    this.vals = vals;
    this.takeCompliment = takeCompliment;    
  }

  @Override
  public OpenBitSet bitSet(FacetDataCache dataCache) {
    int[] index = facetValueConverter.convert(dataCache, vals);
    
    OpenBitSet bitset = new OpenBitSet(dataCache.valArray.size());
    for (int i : index) {
      bitset.fastSet(i);
    }
    if (takeCompliment)
    {
      // flip the bits
      for (int i=0; i < index.length; ++i){
        bitset.fastFlip(i);
      }
    }
    return bitset;
  }

}
