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

import org.apache.lucene.search.DocIdSetIterator;

/**
 * OR operator node for SectionSearchQueryPlan
 */
public class OrNode extends SectionSearchQueryPlan
{
  private NodeQueue _pq;
  
  protected OrNode() { }
  
  public OrNode(SectionSearchQueryPlan[] subqueries)
  {
    if(subqueries.length == 0)
    {
      _curDoc = DocIdSetIterator.NO_MORE_DOCS;
    }
    else
    {
      _pq = new NodeQueue(subqueries.length);
      for(SectionSearchQueryPlan q : subqueries)
      {
        if(q != null) _pq.add(q);
      }
      _curDoc = -1;
    }
  }
  
  @Override
  public int fetchDoc(int targetDoc) throws IOException
  {
    if (_curDoc == DocIdSetIterator.NO_MORE_DOCS) return _curDoc;
    
    if(targetDoc <= _curDoc) targetDoc = _curDoc + 1;
    
    _curSec = -1;
    
    SectionSearchQueryPlan node = (SectionSearchQueryPlan)_pq.top();
    while(true)
    {
      if(node._curDoc < targetDoc)
      {
        if(node.fetchDoc(targetDoc) < DocIdSetIterator.NO_MORE_DOCS)
        {
          node = (SectionSearchQueryPlan)_pq.updateTop();
        }
        else
        {
          _pq.pop();
          if (_pq.size() <= 0)
          {
            _curDoc = DocIdSetIterator.NO_MORE_DOCS;
            return _curDoc;
          }
          node = (SectionSearchQueryPlan)_pq.top();
        }
      }
      else
      {
        _curDoc = node._curDoc;
        return _curDoc;
      }
    }
  }
  
  @Override
  public int fetchSec(int targetSec) throws IOException
  {
    if(_curSec == SectionSearchQueryPlan.NO_MORE_SECTIONS) return _curSec;
    
    if(targetSec <= _curSec) targetSec = _curSec + 1;
    
    SectionSearchQueryPlan node = (SectionSearchQueryPlan)_pq.top();
    while(true)
    {
      if(node._curDoc == _curDoc && _curSec < SectionSearchQueryPlan.NO_MORE_SECTIONS)
      {
        if(node._curSec < targetSec)
        {
          node.fetchSec(targetSec);
          node = (SectionSearchQueryPlan)_pq.updateTop();
        }
        else
        {
          _curSec = node._curSec;
          return _curSec;
        }
      }
      else
      {
        _curSec = SectionSearchQueryPlan.NO_MORE_SECTIONS;
        return _curSec;
      }
    }
  }
}
