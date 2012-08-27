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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Internal data are stored in a long[] with values generated from {@link Date#getTime()}
 */
public class TermDateList extends TermLongList {
	private ThreadLocal<SimpleDateFormat> _dateFormatter = null;
	
	public TermDateList(String formatString)
	{
		super();
		setFormatString(formatString);
	}
	
	public TermDateList(int capacity,String formatString)
	{
		super(capacity,formatString);
		setFormatString(formatString);
	}
	
	public String getFormatString()
	{
		return _formatString;
	}
	
	protected void setFormatString(final String formatString)
	{
		_formatString=formatString;
		_dateFormatter = new ThreadLocal<SimpleDateFormat>() {
		      protected SimpleDateFormat initialValue() {
		        if (formatString!=null){
		          return new SimpleDateFormat(formatString);
		        }
		        else{
		          return null;
		        }
		        
		      }   
		    };
	}
	
	@Override
	protected long parse(String o)
	{
		if (o==null || o.length() == 0)
		{
			return 0L;
		}
		else
		{
			try
			{
			  return _dateFormatter.get().parse(o).getTime();
			}
			catch(ParseException pe)
			{
				throw new RuntimeException(pe.getMessage(),pe);
			}
		}

	}

	@Override
	  public String get(int index)
	  {
		SimpleDateFormat formatter = _dateFormatter.get();
	    if (formatter == null)
	      return String.valueOf(_elements[index]);
	    return formatter.format(_elements[index]);
	  }
	  
	@Override
	public String format(Object o) {
		Long val;
		if (o instanceof String){
			val = parse((String)o);
		}
		else{
			val = (Long)o;
		}
		if (_formatter == null)
		{
			return String.valueOf(o);
		}
		else
		{
			SimpleDateFormat formatter=_dateFormatter.get();
			if (formatter==null) return String.valueOf(o);
			return _formatter.get().format(new Date(val.longValue()));
		}
	}

}
