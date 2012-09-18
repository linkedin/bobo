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

package com.browseengine.bobo.util.test;

import java.util.HashMap;

import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import com.browseengine.bobo.search.section.IntMetaDataCache;
import com.browseengine.bobo.search.section.MetaDataCache;
import com.browseengine.bobo.search.section.MetaDataCacheProvider;

public class IndexReaderWithMetaDataCache extends FilterIndexReader implements MetaDataCacheProvider
{

  private final static Term intMetaTerm = new Term("metafield", "intmeta");
  private HashMap<Term,MetaDataCache> map = new HashMap<Term,MetaDataCache>();
  
  public IndexReaderWithMetaDataCache(IndexReader in) throws Exception
  {
    super(in);
    
    map.put(intMetaTerm, new IntMetaDataCache(intMetaTerm, in));
  }

  public MetaDataCache get(Term term)
  {
    return map.get(term);
  }
}
