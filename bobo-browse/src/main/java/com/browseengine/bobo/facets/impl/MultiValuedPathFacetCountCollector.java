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

import com.browseengine.bobo.api.BrowseSelection;
import com.browseengine.bobo.api.FacetSpec;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.MultiValueFacetDataCache;
import com.browseengine.bobo.util.BigIntArray;
import com.browseengine.bobo.util.BigNestedIntArray;

public class MultiValuedPathFacetCountCollector extends PathFacetCountCollector {

    private final BigNestedIntArray _array;
    
	public MultiValuedPathFacetCountCollector(String name, String sep,
			BrowseSelection sel, FacetSpec ospec, FacetDataCache dataCache) {
		super(name, sep, sel, ospec, dataCache);
		_array = ((MultiValueFacetDataCache)(dataCache))._nestedArray;
	}

	@Override
    public final void collect(int docid) 
    {
      _array.countNoReturn(docid, _count);
    }

    @Override
    public final void collectAll()
    {
      _count = BigIntArray.fromArray(_dataCache.freqs);
    }
}
