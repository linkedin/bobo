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

package com.browseengine.bobo.solr.test;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.FacetParams;

import com.browseengine.solr.BoboRequestBuilder;


public class SolrBoboTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String url = "http://localhost:8983/solr";
		SolrServer solrSvr = new CommonsHttpSolrServer(url);
		SolrQuery query = new SolrQuery();
		
		query.setQuery("van");
		query.setFacet(true);
		query.addFacetField("color","category");
		query.setFacetMinCount(1);
		query.setFields("color,score,category");
		query.setStart(0);
		query.setRows(10);
		query.setFilterQueries("cool");
		query.set(FacetParams.FACET_QUERY, "color:red");
		BoboRequestBuilder.applyFacetExpand(query, "color", true);
		
		QueryResponse res = solrSvr.query(query);
		
		SolrDocumentList results = res.getResults();
		long numFound = results.getNumFound();
		System.out.println("num hits: "+numFound);
		for (SolrDocument doc : results){
			System.out.println(doc);
		}
		
		List<FacetField> facetFieldList = res.getFacetFields();
		for (FacetField ff : facetFieldList){
			System.out.println(ff.getName()+":");
			List<Count> vals = ff.getValues();
			if (vals!=null){
			  for (Count val : vals){
				System.out.println(val.getName()+"("+val.getCount()+")");
			  }
			}
		}
	}

}
