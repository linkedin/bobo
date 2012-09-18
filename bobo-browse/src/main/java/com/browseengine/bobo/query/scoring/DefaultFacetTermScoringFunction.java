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

package com.browseengine.bobo.query.scoring;

import java.util.Arrays;

import org.apache.lucene.search.Explanation;

public class DefaultFacetTermScoringFunction implements FacetTermScoringFunction {
	private float _sum=0.0f;
	
	public final void clearScores(){
		_sum = 0.0f;
	}
	
	public final float score(int df, float boost) {
		return boost;
	}
	
	public final void scoreAndCollect(int df,float boost){
		_sum+=boost;
	}

	public final float getCurrentScore() {
		return _sum;
	}

	public Explanation explain(int df, float boost) {
		Explanation expl = new Explanation();
		expl.setValue(score(df,boost));
		expl.setDescription("facet boost value of: "+boost);
		return expl;
	}

	public Explanation explain(float... scores) {
		Explanation expl = new Explanation();
		float sum = 0.0f;
		for (float score : scores){
			sum+=score;
		}
		expl.setValue(sum);
		expl.setDescription("sum of: "+Arrays.toString(scores));
		return expl;
	}
}
