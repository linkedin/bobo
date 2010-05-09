/**
 * 
 */
package com.browseengine.bobo.facets.impl;

import java.util.NoSuchElementException;
import com.browseengine.bobo.facets.data.TermLongList;
import com.browseengine.bobo.api.LongFacetIterator;

/**
 * @author "Xiaoyang Gu<xgu@linkedin.com>"
 * 
 */
public class DefaultLongFacetIterator extends LongFacetIterator
{

  public TermLongList _valList;
  private int[] _count;
  private int _countLengthMinusOne;
  private int _index;

  public DefaultLongFacetIterator(TermLongList valList, int[] countarray,
      boolean zeroBased)
  {
    _valList = valList;
    _count = countarray;
    _countLengthMinusOne = _count.length - 1;
    _index = -1;
    if (!zeroBased)
      _index++;
    facet = -1;
    count = 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#getFacet()
   */
  public String getFacet()
  {
    return _valList.format(facet);
  }

  public String format(long val)
  {
    return _valList.format(val);
  }

  public String format(Object val)
  {
    return _valList.format(val);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#getFacetCount()
   */
  public int getFacetCount()
  {
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#hasNext()
   */
  public boolean hasNext()
  {
    return (_index < _countLengthMinusOne);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#next()
   */
  public String next()
  {
    if ((_index >= 0) && (_index >= _countLengthMinusOne))
      throw new NoSuchElementException("No more facets in this iteration");
    _index++;
    facet = _valList.getPrimitiveValue(_index);
    count = _count[_index];
    return _valList.get(_index);
  }

  /* (non-Javadoc)
   * @see com.browseengine.bobo.api.LongFacetIterator#nextLong()
   */
  public long nextLong()
  {
    if (_index >= _countLengthMinusOne)
      throw new NoSuchElementException("No more facets in this iteration");
    _index++;
    facet = _valList.getPrimitiveValue(_index);
    count = _count[_index];
    return facet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#remove()
   */
  public void remove()
  {
    throw new UnsupportedOperationException(
        "remove() method not supported for Facet Iterators");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#next(int)
   */
  public String next(int minHits)
  {
    while (++_index < _count.length)
    {
      if (_count[_index] >= minHits)
      {
        facet = _valList.getPrimitiveValue(_index);
        count = _count[_index];
        return _valList.format(facet);
      }
    }
    facet = -1;
    count = 0;
    return format(facet);
  }

  /* (non-Javadoc)
   * @see com.browseengine.bobo.api.LongFacetIterator#nextLong(int)
   */
  public long nextLong(int minHits)
  {
    while (++_index < _count.length)
    {
      if (_count[_index] >= minHits)
      {
        facet = _valList.getPrimitiveValue(_index);
        count = _count[_index];
        return facet;
      }
    }
    facet = -1;
    count = 0;
    return facet;
  }
}