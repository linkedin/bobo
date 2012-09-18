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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.facets.FacetHandler;

public class FacetHandlerLoader {
	
	private FacetHandlerLoader()
	{
		
	}
	public static void load(Collection<FacetHandler> tobeLoaded)
	{
		load(tobeLoaded,null);
	}
	
	public static void load(Collection<FacetHandler> tobeLoaded,Map<String,FacetHandler> preloaded)
	{
		
	}
	
	private static void load(BoboIndexReader reader,Collection<FacetHandler> tobeLoaded,Map<String,FacetHandler> preloaded,Set<String> visited) throws IOException
	{
		Map<String,FacetHandler> loaded = new HashMap<String,FacetHandler>();
		if (preloaded!=null)
		{
			loaded.putAll(preloaded);
		}
		
		Iterator<FacetHandler> iter = tobeLoaded.iterator();
		
		while(iter.hasNext())
		{
			FacetHandler handler = iter.next();
			if (!loaded.containsKey(handler.getName()))
			{
			  Set<String> depends = handler.getDependsOn();
			  if (depends.size() > 0)
			  {
			  }
			  handler.load(reader);
			}
		}
	}
}
