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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.browseengine.bobo.geosearch.IGeoUtil;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.bo.LatitudeLongitudeDocId;

@Component
public class GeoUtil implements IGeoUtil {

    @Override
    public Iterator<CartesianGeoRecord> getGeoRecordIterator(Iterator<LatitudeLongitudeDocId> lldidIter) {
        GeoConverter gc = new GeoConverter();
        ArrayList<CartesianGeoRecord> grl = new ArrayList<CartesianGeoRecord>();
        while (lldidIter.hasNext()) {
            grl.add(gc.toCartesianGeoRecord(lldidIter.next(), (byte)0));
        }
        return grl.iterator();
    }

    @Override
    public TreeSet<CartesianGeoRecord> getBinaryTreeOrderedByBitMag(Iterator<CartesianGeoRecord> grIter) {
        TreeSet<CartesianGeoRecord> tree = getBinaryTreeOrderedByBitMag();
        while(grIter.hasNext()){
            tree.add(grIter.next());
        }
        return tree;
    }

    @Override
    public TreeSet<CartesianGeoRecord> getBinaryTreeOrderedByBitMag() {
        return new TreeSet<CartesianGeoRecord>(new CartesianGeoRecordComparator());
    }
    
    @Override
    public TreeSet<CartesianGeoRecord> getBinaryTreeOrderedByDocId(Iterator<CartesianGeoRecord> grtIter) {
        TreeSet<CartesianGeoRecord> tree = new TreeSet<CartesianGeoRecord>(new CartesianGeoRecordCompareByDocId());
        while(grtIter.hasNext()){
            tree.add(grtIter.next());
        }
        return tree;
    }

    @Override
    public Iterator<CartesianGeoRecord> getGeoRecordRangeIterator(TreeSet<CartesianGeoRecord> tree, CartesianGeoRecord minRange, CartesianGeoRecord maxRange) {
        return tree.subSet(minRange, maxRange).iterator();
    }

    private static final double MINIMUM_LONGITUDE_EXCLUSIVE = -180.;
    private static final double MAXIMUM_LONGITUDE_INCLUSIVE = 180.;
    private static final double MINIMUM_LATITUDE_INCLUSIVE = -90.;
    private static final double MAXIMUM_LATITUDE_INCLUSIVE = 90.;
    
    public static boolean isValidLongitude(Double longitude) {
        return null != longitude 
        && MINIMUM_LONGITUDE_EXCLUSIVE <= longitude 
        && longitude <= MAXIMUM_LONGITUDE_INCLUSIVE;
    }
    
    public static boolean isValidLatitude(Double latitude) {
        return null != latitude 
        && MINIMUM_LATITUDE_INCLUSIVE <= latitude
        && latitude <= MAXIMUM_LATITUDE_INCLUSIVE;
    }
}


