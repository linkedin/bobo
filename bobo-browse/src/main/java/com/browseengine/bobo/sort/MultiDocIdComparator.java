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

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;


public class MultiDocIdComparator extends DocComparator {
	private final DocComparator[] _comparators;
	
	public MultiDocIdComparator(DocComparator[] comparators){
		_comparators = comparators;
	}
	
	public int compare(ScoreDoc doc1, ScoreDoc doc2) {
		for (int i=0;i<_comparators.length;++i){
			int v=_comparators[i].compare(doc1, doc2);
			if (v!=0) return v;
		}
		return 0;
	}

	public MultiDocIdComparator setScorer(Scorer scorer){
	  for (DocComparator comparator : _comparators){
	    comparator.setScorer(scorer);
	  }
    return this;
	}
	
	@Override
	public Comparable value(ScoreDoc doc) {
		return new MultiDocIdComparable(doc, _comparators);
	}
	
	public static class MultiDocIdComparable implements Comparable
	{
	  private ScoreDoc _doc;
	  private DocComparator[] _comparators;

	  public MultiDocIdComparable(ScoreDoc doc, DocComparator[] comparators)
	  {
	    _doc = doc;
	    _comparators = comparators;
	  }
	  
	  public int compareTo(Object o)
      {
	    MultiDocIdComparable other = (MultiDocIdComparable)o;
        Comparable c1,c2;
        for (int i=0;i<_comparators.length;++i){
            c1 = _comparators[i].value(_doc);
            c2 = other._comparators[i].value(other._doc);
            int v = c1.compareTo(c2);
            if (v!=0) {
                return v;
            }
        }
        return 0;
      }
	}
}
