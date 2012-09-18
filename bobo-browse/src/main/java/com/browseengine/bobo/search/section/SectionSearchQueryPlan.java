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
import org.apache.lucene.util.PriorityQueue;

/**
 * This class represents a section search query plan
 *
 */
public abstract class SectionSearchQueryPlan
{
  public static final int NO_MORE_POSITIONS = Integer.MAX_VALUE;
  public static final int NO_MORE_SECTIONS = Integer.MAX_VALUE;
  
  protected int _curDoc;
  protected int _curSec;
  
  /*
   * Priority queue of Nodes.
   */
  static public class NodeQueue extends PriorityQueue
  {
    public NodeQueue(int size)
    {
      initialize(size);
    }

    protected boolean lessThan(Object objA, Object objB)
    {
      SectionSearchQueryPlan nodeA = (SectionSearchQueryPlan)objA;
      SectionSearchQueryPlan nodeB = (SectionSearchQueryPlan)objB;
      if(nodeA._curDoc == nodeB._curDoc)
      {
        return (nodeA._curSec < nodeB._curSec);
      }
      return (nodeA._curDoc < nodeB._curDoc);
    }
  }
  
  public SectionSearchQueryPlan()
  {
    _curDoc = -1;
    _curSec = -1;
  }

  public int getDocId()
  {
    return _curDoc;
  }
  
  public int getSecId()
  {
    return _curSec;
  }
  
  public int fetch(int targetDoc) throws IOException
  {
    while(fetchDoc(targetDoc) < DocIdSetIterator.NO_MORE_DOCS)
    {
      if(fetchSec(0) < SectionSearchQueryPlan.NO_MORE_SECTIONS) return _curDoc;
    }
    return _curDoc;
  }

  abstract public int fetchDoc(int targetDoc) throws IOException;
  
  abstract public int fetchSec(int targetSec) throws IOException;
  
  protected int fetchPos() throws IOException
  {
    return NO_MORE_POSITIONS;
  }
}
