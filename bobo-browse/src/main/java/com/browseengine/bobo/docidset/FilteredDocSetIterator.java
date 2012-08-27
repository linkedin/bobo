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

package com.browseengine.bobo.docidset;

import java.io.IOException;

import org.apache.lucene.search.DocIdSetIterator;


public abstract class FilteredDocSetIterator extends DocIdSetIterator {
	protected DocIdSetIterator _innerIter;
	private int _currentDoc;
	
	public FilteredDocSetIterator(DocIdSetIterator innerIter)
	{
		if (innerIter == null)
		{
			throw new IllegalArgumentException("null iterator");
		}
		_innerIter=innerIter;
		_currentDoc=-1;
	}
	
	abstract protected boolean match(int doc);
	
	public final int docID() {
		return _currentDoc;
	}

	public final int nextDoc() throws IOException{
		int docid = _innerIter.nextDoc();
		while(docid!=DocIdSetIterator.NO_MORE_DOCS)
		{
			if (match(docid))
			{
				_currentDoc=docid;
				return docid;
			}
			else{
				docid = _innerIter.nextDoc();
			}
		}
		return DocIdSetIterator.NO_MORE_DOCS;
	}

	public final int advance(int n) throws IOException{
		int docid =_innerIter.advance(n);
		while (docid!=DocIdSetIterator.NO_MORE_DOCS)
		{
			if (match(docid))
			{
			  _currentDoc=docid;
			  return docid;
			}
			else
			{
			  docid=_innerIter.nextDoc();
			}
		}
		return DocIdSetIterator.NO_MORE_DOCS;
	}

}
