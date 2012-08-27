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
 * AND-NOT operator node for SectionSearchQueryPlan
 */
public class AndNotNode extends SectionSearchQueryPlan
{
  SectionSearchQueryPlan _positiveNode;
  SectionSearchQueryPlan _negativeNode;
  
  public AndNotNode(SectionSearchQueryPlan positiveNode, SectionSearchQueryPlan negativeNode)
  {
    super();
    _positiveNode = positiveNode;
    _negativeNode = negativeNode;
  }
  
  @Override
  public int fetchDoc(int targetDoc) throws IOException
  {
    _curDoc = _positiveNode.fetchDoc(targetDoc);
    _curSec = -1;
    return _curDoc;
  }
  
  @Override
  public int fetchSec(int targetSec) throws IOException
  {
    while(_curSec < SectionSearchQueryPlan.NO_MORE_SECTIONS)
    {
      _curSec = _positiveNode.fetchSec(targetSec);
      if (_curSec == SectionSearchQueryPlan.NO_MORE_SECTIONS) break;

      targetSec = _curSec;

      if(_negativeNode._curDoc < _curDoc)
      {
        if(_negativeNode.fetchDoc(_curDoc) == DocIdSetIterator.NO_MORE_DOCS) break;
      }
          
      if(_negativeNode._curDoc == _curDoc &&
          (_negativeNode._curSec == SectionSearchQueryPlan.NO_MORE_SECTIONS ||
           _negativeNode.fetchSec(targetSec) > _curSec))
      {
        break;
      }
    }
    return _curSec;
  }
}
