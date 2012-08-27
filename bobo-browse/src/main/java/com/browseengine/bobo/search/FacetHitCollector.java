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

package com.browseengine.bobo.search;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.search.DocIdSetIterator;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.docidset.RandomAccessDocIdSet;
import com.browseengine.bobo.facets.FacetCountCollector;
import com.browseengine.bobo.facets.FacetCountCollectorSource;
import com.browseengine.bobo.facets.FacetHandler;
import com.browseengine.bobo.facets.filter.RandomAccessFilter;


public final class FacetHitCollector{
	
	public FacetCountCollectorSource _facetCountCollectorSource;	
	public FacetCountCollectorSource _collectAllSource = null;
	public FacetHandler<?> facetHandler;
	public RandomAccessFilter _filter;
	public final CurrentPointers _currentPointers = new CurrentPointers();
	public LinkedList<FacetCountCollector> _countCollectorList = new LinkedList<FacetCountCollector>();
	public LinkedList<FacetCountCollector> _collectAllCollectorList = new LinkedList<FacetCountCollector>();
	
	public void setNextReader(BoboIndexReader reader,int docBase) throws IOException{
		if (_collectAllSource!=null){
			FacetCountCollector collector = _collectAllSource.getFacetCountCollector(reader, docBase);
			_collectAllCollectorList.add(collector);
			collector.collectAll();
		}
		else{
		  if (_filter!=null){
			_currentPointers.docidSet = _filter.getRandomAccessDocIdSet(reader);
			_currentPointers.postDocIDSetIterator = _currentPointers.docidSet.iterator();
			_currentPointers.doc = _currentPointers.postDocIDSetIterator.nextDoc();
		  }
		  if (_facetCountCollectorSource!=null){
		    _currentPointers.facetCountCollector = _facetCountCollectorSource.getFacetCountCollector(reader, docBase);
		    _countCollectorList.add(_currentPointers.facetCountCollector);
		  }
		}
	}
	
	public static class CurrentPointers{
		public RandomAccessDocIdSet docidSet=null;
		public DocIdSetIterator postDocIDSetIterator = null;
		public int doc;
		public FacetCountCollector facetCountCollector;
	}
}
