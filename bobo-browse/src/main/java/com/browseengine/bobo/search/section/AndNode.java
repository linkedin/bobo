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
 * AND operator node for SectionSearchQueryPlan
 */
public class AndNode extends SectionSearchQueryPlan
{
  protected SectionSearchQueryPlan[] _subqueries;
  
  public AndNode(SectionSearchQueryPlan[] subqueries)
  {
    _subqueries = subqueries;
    _curDoc = (subqueries.length > 0 ? -1 : DocIdSetIterator.NO_MORE_DOCS);
  }
  
  @Override
  public int fetchDoc(int targetDoc) throws IOException
  {
    if(_curDoc == DocIdSetIterator.NO_MORE_DOCS)
    {
      return _curDoc;
    }
    
    SectionSearchQueryPlan node = _subqueries[0];
    _curDoc = node.fetchDoc(targetDoc);
    targetDoc = _curDoc;
    
    int i = 1;
    while(i < _subqueries.length)
    {
      node = _subqueries[i];
      if(node._curDoc < targetDoc)
      {
        _curDoc = node.fetchDoc(targetDoc);
        if(_curDoc == DocIdSetIterator.NO_MORE_DOCS)
        {
          return _curDoc;
        }
        
        if(_curDoc > targetDoc)
        {
          targetDoc = _curDoc;
          i = 0;
          continue;
        }
      }
      i++;
    }
    _curSec = -1;
    return _curDoc;
  }
  
  @Override
  public int fetchSec(int targetSec) throws IOException
  {
    SectionSearchQueryPlan node = _subqueries[0];
    targetSec = node.fetchSec(targetSec);
    if (targetSec == SectionSearchQueryPlan.NO_MORE_SECTIONS)
    {
      _curSec = SectionSearchQueryPlan.NO_MORE_SECTIONS;
      return targetSec;
    }
    
    int i = 1;
    while(i < _subqueries.length)
    {
      node = _subqueries[i];
      if(node._curSec < targetSec)
      {
        _curSec = node.fetchSec(targetSec);
        if (_curSec == SectionSearchQueryPlan.NO_MORE_SECTIONS)
        {
          return _curSec;
        }
        
        if(_curSec > targetSec)
        {
          targetSec = _curSec;
          i = 0;
          continue;
        }
      }
      i++;
    }
    return _curSec;
  }
}
