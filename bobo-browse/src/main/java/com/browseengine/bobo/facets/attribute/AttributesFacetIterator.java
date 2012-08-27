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

import java.util.Iterator;
import java.util.List;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.FacetIterator;

public class AttributesFacetIterator extends FacetIterator {
  private Iterator<BrowseFacet> iterator;

  public AttributesFacetIterator(List<BrowseFacet> facets) {
    iterator = facets.iterator();
  }

  @Override
  public boolean hasNext() {
    // TODO Auto-generated method stub
    return iterator.hasNext();
  }

  @Override
  public void remove() {
   throw new UnsupportedOperationException();
    
  }

  @Override
  public Comparable next() {
    count = 0;
    BrowseFacet next = iterator.next();
    if (next == null) {
      return null;
    }
    count = next.getFacetValueHitCount();
    facet = next.getValue();
    return next.getValue();
  }

  @Override
  public Comparable next(int minHits) {
    while (iterator.hasNext()) {
      BrowseFacet next = iterator.next();
      count = next.getFacetValueHitCount();
      facet = next.getValue();
      if (next.getFacetValueHitCount() >= minHits) {
        return next.getValue();
      }
    }
    return null;
  }

  @Override
  public String format(Object val) {
    return val != null ? val.toString() : null;
  }
}
