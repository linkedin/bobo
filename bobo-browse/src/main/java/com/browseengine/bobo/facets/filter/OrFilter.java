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

package com.browseengine.bobo.facets.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;

import com.kamikaze.docidset.impl.OrDocIdSet;

public class OrFilter extends Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final List<? extends Filter> _filters;
	
	public OrFilter(List<? extends Filter> filters)
	{
		_filters = filters;
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
	  if(_filters.size() == 1)
	  {
	    return _filters.get(0).getDocIdSet(reader);
	  }
	  else
	  {
	    List<DocIdSet> list = new ArrayList<DocIdSet>(_filters.size());
	    for (Filter f : _filters)
	    {
	      list.add(f.getDocIdSet(reader));
	    }
	    return new OrDocIdSet(list);
	  }
	}
}

