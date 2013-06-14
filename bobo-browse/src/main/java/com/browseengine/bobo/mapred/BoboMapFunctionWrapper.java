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

package com.browseengine.bobo.mapred;

import java.util.List;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.facets.FacetCountCollector;

/**
 * Is the part of the bobo request, that maintains the map result intermediate state
 *
 */
public interface BoboMapFunctionWrapper {
	/**
	 * When there is no filter, map reduce will try to map the entire segment
	 * @param reader
	 */
	public void mapFullIndexReader(BoboIndexReader reader, FacetCountCollector[] facetCountCollectors);
	/**
	 * The basic callback method for a single doc
	 * @param docId
	 * @param reader
	 */
	public void mapSingleDocument(int docId, BoboIndexReader reader);
	/**
	 * The callback method, after the segment was processed
	 * @param reader
	 */
	public void finalizeSegment(BoboIndexReader reader,  FacetCountCollector[] facetCountCollectors);
	/**
   * The callback method, after the partition was processed
   * 
   */
	public void finalizePartition();	
	public MapReduceResult getResult();
}
