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

/**
 * @author ymatsuda
 *
 */
public class BigIntBuffer
{
  private static final int PAGESIZE = 1024;
  private static final int MASK = 0x3FF;
  private static final int SHIFT = 10;

  private ArrayList<int[]> _buffer;
  private int _allocSize;
  private int _mark;

  public BigIntBuffer()
  {
    _buffer = new ArrayList<int[]>();
    _allocSize = 0;
    _mark = 0;
  }
  
  public int alloc(int size)
  {
    if(size > PAGESIZE) throw new IllegalArgumentException("size too big");
    
    if((_mark + size) > _allocSize)
    {
      int[] page = new int[PAGESIZE];
      _buffer.add(page);
      _allocSize += PAGESIZE;
    }
    int ptr = _mark;
    _mark += size;

    return ptr;
  }
  
  public void reset()
  {
    _mark = 0;
  }
  
  public void set(int ptr, int val)
  {
    int[] page = _buffer.get(ptr >> SHIFT);
    page[ptr & MASK] = val;
  }
  
  public int get(int ptr)
  {
    int[] page = _buffer.get(ptr >> SHIFT);
    return page[ptr & MASK];
  }
}
