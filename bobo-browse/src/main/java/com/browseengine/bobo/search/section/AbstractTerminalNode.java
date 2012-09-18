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

package com.browseengine.bobo.search.section;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.search.DocIdSetIterator;

/**
 * An abstract class for terminal nodes of SectionSearchQueryPlan
 */
public abstract class AbstractTerminalNode extends SectionSearchQueryPlan
{
  protected TermPositions _tp;
  protected int _posLeft;
  protected int _curPos;
  
  public AbstractTerminalNode(Term term, IndexReader reader) throws IOException
  {
    super();
    _tp = reader.termPositions();
    _tp.seek(term);
    _posLeft = 0;
  }
  
  @Override
  public int fetchDoc(int targetDoc) throws IOException
  {
    if(targetDoc <= _curDoc) targetDoc = _curDoc + 1;
    
    if(_tp.skipTo(targetDoc))
    {
      _curDoc = _tp.doc();
      _posLeft = _tp.freq();
      _curSec = -1;
      _curPos = -1;
      return _curDoc;
    }
    else
    {
      _curDoc = DocIdSetIterator.NO_MORE_DOCS;
      _tp.close();
      return _curDoc;
    }
  }
  
  abstract public int fetchSec(int targetSec) throws IOException;
}
