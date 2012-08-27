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

package com.browseengine.bobo.facets.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.util.OpenBitSet;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.BrowseSelection;
import com.browseengine.bobo.api.FacetIterator;
import com.browseengine.bobo.api.FacetSpec;
import com.browseengine.bobo.facets.data.MultiValueFacetDataCache;
import com.browseengine.bobo.facets.impl.DefaultFacetCountCollector;
import com.browseengine.bobo.util.BigNestedIntArray;

public  final class AttributesFacetCountCollector extends DefaultFacetCountCollector {
  private final AttributesFacetHandler attributesFacetHandler;
  public final BigNestedIntArray _array;
  private int[] buffer;   
  private List<BrowseFacet> cachedFacets;
  private final int numFacetsPerKey;
  private final char separator;
  private OpenBitSet excludes;
  private OpenBitSet includes;
  private final MultiValueFacetDataCache dataCache;
  private String[] values;
  
  @SuppressWarnings("rawtypes")
  public AttributesFacetCountCollector(AttributesFacetHandler attributesFacetHandler, String name, MultiValueFacetDataCache dataCache, int docBase, BrowseSelection browseSelection, FacetSpec ospec, int numFacetsPerKey, char separator){
    super(name,dataCache,docBase,browseSelection,ospec);
    this.attributesFacetHandler = attributesFacetHandler;
    this.dataCache = dataCache;
    this.numFacetsPerKey = numFacetsPerKey;
    this.separator = separator;
    _array = dataCache._nestedArray;
    if (browseSelection != null){
      values = browseSelection.getValues();
    }
  }

  @Override
  public final void collect(int docid) {    
      dataCache._nestedArray.countNoReturn(docid, _count);    
  }

  @Override
  public final void collectAll()
  {
    _count = _dataCache.freqs;
  }
  @Override
  public List<BrowseFacet> getFacets() {
    if (cachedFacets == null) {
    int max = _ospec.getMaxCount();
    _ospec.setMaxCount(max * 10);
    List<BrowseFacet> facets = super.getFacets();
    _ospec.setMaxCount(max);
    filterByKeys(facets,  separator, numFacetsPerKey, values);
    cachedFacets = facets;
    }
    return cachedFacets;
  }
  
  private void filterByKeys(List<BrowseFacet> facets, char separator, int numFacetsPerKey, String[] values) {
    Map<String, AtomicInteger> keyOccurences = new HashMap<String, AtomicInteger>();
    Iterator<BrowseFacet> iterator = facets.iterator();
    String separatorString = String.valueOf(separator);
    while (iterator.hasNext()) {
      BrowseFacet facet = iterator.next();
      String value = facet.getValue();
      if (!value.contains(separatorString)) {
        iterator.remove();
        continue;
      }
      if (values !=null && values.length > 0) {
        boolean belongsToKeys = false;       
        for (String val : values) {
          if (value.startsWith(val)) {
            belongsToKeys = true;
            break;
          }
        }
        if (!belongsToKeys) {
          iterator.remove();
          continue;
        }
      }
      String key = value.substring(0, value.indexOf(separatorString));
      AtomicInteger numOfKeys = keyOccurences.get(key);
      if (numOfKeys == null) {
        numOfKeys = new AtomicInteger(0);
        keyOccurences.put(key, numOfKeys);
      }
      int count = numOfKeys.incrementAndGet();
      if (count > numFacetsPerKey) {
        iterator.remove();
      }
    }    
  }

  @Override
  public FacetIterator iterator() {    
    return new AttributesFacetIterator(getFacets());
  }  
}
