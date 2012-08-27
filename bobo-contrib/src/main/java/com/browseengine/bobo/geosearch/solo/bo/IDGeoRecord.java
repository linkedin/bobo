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

package com.browseengine.bobo.geosearch.solo.bo;

import java.util.Arrays;

import com.browseengine.bobo.geosearch.bo.IGeoRecord;

public class IDGeoRecord implements IGeoRecord {
    public final long highOrder;
    public final int lowOrder;
    
    public final byte[] id;
    
    public static final IDGeoRecord MIN_VALID_GEORECORD = 
        new IDGeoRecord(0, 0, new byte[0]);
    
    public static final IDGeoRecord MAX_VALID_GEORECORD = 
        new IDGeoRecord(Long.MAX_VALUE, Integer.MAX_VALUE, new byte[0]);
    
    public IDGeoRecord(long highOrder, int lowOrder, byte[] id) {
        if (highOrder < 0L || lowOrder < 0) {
            throw new RuntimeException("GeoRecord(" + highOrder + ", " + lowOrder 
                    + ", " + id + "): only supports positive highOrder and lowOrder");
        }
        
        this.highOrder = highOrder;
        this.lowOrder = lowOrder;
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (highOrder ^ (highOrder >>> 32));
        result = prime * result + Arrays.hashCode(id);
        result = prime * result + lowOrder;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        IDGeoRecord other = (IDGeoRecord) obj;
        if (highOrder != other.highOrder)
            return false;
        if (!Arrays.equals(id, other.id))
            return false;
        if (lowOrder != other.lowOrder)
            return false;
        if (!Arrays.equals(id, other.id))
            return false;
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GeoRecord [highOrder=" + highOrder + ", lowOrder=" + 
            lowOrder + ", id=" + Arrays.toString(id) + "]";
    }
}
