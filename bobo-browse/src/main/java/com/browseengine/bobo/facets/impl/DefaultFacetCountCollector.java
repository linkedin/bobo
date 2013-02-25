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

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.BrowseSelection;
import com.browseengine.bobo.api.ComparatorFactory;
import com.browseengine.bobo.api.FacetIterator;
import com.browseengine.bobo.api.FacetSpec;
import com.browseengine.bobo.api.FacetSpec.FacetSortSpec;
import com.browseengine.bobo.api.FieldValueAccessor;
import com.browseengine.bobo.facets.FacetCountCollector;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.TermDoubleList;
import com.browseengine.bobo.facets.data.TermFloatList;
import com.browseengine.bobo.facets.data.TermIntList;
import com.browseengine.bobo.facets.data.TermLongList;
import com.browseengine.bobo.facets.data.TermShortList;
import com.browseengine.bobo.facets.data.TermValueList;
import com.browseengine.bobo.util.BigSegmentedArray;
import com.browseengine.bobo.util.IntBoundedPriorityQueue;
import com.browseengine.bobo.util.IntBoundedPriorityQueue.IntComparator;
import com.browseengine.bobo.util.LazyBigIntArray;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class DefaultFacetCountCollector implements FacetCountCollector
{
  private static final Logger log = Logger.getLogger(DefaultFacetCountCollector.class.getName());

  public BigSegmentedArray _count;
  public int _countlength;

  protected final FacetSpec _ospec;
  protected final FacetDataCache _dataCache;
  protected final BrowseSelection _sel;
  protected final BigSegmentedArray _array;

  private final String _name;
  private boolean _closed = false;

  public DefaultFacetCountCollector(String name,FacetDataCache dataCache,int docBase,
      BrowseSelection sel,FacetSpec ospec)
  {
    _sel = sel;
    _ospec = ospec;
    _name = name;
    _dataCache=dataCache;
    _countlength = _dataCache.valArray.size();
    _count = new LazyBigIntArray(_countlength);
    _array = _dataCache.orderArray;
  }

  public String getName()
  {
    return _name;
  }

  abstract public void collect(int docid);

  abstract public void collectAll();

  public BrowseFacet getFacet(String value)
  {
    if (_closed)
    {
      throw new IllegalStateException("This instance of count collector for " + _name + " was already closed");
    }
    BrowseFacet facet = null;
    int index=_dataCache.valArray.indexOf(value);
    if (index >=0 ){
      facet = new BrowseFacet(_dataCache.valArray.get(index),_count.get(index));
    }
    else{
      facet = new BrowseFacet(_dataCache.valArray.format(value),0);  
    }
    return facet; 
  }

  public int getFacetHitsCount(Object value)
  {
    if (_closed)
    {
      throw new IllegalStateException("This instance of count collector for " + _name + " was already closed");
    }
    int index=_dataCache.valArray.indexOf(value);
    if (index >= 0)
    {
      return _count.get(index);
    }
    else{
      return 0;  
    }
  }

  public BigSegmentedArray getCountDistribution()
  {
    return _count;
  }
  
  public FacetDataCache getFacetDataCache(){
	  return _dataCache;
  }
  
  public static List<BrowseFacet> getFacets(FacetSpec ospec, BigSegmentedArray count, int countlength, final TermValueList<?> valList){
	  if (ospec!=null)
	    {
	      int minCount=ospec.getMinHitCount();
	      int max=ospec.getMaxCount();
	      if (max <= 0) max=countlength;

	      List<BrowseFacet> facetColl;
	      FacetSortSpec sortspec = ospec.getOrderBy();
	      if (sortspec == FacetSortSpec.OrderValueAsc)
	      {
	        facetColl=new ArrayList<BrowseFacet>(max);
	        for (int i = 1; i < countlength;++i) // exclude zero
	        {
	          int hits=count.get(i);
	          if (hits>=minCount)
	          {
	            BrowseFacet facet=new BrowseFacet(valList.get(i),hits);
	            facetColl.add(facet);
	          }
	          if (facetColl.size()>=max) break;
	        }
	      }
	      else //if (sortspec == FacetSortSpec.OrderHitsDesc)
	      {
	        ComparatorFactory comparatorFactory;
	        if (sortspec == FacetSortSpec.OrderHitsDesc){
	          comparatorFactory = new FacetHitcountComparatorFactory();
	        }
	        else{
	          comparatorFactory = ospec.getCustomComparatorFactory();
	        }

	        if (comparatorFactory == null){
	          throw new IllegalArgumentException("facet comparator factory not specified");
	        }

	        final IntComparator comparator = comparatorFactory.newComparator(new FieldValueAccessor(){

	          public String getFormatedValue(int index) {
	            return valList.get(index);
	          }

	          public Object getRawValue(int index) {
	            return valList.getRawValue(index);
	          }

	        }, count);
	        facetColl=new LinkedList<BrowseFacet>();
	        final int forbidden = -1;
	        IntBoundedPriorityQueue pq=new IntBoundedPriorityQueue(comparator,max, forbidden);

	        for (int i=1;i<countlength;++i)
	        {
	          int hits=count.get(i);
	          if (hits>=minCount)
	          {
	            pq.offer(i);
	          }
	        }

	        int val;
	        while((val = pq.pollInt()) != forbidden)
	        {
	          BrowseFacet facet=new BrowseFacet(valList.get(val),count.get(val));
	          ((LinkedList<BrowseFacet>)facetColl).addFirst(facet);
	        }
	      }
	      return facetColl;
	    }
	    else
	    {
	      return FacetCountCollector.EMPTY_FACET_LIST;
	    }
  }

  public List<BrowseFacet> getFacets() {
    if (_closed)
    {
      throw new IllegalStateException("This instance of count collector for " + _name + " was already closed");
    }
    
    return getFacets(_ospec,_count, _countlength, _dataCache.valArray);
    
  }

  @Override
  public void close()
  {
    if (_closed)
    {
      log.warn("This instance of count collector for '" + _name + "' was already closed. This operation is no-op.");
      return;
    }
    _closed = true;
  }

  /**
   * This function returns an Iterator to visit the facets in value order
   * @return	The Iterator to iterate over the facets in value order
   */
  public FacetIterator iterator()
  {
    if (_closed)
    {
      throw new IllegalStateException("This instance of count collector for '" + _name + "' was already closed");
    }
    if (_dataCache.valArray.getType().equals(Integer.class))
    {
      return new DefaultIntFacetIterator((TermIntList) _dataCache.valArray, _count, _countlength, false);
    } else if (_dataCache.valArray.getType().equals(Long.class))
    {
      return new DefaultLongFacetIterator((TermLongList) _dataCache.valArray, _count, _countlength, false);
    } else if (_dataCache.valArray.getType().equals(Short.class))
    {
      return new DefaultShortFacetIterator((TermShortList) _dataCache.valArray, _count, _countlength, false);
    } else if (_dataCache.valArray.getType().equals(Float.class))
    {
      return new DefaultFloatFacetIterator((TermFloatList) _dataCache.valArray, _count, _countlength, false);
    } else if (_dataCache.valArray.getType().equals(Double.class))
    {
      return new DefaultDoubleFacetIterator((TermDoubleList) _dataCache.valArray, _count, _countlength, false);
    } else
    return new DefaultFacetIterator(_dataCache.valArray, _count, _countlength, false);
  }
}
