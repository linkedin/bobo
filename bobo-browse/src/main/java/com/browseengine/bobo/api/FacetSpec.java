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
import java.util.HashMap;
import java.util.Map;

/**
 * specifies how facets are to be returned for a browse
 *
 */
public class FacetSpec implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Sort options for facets
	 */
	public static enum FacetSortSpec
	{
	  /**
	   * Order by the facet values in lexographical ascending order
	   */
		OrderValueAsc,
		
		/**
		 * Order by the facet hit counts in descending order
		 */
		OrderHitsDesc,
		
		/**
		 * custom order, must have a comparator
		 */
		OrderByCustom
	}
			
	private FacetSortSpec orderBy;
	private int max;
	private boolean expandSelection;
	private int minCount;
	private ComparatorFactory _comparatorFactory;
	private Map<String, String> properties;
	/**
	 * Constructor.
	 *
	 */
	public FacetSpec(){
		orderBy=FacetSortSpec.OrderValueAsc;
		minCount=1;
		expandSelection = false;
		_comparatorFactory = null;
		properties = new HashMap<String, String>();
	}				
	
	public FacetSpec setCustomComparatorFactory(ComparatorFactory comparatorFactory){
		_comparatorFactory = comparatorFactory;
    return this;
	}
	
	public ComparatorFactory getCustomComparatorFactory(){
		return _comparatorFactory;
	}
	
    /**
	 * Sets the minimum number of hits a choice would need to have to be returned.
	 *
     * @param minCount minimum count
     * @see #getMinHitCount()
	 */
	public FacetSpec setMinHitCount(int minCount){
		this.minCount=minCount;
    return this;
	}
	
	/**
	 * Gets the minimum number of hits a choice would need to have to be returned.
	 * @return minimum count
	 * @see #setMinHitCount(int)
	 */
	public int getMinHitCount(){
		return minCount;
	}
	
	/**
	 * Get the current choice sort order
	 * @return choice sort order
	 * @see #setOrderBy(FacetSortSpec)
	 */
	public FacetSortSpec getOrderBy() {
		return orderBy;
	}

	/**
	 * Sets the choice sort order
	 *
   * @param order sort order
   * @see #getOrderBy()
	 */
	public FacetSpec setOrderBy(FacetSortSpec order) {
		orderBy = order;
    return this;
	}				

	/**
	 * Gets the maximum number of choices to return
	 * @return max number of choices to return
	 * @see #setMaxCount(int)
	 */
	public int getMaxCount() {
		return max;
	}

	/**
	 * Sets the maximum number of choices to return.
	 *
   * @param maxCount max number of choices to return, default = 0 which means all
   * @see #getMaxCount()
	 */
	public FacetSpec setMaxCount(int maxCount) {
		max = maxCount;
    return this;
	}

	@Override
	public String toString(){
		StringBuffer buffer=new StringBuffer();			
		buffer.append("orderBy: ").append(orderBy).append("\n");
		buffer.append("max count: ").append(max).append("\n");
		buffer.append("min hit count: ").append(minCount).append("\n");
		buffer.append("expandSelection: ").append(expandSelection);
		return buffer.toString();
	}

	/**
	 * Gets whether we are expanding sibling choices
	 * @return A boolean indicating whether to expand sibling choices.
	 * @see #setExpandSelection(boolean)
	 */
	public boolean isExpandSelection() {
		return expandSelection;
	}

	/**
	 * Sets whether we are expanding sibling choices
	 *
   * @param expandSelection indicating whether to expand sibling choices.
   * @see #isExpandSelection()
	 */
	public FacetSpec setExpandSelection(boolean expandSelection) {
		this.expandSelection = expandSelection;
    return this;
	}

  /**
   * Gets  custom properties for the facet search. For example AttributeFacetHandler uses this to perform custom facet filtering
   */
  public Map<String, String> getProperties() {
    return properties;
  }

  /**
   * Sets  custom properties for the facet search. For example AttributeFacetHandler uses this to perform custom facet filtering
   * @param properties
   */
  public FacetSpec setProperties(Map<String, String> properties) {
    this.properties = properties;
    return this;
  }

  @Override
  public FacetSpec clone() {
    Map<String, String> properties = getProperties();
    Map<String, String> clonedProperties = new HashMap<String, String>(properties.size());
    clonedProperties.putAll(properties);

    return new FacetSpec()
      .setCustomComparatorFactory(getCustomComparatorFactory())
      .setExpandSelection(isExpandSelection())
      .setMaxCount(getMaxCount())
      .setMinHitCount(getMinHitCount())
      .setOrderBy(getOrderBy())
      .setProperties(clonedProperties);
  }
}
