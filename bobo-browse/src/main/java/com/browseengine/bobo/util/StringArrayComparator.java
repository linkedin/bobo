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

import java.util.Arrays;


public class StringArrayComparator implements Comparable<StringArrayComparator> {
	private String[] vals;
	public StringArrayComparator(String[] vals){
		this.vals = vals;
	}
	public int compareTo(StringArrayComparator node) {
		String[] o = node.vals;
		if (vals==o){
			return 0;
		}
		if (vals == null){
			return -1;
		}
		if (o == null){
			return 1;
		}
		for (int i = 0;i < vals.length; ++i){
			if (i>=o.length){
				return 1;
			}
			int compVal = vals[i].compareTo(o[i]);
			if (vals[i].startsWith("-") && o[i].startsWith("-") ) {
			  compVal *= -1;
			}
			if (compVal!=0) return compVal;
		}
		if (vals.length == o.length) return 0;
		return -1;
	}
	
	@Override
	public String toString(){
		return Arrays.toString(vals);
	}

}
