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

package com.browseengine.bobo.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

public class ResultMerger {
	
	public static <T> Iterator<T> mergeResults(final Iterator<T>[] results,final Comparator<T> comparator){
		
		return new Iterator<T>(){
			TreeMap<T,Iterator<T>> map=new TreeMap<T,Iterator<T>>(comparator);
			{
				for (Iterator<T> result : results)
				{
					if (result.hasNext())
					{
						map.put(result.next(),result);
					}
				}
			}
			
			public boolean hasNext() {
				return map.size()>0;
			}

			public T next() {
				T first=map.firstKey();
				Iterator<T> iter=map.remove(first);
				while (iter.hasNext())
				{
					T next=iter.next();
					if (!map.containsKey(next))
					{
						map.put(next,iter);
						break;
					}
				}
				return first;
			}

			public void remove() {
				T first=map.firstKey();
				Iterator<T> iter=map.remove(first);
				while (iter.hasNext())
				{
					T next=iter.next();
					if (!map.containsKey(next))
					{
						map.put(next,iter);
						break;
					}
				}
			}
		};
	}
	
	/*public static <F extends BrowseFacet> Iterator<F> mergeFacets(Iterator<F>[] facetList,final Comparator<F> comparator){
		return null;
	}
	*/
}
