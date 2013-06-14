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

package com.browseengine.bobo.query;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Scorer;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.facets.data.FacetDataCache;
import com.browseengine.bobo.facets.data.TermLongList;
import com.browseengine.bobo.util.BigSegmentedArray;

public class RecencyBoostScorerBuilder implements ScorerBuilder {

	private final float _maxFactor;
	private final TimeUnit _timeunit;
	private final float _min;
	private final float _max;
	private final long _cutoffInMillis;
	private final float _A;
	private final String _timeFacetName;
	private final long _now;
	
	public RecencyBoostScorerBuilder(String timeFacetName,float maxFactor,long cutoff,TimeUnit timeunit){
		this(timeFacetName,maxFactor,timeunit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS),cutoff,timeunit);
	}
	
	public RecencyBoostScorerBuilder(String timeFacetName,float maxFactor,long from,long cutoff,TimeUnit timeunit) {
		_timeFacetName = timeFacetName;
		_maxFactor = maxFactor;
		_min = 1.0f;
		_max = _maxFactor + _min;
		_timeunit = timeunit;
		_cutoffInMillis = _timeunit.toMillis(cutoff);
		_A = (_min - _max) / (((float)_cutoffInMillis)*((float)_cutoffInMillis));
		_now =timeunit.toMillis(from);
	}
	
	public Explanation explain(IndexReader reader, int doc,
			Explanation innerExplaination) throws IOException {
		if (reader instanceof BoboIndexReader){
			  BoboIndexReader boboReader = (BoboIndexReader)reader;
			  Object dataObj = boboReader.getFacetData(_timeFacetName);
			  if (dataObj instanceof FacetDataCache<?>){
			    FacetDataCache<Long> facetDataCache = (FacetDataCache<Long>)(boboReader.getFacetData(_timeFacetName));
			    final BigSegmentedArray orderArray = facetDataCache.orderArray;
			    final TermLongList termList = (TermLongList)facetDataCache.valArray;
			    final long now = System.currentTimeMillis();
			    Explanation finalExpl = new Explanation();
			    finalExpl.addDetail(innerExplaination);
			    float rawScore = innerExplaination.getValue();
			    long timeVal = termList.getPrimitiveValue(orderArray.get(doc));
			    float timeScore = computeTimeFactor(timeVal);
			    float finalScore = combineScores(timeScore,rawScore);
			    finalExpl.setValue(finalScore);
			    finalExpl.setDescription("final score = (time score: "+timeScore+") * (raw score: "+rawScore+"), timeVal: "+timeVal);
			    return finalExpl;
			  }
			  else{
				  throw new IllegalStateException("underlying facet data must be of type FacetDataCache<Long>");
			  }
			}
			else{
			  throw new IllegalStateException("reader not instance of "+BoboIndexReader.class);
			}
	}

	public Scorer createScorer(final Scorer innerScorer, IndexReader reader,
			boolean scoreDocsInOrder, boolean topScorer) throws IOException {
		if (reader instanceof BoboIndexReader){
		  BoboIndexReader boboReader = (BoboIndexReader)reader;
		  Object dataObj = boboReader.getFacetData(_timeFacetName);
		  if (dataObj instanceof FacetDataCache<?>){
		    FacetDataCache<Long> facetDataCache = (FacetDataCache<Long>)(boboReader.getFacetData(_timeFacetName));
		    final BigSegmentedArray orderArray = facetDataCache.orderArray;
		    final TermLongList termList = (TermLongList)facetDataCache.valArray;
		    return new Scorer(innerScorer.getSimilarity()){
			  
			
			  @Override
		   	  public float score() throws IOException {
			    float rawScore = innerScorer.score();
			    long timeVal = termList.getRawValue(orderArray.get(innerScorer.docID()));
			    float timeScore = computeTimeFactor(timeVal);
			    return combineScores(timeScore,rawScore);
			  }

			  @Override
			  public int advance(int target) throws IOException {
				return innerScorer.advance(target);
			  }

			  @Override
			  public int docID() {
				return innerScorer.docID();
			  }

			  @Override
			  public int nextDoc() throws IOException {
				return innerScorer.nextDoc();
			  }
			  
			  
		    };
		  }
		  else{
			  throw new IllegalStateException("underlying facet data must be of type FacetDataCache<Long>");
		  }
		}
		else{
		  throw new IllegalStateException("reader not instance of "+BoboIndexReader.class);
		}
	}
	
	protected float computeTimeFactor(long timeVal){
		long xVal = _now - timeVal;
		if (xVal > _cutoffInMillis){
			return _min;
		}
		else{
			float xValFloat = (float)xVal;
			return _A*xValFloat*xValFloat+_max;
		}
	}
	
	private static float combineScores(float timeScore,float rawScore){
		return timeScore * rawScore;
	}
}
