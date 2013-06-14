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

package com.browseengine.bobo.test.solr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.solr.client.solrj.SolrQuery;

import com.browseengine.bobo.api.BrowseSelection.ValueOperation;
import com.browseengine.solr.BoboRequestBuilder;

public class BoboSolrTestCase extends TestCase {

	public void testFacetExpand() throws Exception{
		String fakeField = "fake";
		SolrQuery solrQ = new SolrQuery();
		
		BoboRequestBuilder.applyFacetExpand(solrQ, fakeField, true);
		assertTrue(BoboRequestBuilder.isFacetExpand(solrQ, fakeField));
		
		solrQ = new SolrQuery();
		BoboRequestBuilder.applyFacetExpand(solrQ, fakeField, false);
		assertFalse(BoboRequestBuilder.isFacetExpand(solrQ, fakeField));
	}
	
	public void testSelectionOperation() throws Exception{
		String fakeField = "fake";
		SolrQuery solrQ = new SolrQuery();
		
		ValueOperation op = ValueOperation.ValueOperationAnd;
		BoboRequestBuilder.applySelectionOperation(solrQ, fakeField, op);
		assertEquals(op,BoboRequestBuilder.getSelectionOperation(solrQ, fakeField));
		
		solrQ = new SolrQuery();
        op = ValueOperation.ValueOperationOr;
        BoboRequestBuilder.applySelectionOperation(solrQ, fakeField, op);
        assertEquals(op,BoboRequestBuilder.getSelectionOperation(solrQ, fakeField));
	}
	
	public void testSelectionNots() throws Exception{
		String fakeField = "fake";
		SolrQuery solrQ = new SolrQuery();
		
		HashSet<String> set = new HashSet<String>();
		set.add("s1");
		set.add("s2");
		
		BoboRequestBuilder.applySelectionNotValues(solrQ, fakeField, set.toArray(new String[set.size()]));
		
		String[] notVals = BoboRequestBuilder.getSelectionNotValues(solrQ, fakeField);
		
		if (notVals == null){
			fail("notvals should not be null");
		}
		int numContains = 0;
		for (String val : notVals){
			boolean contains = set.contains(val);
			if (!contains){
				fail(val+" does not exist in "+set);
			}
			else{
				numContains++;
			}
		}
		
		if (numContains != set.size()){
			fail(numContains+" != "+set.size());
		}
	}
	
	public void testSelectionProps() throws Exception{
		String fakeField = "fake";
		SolrQuery solrQ = new SolrQuery();
		
		HashMap<String,String> prop = new HashMap<String,String>();
		prop.put("p1","v1");
		prop.put("p2","v2");
		
		BoboRequestBuilder.applySelectionProperties(solrQ, fakeField, prop);
		
		Map<String,String> prop2= BoboRequestBuilder.getSelectionProperties(solrQ, fakeField);
		
		assertEquals(prop, prop2);
	}
}
