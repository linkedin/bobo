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

import java.util.List;
import java.util.NoSuchElementException;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.FacetIterator;

/**
 * @author nnarkhed
 *
 */
public class PathFacetIterator extends FacetIterator {

	private BrowseFacet[] _facets;
	private int _index;
	
	/**
	 * @param facets a value ascending sorted list of BrowseFacets
	 */
	public PathFacetIterator(List<BrowseFacet> facets) {
		_facets = facets.toArray(new BrowseFacet[facets.size()]);
		_index = -1;
		facet = null;
		count = 0;
	}
	
	/* (non-Javadoc)
	 * @see com.browseengine.bobo.api.FacetIterator#next()
	 */
	public Comparable next() {
		if((_index >= 0) && !hasNext())
			throw new NoSuchElementException("No more facets in this iteration");
		_index++;
		facet = _facets[_index].getValue();
		count = _facets[_index].getFacetValueHitCount();
		return facet;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return ( (_index >= 0) && (_index < (_facets.length-1)) );
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove() method not supported for Facet Iterators");
	}

  /* (non-Javadoc)
   * @see com.browseengine.bobo.api.FacetIterator#next(int)
   */
  public Comparable next(int minHits)
  {
    while(++_index < _facets.length)
    {
      if(_facets[_index].getFacetValueHitCount() >= minHits)
      {
        facet = _facets[_index].getValue();
        count = _facets[_index].getFacetValueHitCount();
        return facet;
      }
    }
    facet = null;
    count = 0;
    return facet;  
  }

  /**
   * The string from here should be already formatted. No need to reformat.
   * @see com.browseengine.bobo.api.FacetIterator#format(java.lang.Object)
   */
  @Override
  public String format(Object val)
  {
    return (String)val;
  }
}
