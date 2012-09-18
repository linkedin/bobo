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

package com.browseengine.bobo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.lucene.util.PriorityQueue;

public class SearchResultMerger<T>
{
  private SearchResultMerger()
  {
    
  }
  
  public static class MergedIterator<T> implements Iterator<T>
  {
    private class IteratorCtx
    {
      public Iterator<T> _iterator;
      public T _curVal;
      
      public IteratorCtx(Iterator<T> iterator)
      {
        _iterator = iterator;
        _curVal = null;
      }
        
      public boolean fetch()
      {
        if(_iterator.hasNext())
        {
          _curVal = _iterator.next();
          return true;
        }
        _curVal = null;
        return false;
      }
    }

    private final PriorityQueue _queue;

    public MergedIterator(final List<Iterator<T>> sources, final Comparator<T> comparator)
    {
      _queue = new PriorityQueue()
      {
        {
          this.initialize(sources.size());
        }
      
        @SuppressWarnings("unchecked")
        @Override
        protected boolean lessThan(Object o1, Object o2)
        {
          T v1 = ((IteratorCtx)o1)._curVal;
          T v2 = ((IteratorCtx)o2)._curVal;
          
          return (comparator.compare(v1, v2) < 0);
        }
      };
    
      for(Iterator<T> iterator : sources)
      {
        IteratorCtx ctx = new IteratorCtx(iterator);
        if(ctx.fetch()) _queue.add(ctx);
      }
    }

    public boolean hasNext()
    {
      return _queue.size() > 0;
    }

    @SuppressWarnings("unchecked")
    public T next()
    {
      IteratorCtx ctx = (IteratorCtx)_queue.top();
      T val = ctx._curVal;
      if (ctx.fetch())
      {
        _queue.updateTop();
      }
      else
      {
        _queue.pop();
      }
      return val;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public static <T> Iterator<T> mergeIterator(List<Iterator<T>> results, Comparator<T> comparator)
  {
    return new MergedIterator<T>(results, comparator);
  }

  public static <T> ArrayList<T> mergeResult(int offset,int count,List<Iterator<T>> results,Comparator<T> comparator)
  {
    Iterator<T> mergedIter=mergeIterator(results, comparator);
    
    for (int c = 0; c < offset && mergedIter.hasNext(); c++)
    {
      mergedIter.next();
    }
    
    ArrayList<T> mergedList=new ArrayList<T>();
    
    for (int c = 0; c < count && mergedIter.hasNext(); c++)
    {
      mergedList.add(mergedIter.next());
    }
    
    return mergedList;
  }
}
