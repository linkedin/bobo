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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;

public class LuceneCustomDocComparatorSource extends DocComparatorSource {
	private final FieldComparator<Comparable> _luceneComparator;
	private final String _fieldname;
	public LuceneCustomDocComparatorSource(String fieldname,FieldComparator<Comparable> luceneComparator){
		_fieldname = fieldname;
		_luceneComparator = luceneComparator;
	}
	
	@Override
	public DocComparator getComparator(IndexReader reader, int docbase)
			throws IOException {
		_luceneComparator.setNextReader(reader, docbase);
		return new DocComparator() {
			
			@Override
			public Comparable value(ScoreDoc doc) {
				return _luceneComparator.value(doc.doc);
			}
			
			@Override
			public int compare(ScoreDoc doc1, ScoreDoc doc2) {
				return _luceneComparator.compare(doc1.doc, doc2.doc);
			}

			@Override
			public DocComparator setScorer(Scorer scorer) {
				_luceneComparator.setScorer(scorer);
        return this;
			}
		};
	}

}
