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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.search.Explanation;

public abstract class BoboDocScorer {
	protected final FacetTermScoringFunction _function;
	protected final float[] _boostList;
	
	public BoboDocScorer(FacetTermScoringFunction scoreFunction,float[] boostList){
		_function = scoreFunction;
		_boostList = boostList;
	}
	
    public abstract float score(int docid);
    
    abstract public Explanation explain(int docid);
    
    public static float[] buildBoostList(List<String> valArray,Map<String,Float> boostMap){
    	float[] boostList = new float[valArray.size()];
    	Arrays.fill(boostList, 0.0f);
    	if (boostMap!=null && boostMap.size()>0){
    		Iterator<Entry<String,Float>> iter = boostMap.entrySet().iterator();
    		while(iter.hasNext()){
    			Entry<String,Float> entry = iter.next();
    			int index = valArray.indexOf(entry.getKey());
    			if (index>=0){
    				Float fval = entry.getValue();
    				if (fval!=null){
    				  boostList[index] = fval.floatValue();
    				}
    			}
    		}
    	}
    	return boostList;
    }
}
