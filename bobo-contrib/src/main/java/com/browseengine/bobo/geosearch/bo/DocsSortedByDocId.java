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

package com.browseengine.bobo.geosearch.bo;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author Ken McCracken
 *
 */
public class DocsSortedByDocId {
    
    private TreeMap<Integer, Collection<GeoRecordAndLongitudeLatitudeDocId>> docs;
    
    public DocsSortedByDocId() {
        docs = new TreeMap<Integer, Collection<GeoRecordAndLongitudeLatitudeDocId>>(new IntegerComparator());
    }
    
    public void add(int docid, GeoRecordAndLongitudeLatitudeDocId data) {
        Collection<GeoRecordAndLongitudeLatitudeDocId> collection = docs.get(docid);
        if (null != collection) {
            collection.add(data);
        } else {
            collection = new HashSet<GeoRecordAndLongitudeLatitudeDocId>();
            collection.add(data);
            docs.put(docid, collection);
        }
    }
    
    public Iterator<Entry<Integer,Collection<GeoRecordAndLongitudeLatitudeDocId>>> getScoredDocs() {
        return docs.entrySet().iterator();
    }
    
    public Entry<Integer, Collection<GeoRecordAndLongitudeLatitudeDocId>> pollFirst() {
        return docs.pollFirstEntry();
    }
    
    public int size() {
        return docs.size();
    }
    
    private static class IntegerComparator implements Comparator<Integer> {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0.compareTo(arg1);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DocsSortedByDocId [docs=" + docs + "]";
    }
    
    

}
