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

package com.browseengine.bobo.geosearch.impl;
import java.util.Comparator;

import com.browseengine.bobo.geosearch.bo.GeoRecord;
import com.browseengine.bobo.geosearch.bo.LatitudeLongitudeDocId;
public class GeoRecordCompareByDocId implements Comparator<Object>
{

    @Override
    public int compare(Object o1, Object o2) {
        GeoConverter gc = new GeoConverter();
        LatitudeLongitudeDocId lldid1 = gc.toLongitudeLatitudeDocId((GeoRecord)o1);
        LatitudeLongitudeDocId lldid2 = gc.toLongitudeLatitudeDocId((GeoRecord)o2);
        int diff = lldid1.docid - lldid2.docid;
        if(diff < 0) {
            return 1;
        } else if (diff > 0) {
            return -1;
        }
        return 0;
    }

}
