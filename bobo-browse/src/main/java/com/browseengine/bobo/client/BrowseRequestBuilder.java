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

package com.browseengine.bobo.client;

import org.apache.lucene.search.SortField;

import com.browseengine.bobo.api.BrowseRequest;
import com.browseengine.bobo.api.BrowseSelection;
import com.browseengine.bobo.api.FacetSpec;
import com.browseengine.bobo.api.FacetSpec.FacetSortSpec;

public class BrowseRequestBuilder {
	private BrowseRequest _req;
	private String _qString;
	public BrowseRequestBuilder(){
		clear();
	}
	
	public BrowseRequestBuilder addSelection(String name, String val, boolean isNot){
		BrowseSelection sel = _req.getSelection(name);
		if (sel==null){
			sel = new BrowseSelection(name);
		}
		if (isNot){
			sel.addNotValue(val);
		}
		else{
			sel.addValue(val);
		}
		_req.addSelection(sel);
    return this;
	}
	
	public BrowseRequestBuilder clearSelection(String name){
		_req.removeSelection(name);
    return this;
	}
	
	public BrowseRequestBuilder applyFacetSpec(String name, int minHitCount, int maxCount, boolean expand, FacetSortSpec orderBy){
		FacetSpec fspec = new FacetSpec();
		fspec.setMinHitCount(minHitCount);
		fspec.setMaxCount(maxCount);
		fspec.setExpandSelection(expand);
		fspec.setOrderBy(orderBy);
		_req.setFacetSpec(name, fspec);
    return this;
	}
	
	public BrowseRequestBuilder applySort(SortField[] sorts){
		if (sorts==null){
			_req.clearSort();
		}
		else{
			_req.setSort(sorts);
		}
    return this;
	}
	
	public BrowseRequestBuilder clearFacetSpecs(){
		_req.getFacetSpecs().clear();
    return this;
	}
	public BrowseRequestBuilder clearFacetSpec(String name){
		_req.getFacetSpecs().remove(name);
    return this;
	}
	
	public BrowseRequestBuilder setOffset(int offset){
		_req.setOffset(offset);
    return this;
	}
	
	public BrowseRequestBuilder setCount(int count){
		_req.setCount(count);
    return this;
	}
	
	public BrowseRequestBuilder setQuery(String qString){
		_qString = qString;
    return this;
	}
	
	public BrowseRequestBuilder clear(){
		_req = new BrowseRequest();
		_req.setOffset(0);
		_req.setCount(5);
		_req.setFetchStoredFields(true);
		_qString = null;
    return this;
	}
	
	public BrowseRequestBuilder clearSelections(){
		_req.clearSelections();
    return this;
	}
	
	public BrowseRequest getRequest(){
		return _req;
	}
	
	public String getQueryString(){
		return _qString;
	}
}
