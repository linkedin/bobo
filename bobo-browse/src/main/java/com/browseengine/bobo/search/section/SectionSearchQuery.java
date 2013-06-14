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
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Weight;

/**
 *
 */
public class SectionSearchQuery extends Query
{
  private static final long serialVersionUID = 1L;
  
  private Query _query;
  
  private class SectionSearchWeight extends Weight
  {
    private static final long serialVersionUID = 1L;
    
    float _weight;
    Similarity _similarity;

    public SectionSearchWeight(Searcher searcher) throws IOException
    {
      _similarity = getSimilarity(searcher);
    }

    public String toString()
    {
      return "weight(" + SectionSearchQuery.this + ")";
    }

    public Query getQuery()
    {
      return SectionSearchQuery.this;
    }

    public float getValue()
    {
      return getBoost();
    }

    public float sumOfSquaredWeights()
    {
      _weight = getBoost();
      return _weight * _weight;
    }

    @Override
    public void normalize(float queryNorm)
    {
      _weight *= queryNorm;
    }

    public Scorer scorer(IndexReader reader) throws IOException
    {
      SectionSearchScorer scorer = new SectionSearchScorer(_similarity, getValue(), reader);
      
      return scorer;
    }

    @Override
    public Explanation explain(IndexReader reader, int doc) throws IOException
    {
      Explanation result = new Explanation();
      result.setValue(_weight);
      result.setDescription(SectionSearchQuery.this.toString());

      return result;
    }

    @Override
    public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
    {
      return scorer(reader);
    }
  }

  public class SectionSearchScorer extends Scorer
  {
    private int              _curDoc = -1;
    private float            _curScr;
    private boolean          _more = true; // more hits
    private SectionSearchQueryPlan _plan;
    
    public SectionSearchScorer(Similarity similarity, float score, IndexReader reader)
      throws IOException
    {
      super(similarity);
      _curScr = score;
      
      SectionSearchQueryPlanBuilder builer = new SectionSearchQueryPlanBuilder(reader);
      _plan = builer.getPlan(_query);
      if(_plan != null)
      {
        _curDoc = -1;
        _more = true;
      }
      else
      {
        _curDoc = DocIdSetIterator.NO_MORE_DOCS;
        _more = false;;        
      }
    }
    
    @Override
    public int docID()
    {
      return _curDoc;
    }

    @Override
    public int nextDoc() throws IOException
    {
      return advance(0);
    }

    @Override
    public float score() throws IOException
    {
      return _curScr;
    }

    @Override
    public int advance(int target) throws IOException
    {
      if(_curDoc < DocIdSetIterator.NO_MORE_DOCS)
      {
        if(target <= _curDoc) target = _curDoc + 1;
  
        return _plan.fetch(target);
      }
      return _curDoc;
    }
  }
  
  /**
   * constructs SectionSearchQuery
   * 
   * @param query
   */
  public SectionSearchQuery(Query query)
  {
    _query = query;
  }

  @Override
  public String toString(String field)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("SECTION(" + _query.toString() + ")");
    return buffer.toString();
  }

  @Override
  public Weight createWeight(Searcher searcher) throws IOException
  {
    return new SectionSearchWeight(searcher);
  }

  @Override
  public Query rewrite(IndexReader reader) throws IOException
  {
    _query.rewrite(reader);
    return this;
  }
}
