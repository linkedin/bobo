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

package com.browseengine.bobo.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.search.DocIdSet;

import com.kamikaze.docidset.impl.AndDocIdSet;
import com.kamikaze.docidset.impl.NotDocIdSet;
import com.kamikaze.docidset.impl.OrDocIdSet;

public class DefaultBrowseQueryParser implements BrowseQueryParser {

	public DocIdSet parse(SelectionNode[] selectionNodes,SelectionNode[] notSelectionNodes,int maxDoc) {
		DocIdSet docSet=null;
		DocIdSet selSet=null;
		
		if (selectionNodes!=null && selectionNodes.length>0)
		{
			ArrayList<DocIdSet> selSetList=new ArrayList<DocIdSet>(selectionNodes.length);
			for (SelectionNode selectionNode : selectionNodes)
			{				
				DocIdSet ds=selectionNode.getDocSet();
				
				if (ds!=null)
				{
					selSetList.add(ds);
				}
			}
			
			if (selSetList.size()>0)
			{
				if (selSetList.size()==1)
				{
					selSet=selSetList.get(0);
				}
				else
				{
					selSet=new AndDocIdSet(selSetList);
				}
			}
		}
			
		DocIdSet notSelSet=null;
		
		if (notSelectionNodes!=null && notSelectionNodes.length > 0)
		{
			ArrayList<DocIdSet> notSelSetList=new ArrayList<DocIdSet>(notSelectionNodes.length);
			for (SelectionNode selectionNode : notSelectionNodes)
			{
				DocIdSet ds=selectionNode.getDocSet();
				
				if (ds!=null)
				{
					notSelSetList.add(ds);
				}
				
				if (notSelSetList.size()>0)
				{
					if (notSelSetList.size()==1)
					{
						notSelSet=notSelSetList.get(0);
					}
					else
					{
						notSelSet=new OrDocIdSet(notSelSetList);
					}
				}	
			}
		}
		
		if (notSelSet!=null)
		{
			notSelSet=new NotDocIdSet(notSelSet,maxDoc);
		}
		
		if (selSet!=null && notSelSet!=null)
		{
			DocIdSet[] sets=new DocIdSet[]{selSet,notSelSet};
			docSet=new AndDocIdSet(Arrays.asList(sets));
		}
		else if (selSet!=null)
		{
			docSet=selSet;
		}
		else if (notSelSet!=null)
		{
			docSet=notSelSet;
		}
		
		
		return docSet;
	}
}
