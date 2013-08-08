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

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.Weight;

import com.browseengine.bobo.facets.FacetHandler;
import com.browseengine.bobo.sort.SortCollector;

public interface Browsable extends Searchable
{
	
	void browse(BrowseRequest req, 
	            Collector hitCollector,
	            Map<String,FacetAccessible> facets) throws BrowseException;

	void browse(BrowseRequest req, 
	            Collector hitCollector,
	            Map<String,FacetAccessible> facets,
	            int start) throws BrowseException;

	void browse(BrowseRequest req, 
	            Weight weight,
	            Collector hitCollector,
	            Map<String,FacetAccessible> facets,
	            int start) throws BrowseException;


	BrowseResult browse(BrowseRequest req) throws BrowseException;

	Set<String> getFacetNames();
	
	void setFacetHandler(FacetHandler<?> facetHandler) throws IOException;

	FacetHandler<?> getFacetHandler(String name);
	
	Map<String,FacetHandler<?>> getFacetHandlerMap();

	Similarity getSimilarity();
	
	void setSimilarity(Similarity similarity);
	
	String[] getFieldVal(int docid,String fieldname) throws IOException;
	
	Object[] getRawFieldVal(int docid,String fieldname) throws IOException;
	
	int numDocs();
	
	SortCollector getSortCollector(SortField[] sort,Query q,int offset,int count,boolean fetchStoredFields,Set<String> termVectorsToFetch,boolean forceScoring, String[] groupBy, int maxPerGroup, boolean collectDocIdCache, Set<String> facetsToFetch);
	
	Explanation explain(Query q, int docid) throws IOException;
}
