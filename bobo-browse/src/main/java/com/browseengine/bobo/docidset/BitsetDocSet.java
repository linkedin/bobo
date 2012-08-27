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
import java.util.BitSet;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;

public class BitsetDocSet extends DocIdSet {
	private final BitSet _bs;
	public BitsetDocSet() {
		_bs=new BitSet();
	}
	
	public BitsetDocSet(int nbits) {
		_bs=new BitSet(nbits);
	}

	public void addDoc(int docid) {
		_bs.set(docid);
	}

	public int size() {
		return _bs.cardinality();
	}

	@Override
	public DocIdSetIterator iterator() {
		return new BitsDocIdSetIterator(_bs);
	}

	private static class BitsDocIdSetIterator extends DocIdSetIterator
	{
		private final BitSet _bs;
		private int _current;
		BitsDocIdSetIterator(BitSet bs)
		{
			_bs=bs;
			_current=-1;
		}
		
		@Override
		public int docID() {
			return _current;
		}

		@Override
		public int nextDoc() throws IOException {
			return _bs.nextSetBit(_current+1);
		}

		@Override
		public int advance(int target) throws IOException {
			return _bs.nextSetBit(target);
		}
		
	}
}
