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

/**
 * UNARY-NOT operator node
 * (this node is not supported by SectionSearchQueryPlan)
 */
public class UnaryNotNode extends SectionSearchQueryPlan
{
  private SectionSearchQueryPlan _subquery;
  
  public UnaryNotNode(SectionSearchQueryPlan subquery)
  {
    super();
    _subquery = subquery;
  }
  
  public SectionSearchQueryPlan getSubquery()
  {
    return _subquery;
  }
  
  @Override
  public int fetchDoc(int targetDoc) throws IOException
  {
    throw new UnsupportedOperationException("UnaryNotNode does not support fetchDoc");
  }

  @Override
  public int fetchSec(int targetSec) throws IOException
  {
    throw new UnsupportedOperationException("UnaryNotNode does not support fetchSec");
  }
}
