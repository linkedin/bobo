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

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.store.Directory;

import com.browseengine.bobo.geosearch.IGeoRecordIterator;
import com.browseengine.bobo.geosearch.bo.CartesianGeoRecord;
import com.browseengine.bobo.geosearch.bo.GeoSegmentInfo;

/**
 * @author Ken McCracken
 *
 */
public class GeoRecordBTree extends BTree<CartesianGeoRecord> implements IGeoRecordIterator {
    public static final long HIGH_ORDER_NULL_NODE_VALUE = -1L;
    
    private final long[] highOrders;
    private final long[] lowOrders;
    private final byte[] filterBytes;

    public GeoRecordBTree(int treeSize, Iterator<CartesianGeoRecord> inputIterator, Directory directory, 
            String fileName, GeoSegmentInfo geoSegmentInfo) throws IOException {
        super(treeSize, false);
        
        highOrders = new long[this.arrayLength];
        lowOrders = new long [this.arrayLength];
        filterBytes = new byte [this.arrayLength];
        
        buildTreeFromIterator(inputIterator);
        
        nullCheckChecksValues = true;
    }
    
    public GeoRecordBTree(Set<CartesianGeoRecord> tree) throws IOException {
        super(tree.size(), false);
        highOrders = new long[this.arrayLength];
        lowOrders = new long [this.arrayLength];
        filterBytes = new byte [this.arrayLength];
        
        Iterator<CartesianGeoRecord> it = tree.iterator();
        buildTreeFromIterator(it);
        nullCheckChecksValues = true;
    }
    
    private void buildTreeFromIterator(Iterator<CartesianGeoRecord> geoIter) throws IOException {
        int index = leftMostLeafIndex;
        while(geoIter.hasNext()) {
            CartesianGeoRecord geoRecord = geoIter.next();
            highOrders [index] = geoRecord.highOrder;
            lowOrders [index] = geoRecord.lowOrder;
            filterBytes [index] = geoRecord.filterByte;
            index = this.getNextIndex(index);
        }
    }
    
    public GeoRecordBTree(long[] highOrders, long[] lowOrders, byte[] filterBytes) {
        super(highOrders.length, true);
        this.highOrders = highOrders;
        this.lowOrders = lowOrders;
        this.filterBytes = filterBytes;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getArrayLength() {
        return highOrders.length;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int compare(int index, CartesianGeoRecord value) {
        return compare(highOrders[index], lowOrders[index], value.highOrder, value.lowOrder);
    }
    
    private int compare(long leftHighOrder, long leftLowOrder, long rightHighOrder, long rightLowOrder) {
        long diff = leftHighOrder - rightHighOrder;
        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        }
        long lowdiff = leftLowOrder - rightLowOrder;
        if(lowdiff < 0) {
            return -1;
        } else if (lowdiff > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int compareValuesAt(int leftIndex, int rightIndex) {
        return compare(highOrders[leftIndex], lowOrders[leftIndex], highOrders[rightIndex], lowOrders[rightIndex]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CartesianGeoRecord getValueAtIndex(int index) {
        return new CartesianGeoRecord(highOrders[index], lowOrders[index], filterBytes[index]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isNullNoRangeCheck(int index) {
        long highOrder = highOrders[index];
        return HIGH_ORDER_NULL_NODE_VALUE == highOrder;
    }

    public long[] getHighOrders() {
        return highOrders;
    }

    

    public long[] getLowOrders() {
        return lowOrders;
    }

    

    public byte[] getFilterBytes() {
        return filterBytes;
    }

   

    public static long getHighOrderNullNodeValue() {
        return HIGH_ORDER_NULL_NODE_VALUE;
    }

    @Override
    protected void setValueAtIndex(int index, CartesianGeoRecord value) throws IOException {
        highOrders[index] = value.highOrder;
        lowOrders[index] = value.lowOrder;
        filterBytes[index] = value.filterByte;
    }

    @Override
    public void close() throws IOException {
    }
    
}
