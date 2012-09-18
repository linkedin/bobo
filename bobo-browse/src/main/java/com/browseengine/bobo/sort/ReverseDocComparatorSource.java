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

package com.browseengine.bobo.sort;

import java.io.IOException;
import java.io.Serializable;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;

public class ReverseDocComparatorSource extends DocComparatorSource {
	private final DocComparatorSource _inner;
	public ReverseDocComparatorSource(DocComparatorSource inner) {
		_inner = inner;
	}

	@Override
	public DocComparator getComparator(IndexReader reader, int docbase)
			throws IOException {
		return new ReverseDocComparator(_inner.getComparator(reader, docbase));
	}
	
	public static class ReverseDocComparator extends DocComparator{
		private final DocComparator _comparator;
		public ReverseDocComparator(DocComparator comparator){
			_comparator = comparator;
		}
		
		@Override
		public int compare(ScoreDoc doc1, ScoreDoc doc2) {
			return -_comparator.compare(doc1, doc2);
		}

		@Override
		public Comparable value(final ScoreDoc doc) {
			return new ReverseComparable(_comparator.value(doc));
			
		}
		
		public static class ReverseComparable implements Comparable,Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			final Comparable _inner;
			
			ReverseComparable(Comparable inner){
				_inner = inner;
			}
			
			public int compareTo(Object o) {
				if (o instanceof ReverseComparable){
					Comparable inner = ((ReverseComparable)o)._inner;						
					return -_inner.compareTo(inner);
				}
				else{
					throw new IllegalStateException("expected instanace of "+ReverseComparable.class);
				}
			}
			
			@Override
			public String toString(){
				StringBuilder buf = new StringBuilder();
				buf.append("!").append(_inner);
				return buf.toString();
			}
		}
		
	}

}
