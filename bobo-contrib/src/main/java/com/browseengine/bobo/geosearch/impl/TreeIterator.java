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
import java.util.NoSuchElementException;

class TreeIterator<T> implements Iterator<T> {
    private final int rightIndex;
    private int index;
    private BTree<T> treeAsArray;
    
    public TreeIterator(BTree<T> treeAsArray, int leftIndex, int rightIndex) {
        this.treeAsArray = treeAsArray;
        this.index = leftIndex;
        this.rightIndex = rightIndex;
    }

    @Override
    public boolean hasNext() {
        return index != BTree.INDEX_OUT_OF_BOUNDS;
    }

    @Override
    public T next() {
        
        
        try {
            if (index == BTree.INDEX_OUT_OF_BOUNDS) {
                throw new NoSuchElementException();
            }
            T value;
            value = treeAsArray.getValueAtIndex(index);
            if (index == rightIndex) {
                index = BTree.INDEX_OUT_OF_BOUNDS;
            } else {
                index = treeAsArray.getNextIndex(index);
            }
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
        
    }
}
