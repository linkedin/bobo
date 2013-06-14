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

package com.browseengine.bobo.api;

import java.util.HashSet;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class QueriesSupport {
  public static Query combineAnd(Query... queries) {
    HashSet<Query> uniques = new HashSet<Query>();
    for (int i = 0; i < queries.length; i++) {
      Query query = queries[i];
      BooleanClause[] clauses = null;
      // check if we can split the query into clauses
      boolean splittable = (query instanceof BooleanQuery);
      if(splittable){
        BooleanQuery bq = (BooleanQuery) query;
        splittable = bq.isCoordDisabled();
        clauses = bq.getClauses();
        for (int j = 0; splittable && j < clauses.length; j++) {
          splittable = (clauses[j].getOccur() == BooleanClause.Occur.MUST);
        }
      }
      if(splittable){
        for (int j = 0; j < clauses.length; j++) {
          uniques.add(clauses[j].getQuery());
        }
      } else {
        uniques.add(query);
      }
    }
    // optimization: if we have just one query, just return it
    if(uniques.size() == 1){
        return uniques.iterator().next();
    }
    BooleanQuery result = new BooleanQuery(true);
    for (final Query query : uniques)
      result.add(query, BooleanClause.Occur.MUST);
    return result;
  }
}
