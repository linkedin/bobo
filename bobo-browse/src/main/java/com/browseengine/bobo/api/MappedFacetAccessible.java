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

package com.browseengine.bobo.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.browseengine.bobo.facets.impl.PathFacetIterator;

public class MappedFacetAccessible implements FacetAccessible, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final HashMap<String,BrowseFacet> _facetMap;
	private final BrowseFacet[] _facets;
	
	public MappedFacetAccessible(BrowseFacet[] facets){
		_facetMap = new HashMap<String,BrowseFacet>();
		for (BrowseFacet facet : facets){
			_facetMap.put(facet.getValue(), facet);
		}
		_facets = facets;
	}

	public BrowseFacet getFacet(String value) {
		return _facetMap.get(value);
	}

  public int getFacetHitsCount(Object value)
  {
    BrowseFacet facet = _facetMap.get(value);
    if (facet != null)
      return facet.getHitCount();
    return 0;
  }

	public List<BrowseFacet> getFacets() {
		return Arrays.asList(_facets);
	}

	public void close()
	{
		// TODO Auto-generated method stub

	}

	public FacetIterator iterator() {
		return new PathFacetIterator(Arrays.asList(_facets));
	}

}
