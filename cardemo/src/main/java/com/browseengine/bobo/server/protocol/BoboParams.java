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

package com.browseengine.bobo.server.protocol;

import java.util.Iterator;

public abstract class BoboParams {
	abstract public String get(String name);
	abstract public String[] getStrings(String name);
	abstract public Iterator<String> getParamNames();
	 
	public String getString(String name,boolean required){
		String retVal=get(name);
		if (retVal==null && required){
			throw new IllegalArgumentException("parameter "+name+" does not exist");
		}
		return retVal;
	}
	public boolean getBool(String name,boolean defaultVal){
		String retVal=getString(name,false);
		if (retVal!=null){
			return Boolean.parseBoolean(retVal);
		}
		else{
			return defaultVal;
		}
	}
	
	public String getString(String name){
		return getString(name,false);
	}
	
	public String getString(String name,String defaultVal){
		String retVal=get(name);
		if (retVal==null) return defaultVal;
		return retVal;
	}
	
	public int getInt(String name,boolean required){
		String retVal=getString(name,required);
		return Integer.parseInt(retVal);
	}
	
	public int getInt(String name,int defaultVal){
		String retVal=getString(name,false);
		try{
			return Integer.parseInt(retVal);
		}
		catch(Exception e){
			return defaultVal;
		}
	}
}
