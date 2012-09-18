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


/**
 * Requires:
 * a total ordering on tree[] where tree[0] is the root node, tree[1] is the left child of root, 
 * tree[2] is right child of root, and so on.
 * The value for a null node should be {{@link #NULL_NODE_VALUE}.
 * 
 * @author Ken McCracken
 *
 */
public class IntBTree extends BTree<Integer> {
    public static final int NULL_NODE_VALUE = -1;
    
    private final int[] tree;
    
    public IntBTree(int[] tree) {
        super(tree.length, true);
        this.tree = tree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getArrayLength() {
        return tree.length;
    }
    
    @Override
    protected boolean isNullNoRangeCheck(int index) {
        int value = tree[index];
        return value == NULL_NODE_VALUE;
    }
    
    @Override
    protected Integer getValueAtIndex(int index) {
        return tree[index];
    }
    
    @Override
    protected int compare(int index, Integer value) {
        int valueAsInt = value;
        return tree[index] - valueAsInt;
    }
    
    @Override
    protected int compareValuesAt(int leftIndex, int rightIndex) {
        return tree[leftIndex] - tree[rightIndex];
    }

    @Override
    protected void setValueAtIndex(int index, Integer value) throws IOException {
        tree[index] = value;
    }

    @Override
    public void close() throws IOException {
    }
    
    
}
