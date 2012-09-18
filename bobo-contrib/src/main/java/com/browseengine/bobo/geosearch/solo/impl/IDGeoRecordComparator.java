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

package com.browseengine.bobo.geosearch.solo.impl;

import java.util.Comparator;

import com.browseengine.bobo.geosearch.solo.bo.IDGeoRecord;

/**
 * 
 * @author gcooney
 *
 */
public class IDGeoRecordComparator implements Comparator<IDGeoRecord> {

    @Override
    public int compare(IDGeoRecord idGeoRecord1, IDGeoRecord idGeoRecord2) {
        long diff = idGeoRecord1.highOrder - idGeoRecord2.highOrder;
        if (diff > 0) {
            return 1;
        }
        if (diff < 0) {
            return -1;
        }
        int idiff = idGeoRecord1.lowOrder - idGeoRecord2.lowOrder;
        if(idiff > 0) {
            return 1;
        }
        if (idiff < 0) {
            return -1;
        }
        
        for (int i = 0; i < idGeoRecord1.id.length && i < idGeoRecord2.id.length; i++) {
            if (idGeoRecord1.id[i] > idGeoRecord2.id[i]) {
                return 1;
            } else if (idGeoRecord1.id[i] < idGeoRecord2.id[i]) {
                return -1;
            }
        }

        if (idGeoRecord1.id.length > idGeoRecord2.id.length) {
            return 1;
        } else if (idGeoRecord1.id.length < idGeoRecord2.id.length) {
            return -1;
        }
        
        return 0;
    }
 
}
