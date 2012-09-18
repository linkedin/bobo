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

import java.util.Comparator;
import java.util.PriorityQueue;

public class BoundedPriorityQueue<E> extends PriorityQueue<E>
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private final int _maxSize;
  public BoundedPriorityQueue(int maxSize)
  {
    super();
    _maxSize=maxSize;
  }

  public BoundedPriorityQueue(Comparator<? super E> comparator,int maxSize)
  {
    super(maxSize, comparator);
    _maxSize=maxSize;
  }

  @Override
  public boolean offer(E o)
  {
    int size=size();
    if (size<_maxSize)
    {
      return super.offer(o);
    }
    else
    {
      E smallest=super.peek();
      Comparator<? super E> comparator = super.comparator();
      boolean madeIt=false;
      if (comparator == null)
      {
        if (((Comparable<E>)smallest).compareTo(o) < 0)
        {
          madeIt=true;
        }
      }
      else
      {
        if (comparator.compare(smallest, o) < 0)
        {
          madeIt=true;
        }
      }
      
      if (madeIt)
      {
        super.poll();
        return super.offer(o);
      }
      else
      {
        return false;
      }
    }
  }
}
