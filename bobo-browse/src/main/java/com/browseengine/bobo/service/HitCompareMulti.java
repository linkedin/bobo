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

package com.browseengine.bobo.service;

import java.util.Comparator;

import com.browseengine.bobo.api.BrowseHit;

public class HitCompareMulti implements Comparator<BrowseHit>
{
	protected Comparator<BrowseHit>[] m_hcmp;

	public HitCompareMulti(Comparator<BrowseHit>[] hcmp)
	{
		m_hcmp = hcmp;
	}

	// HitCompare
	public int compare(BrowseHit h1, BrowseHit h2)
	{
		int retVal=0;
		for (int i=0;i<m_hcmp.length;++i){
			retVal=m_hcmp[i].compare(h1, h2);
			if (retVal!=0) break;
		}
		return retVal;
	}
}
