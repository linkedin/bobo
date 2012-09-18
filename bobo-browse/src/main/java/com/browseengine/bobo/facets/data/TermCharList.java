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

package com.browseengine.bobo.facets.data;

import it.unimi.dsi.fastutil.chars.CharArrayList;

import java.util.Arrays;
import java.util.List;

public class TermCharList extends TermValueList<Character> {

  private char[] _elements = null;
	private static char parse(String s)
	{
		return s==null ? (char)0 : s.charAt(0);
	}
	
	public TermCharList() {
		super();
	}

	public TermCharList(int capacity) {
		super(capacity);
	}

	@Override
	public boolean add(String o) {
		return ((CharArrayList)_innerList).add(parse(o));
	}

	@Override
	protected List<?> buildPrimitiveList(int capacity) {
	  _type = Character.class;
		return  capacity>0 ? new CharArrayList(capacity) : new CharArrayList();
	}

  @Override
  public boolean containsWithType(Character val)
  {
    return Arrays.binarySearch(_elements, val)>=0;
  }

  public boolean containsWithType(char val)
  {
    return Arrays.binarySearch(_elements, val)>=0;
  }

  @Override
	public int indexOf(Object o)
  {
    char val;
    if (o instanceof String)
      val = parse((String)o);
    else
      val = (Character)o;
		char[] elements=((CharArrayList)_innerList).elements();
		return Arrays.binarySearch(elements, val);
	}

  @Override
  public int indexOfWithType(Character val)
  {
    return Arrays.binarySearch(_elements, val);
  }

  public int indexOfWithType(char val)
  {
    return Arrays.binarySearch(_elements, val);
  }

  @Override
	public void seal() {
		((CharArrayList)_innerList).trim();
		_elements = ((CharArrayList)_innerList).elements();
	}

	@Override
	public String format(Object o) {
		return String.valueOf(o);
	}
}
