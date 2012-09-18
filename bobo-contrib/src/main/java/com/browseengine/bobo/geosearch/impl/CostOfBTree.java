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
import java.util.LinkedList;

/**
 * Allows us to approximate the cumulative cost of retrieval, in terms of number of 
 * records read from the tree.
 * If a record was read from the tree in the last 3 gets, we don't increment 
 * (again).
 * If a record was read from the tree either never before, or more than 3 gets ago, 
 * we increment the cost counter.
 * 
 * @author Ken McCracken
 *
 */
public class CostOfBTree<V> extends BTree<V> {
    private final BTree<V> tree;
    private final LinkedList<Integer> previousIndexes;
    private static final int MAX_SIZE_PREVIOUS_INDEXES = 3;
    private int costCounter;
    
    public CostOfBTree(BTree<V> tree) {
        super(tree.getArrayLength(), true);
        this.tree = tree;
        this.previousIndexes = new LinkedList<Integer>();
        this.costCounter = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getArrayLength() {
        return tree.getArrayLength();
    }
    
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    protected int compare(int index, V value) throws IOException {
        incrementCostCounter(index);
        return tree.compare(index, value);
    }
    
    private void incrementCostCounter(int index) {
        Integer indexAsInteger = Integer.valueOf(index);
        if (!previousIndexes.remove(indexAsInteger)) {
            costCounter++;
            if (previousIndexes.size() >= MAX_SIZE_PREVIOUS_INDEXES) {
                previousIndexes.removeLast();
            }
        } // else it is in-memory
        previousIndexes.addFirst(indexAsInteger);
    }
    
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    protected int compareValuesAt(int leftIndex, int rightIndex) throws IOException {
        incrementCostCounter(leftIndex);
        incrementCostCounter(rightIndex);
        return tree.compareValuesAt(leftIndex, rightIndex);
    }
    /**
     * 
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    protected V getValueAtIndex(int index) throws IOException {
        incrementCostCounter(index);
        return tree.getValueAtIndex(index);
    }
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    protected boolean isNullNoRangeCheck(int index) throws IOException {
        incrementCostCounter(index);
        return tree.isNullNoRangeCheck(index);
    }

    @Override
    protected void setValueAtIndex(int index, V value) throws IOException {
        incrementCostCounter(index);
        tree.setValueAtIndex(index, value);
    }

    @Override
    public void close() throws IOException {
        tree.close();
    }
    
    
}
