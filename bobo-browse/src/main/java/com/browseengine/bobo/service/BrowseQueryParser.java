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

import org.apache.lucene.search.DocIdSet;

/**
 * Builds a DocSet from an array of SelectioNodes
 */
public interface BrowseQueryParser {
	public static class SelectionNode
	{
		private String fieldName;
		private DocIdSet docSet;
		
		public SelectionNode()
		{	
		}
		
		public SelectionNode(String fieldName,DocIdSet docSet)
		{
			this.fieldName=fieldName;
			this.docSet=docSet;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public DocIdSet getDocSet() {
			return docSet;
		}
		public void setDocSet(DocIdSet docSet) {
			this.docSet = docSet;
		}		
	}
	
	DocIdSet parse(SelectionNode[] selectionNodes,SelectionNode[] notSelectionNodes,int maxDoc);
}
