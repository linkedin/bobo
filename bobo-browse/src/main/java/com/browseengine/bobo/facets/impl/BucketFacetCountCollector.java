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


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.util.BitVector;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.FacetIterator;
import com.browseengine.bobo.api.FacetSpec;
import com.browseengine.bobo.facets.FacetCountCollector;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.TermStringList;
import com.browseengine.bobo.facets.data.TermValueList;
import com.browseengine.bobo.util.BigSegmentedArray;
import com.browseengine.bobo.util.LazyBigIntArray;

public class BucketFacetCountCollector implements FacetCountCollector
{
  private final String _name;
  private final DefaultFacetCountCollector _subCollector;
  private final FacetSpec _ospec;
  private final Map<String,String[]> _predefinedBuckets;
  private BigSegmentedArray _collapsedCounts;
  private TermStringList _bucketValues;
  private final int _numdocs;
  
  protected BucketFacetCountCollector(String name,  DefaultFacetCountCollector subCollector, FacetSpec ospec,Map<String,String[]> predefinedBuckets,int numdocs)
  {
    _name = name;
    _subCollector = subCollector;
    _ospec=ospec;
    _numdocs = numdocs;
    
    _predefinedBuckets = predefinedBuckets;
    _collapsedCounts = null;
    
    _bucketValues = new TermStringList();
    _bucketValues.add("");
    
    String[] bucketArray = _predefinedBuckets.keySet().toArray(new String[0]);
    Arrays.sort(bucketArray);
    for (String bucket : bucketArray){
    	_bucketValues.add(bucket);
    }
    _bucketValues.seal();
  }
  
  private BigSegmentedArray getCollapsedCounts(){
	if (_collapsedCounts==null){
		_collapsedCounts = new LazyBigIntArray(_bucketValues.size());
		FacetDataCache dataCache = _subCollector._dataCache;
		TermValueList<?> subList = dataCache.valArray; 
		BigSegmentedArray subcounts = _subCollector._count;
		BitVector indexSet = new BitVector(subcounts.size());
		int c = 0;
		int i = 0;
		for (String val : _bucketValues){
			if (val.length()>0){
				String[] subVals = _predefinedBuckets.get(val);
				int count = 0;
				for (String subVal : subVals){
					int index = subList.indexOf(subVal);
					if (index>0){
						int subcount = subcounts.get(index);
						count+=subcount;
						if (!indexSet.get(index)){
							indexSet.set(index);
							c+=dataCache.freqs[index];
						}
					}
				}
				_collapsedCounts.add(i, count);
			}
			i++;
		}
		_collapsedCounts.add(0, (_numdocs-c));
	}
	return _collapsedCounts;
  }
  
 // get the total count of all possible elements 
  public BigSegmentedArray getCountDistribution()
  {
    return getCollapsedCounts();
  }
  
  public String getName()
  {
      return _name;
  }
  
  // get the facet of one particular bucket
  public BrowseFacet getFacet(String bucketValue)
  {
      int index = _bucketValues.indexOf(bucketValue);
      if (index<0){
    	  return new BrowseFacet(bucketValue,0);
      }
      
      BigSegmentedArray counts = getCollapsedCounts();
    
      return new BrowseFacet(bucketValue,counts.get(index));
  }
  
  public int getFacetHitsCount(Object value) 
  {
    int index = _bucketValues.indexOf(value);
    if (index<0){
      return 0;
    }
    
    BigSegmentedArray counts = getCollapsedCounts();
  
    return counts.get(index);
  }

  public final void collect(int docid) {
	  _subCollector.collect(docid);
  }
  
  public final void collectAll()
  {
	  _subCollector.collectAll();
  }
  
  // get facets for all predefined buckets
  public List<BrowseFacet> getFacets() 
  {

	BigSegmentedArray counts = getCollapsedCounts();
    return DefaultFacetCountCollector.getFacets(_ospec, counts, counts.size(), _bucketValues);

  }
  
  
  public void close()
  {
	  _subCollector.close();
  }    

  public FacetIterator iterator() 
  {
	BigSegmentedArray counts = getCollapsedCounts();
	return new DefaultFacetIterator(_bucketValues, counts, counts.size(), true);
  }  
}

