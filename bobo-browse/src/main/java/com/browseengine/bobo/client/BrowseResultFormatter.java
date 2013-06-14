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

package com.browseengine.bobo.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.browseengine.bobo.api.BrowseFacet;
import com.browseengine.bobo.api.BrowseHit;
import com.browseengine.bobo.api.BrowseResult;
import com.browseengine.bobo.api.FacetAccessible;

public class BrowseResultFormatter{
    
    static String formatResults(BrowseResult res) {
            StringBuffer sb = new StringBuffer();
            sb.append(res.getNumHits());
            sb.append(" hits out of ");
            sb.append(res.getTotalDocs());
            sb.append(" docs\n");
            BrowseHit[] hits = res.getHits();
            Map<String,FacetAccessible> map = res.getFacetMap();
            Set<String> keys = map.keySet();
            for(String key : keys) {
                    FacetAccessible fa = map.get(key);
                    sb.append(key + "\n");
                    List<BrowseFacet> lf = fa.getFacets();
                    for(BrowseFacet bf : lf) {
                            sb.append("\t" + bf + "\n");
                    }
            }
            for(BrowseHit hit : hits) {
                    sb.append("------------\n");
                    sb.append(formatHit(hit));
                    sb.append("\n");
            }
            sb.append("*****************************\n");
            return sb.toString();
    }
    
    static StringBuffer formatHit(BrowseHit hit) {
            StringBuffer sb = new StringBuffer();
            Map<String, String[]> fields = hit.getFieldValues();
            Set<String> keys = fields.keySet();
            for(String key : keys) {
                    sb.append("\t" + key + " :");
                    String[] values = fields.get(key);
                    for(String value : values)
                    {
                            sb.append(" " + value);
                    }
                    sb.append("\n");
            }
            return sb;
    }
}
