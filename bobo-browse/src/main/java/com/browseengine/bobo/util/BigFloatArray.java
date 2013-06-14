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

public class BigFloatArray {

	  private static final long serialVersionUID = 1L;
		
	  private float[][] _array;
	  private int _numrows;
	  /* Remember that 2^SHIFT_SIZE = BLOCK_SIZE */
	  final private static int BLOCK_SIZE = 1024;
	  final private static int SHIFT_SIZE = 10; 
	  final private static int MASK = BLOCK_SIZE -1;
	  
	  public BigFloatArray(int size)
	  {
	    _numrows = size >> SHIFT_SIZE;
	    _array = new float[_numrows+1][];
	    for (int i = 0; i <= _numrows; i++)
	    {
	      _array[i]=new float[BLOCK_SIZE];
	    }
	  }
	  
	  public void add(int docId, float val)
	  {
	    _array[docId >> SHIFT_SIZE][docId & MASK] = val;
	  }
	  
	  public float get(int docId)
	  {
	    return _array[docId >> SHIFT_SIZE][docId & MASK];
	  }
	  
	  public int capacity()
	  {
	    return _numrows * BLOCK_SIZE;
	  }
	  
	  public void ensureCapacity(int size)
	  {
	    int newNumrows = (size >> SHIFT_SIZE) + 1;
	    if (newNumrows > _array.length)
	    {
	      float[][] newArray = new float[newNumrows][];           // grow
	      System.arraycopy(_array, 0, newArray, 0, _array.length);
	      for (int i = _array.length; i < newNumrows; ++i)
	      {
	        newArray[i] = new float[BLOCK_SIZE];
	      }
	      _array = newArray;
	    }
	    _numrows = newNumrows;
	  }
}
