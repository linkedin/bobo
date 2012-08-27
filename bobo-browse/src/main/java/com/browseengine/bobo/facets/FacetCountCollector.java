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

package com.browseengine.bobo.facets;

import java.util.LinkedList;
import java.util.List;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.FacetAccessible;

/**
 *  Collects facet counts for a given browse request
 */
public interface FacetCountCollector extends FacetAccessible
{
	/**
	 * Collect a hit. This is called for every hit, thus the implementation needs to be super-optimized.
	 * @param docid doc
	 */
	void collect(int docid);
	
	/**
	 * Collects all hits. This is called once per request by the facet engine in certain scenarios. 
	 */
	void collectAll();
	
	/**
	 * Gets the name of the facet
	 * @return facet name
	 */
	String getName();
	
	/**
	 * Returns an integer array representing the distribution function of a given facet.
	 * @return integer array of count values representing distribution of the facet values.
	 */
	int[] getCountDistribution();
	
	/**
	 * Empty facet list. 
	 */
	public static List<BrowseFacet> EMPTY_FACET_LIST = new LinkedList<BrowseFacet>();

}
