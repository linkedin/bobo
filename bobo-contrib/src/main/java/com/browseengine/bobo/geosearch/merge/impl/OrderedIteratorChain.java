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

package com.browseengine.bobo.geosearch.merge.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Like an apache commons collections , IteratorChain, but 
 * we want to select from our component List of Iterators 
 * according to the smallest value next, determined by 
 * the comparator.  The assumption here to get a correct ordering 
 * on this overall iterator, is that each component iterator 
 * is ordered as well.  The input iterators 
 * is already sorted according to that Comparator, in ascending 
 * .compareTo(..) order (normal Java ordering).
 * 
 * @author Ken McCracken
 *
 */
public class OrderedIteratorChain<V> implements Iterator<V> {

    private List<PeekingIterator<V>> listOfOrderedIterators;
    private Comparator<V> comparator;
    private int numberOfIterators;
    
    public OrderedIteratorChain(List<Iterator<V>> listOfOrderedIterators, 
            Comparator<V> comparator) {
        this.numberOfIterators = listOfOrderedIterators.size();
        this.listOfOrderedIterators = new ArrayList<PeekingIterator<V>>(numberOfIterators);
        for (int i = 0; i < numberOfIterators; i++) {
            this.listOfOrderedIterators.add(new PeekingIterator<V>(listOfOrderedIterators.get(i)));
        }
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        for (int i = 0; i < numberOfIterators; i++) {
            if (listOfOrderedIterators.get(i).hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    private int findMinimumFromPeeks() {
        V minimum = null;
        int nextCameFromIteratorAt = -1;
        for (int i = 0; i < numberOfIterators; i++) {
            V candidate = listOfOrderedIterators.get(i).peek();
            if (null != candidate) {
                if (null == minimum) {
                    minimum = candidate;
                    nextCameFromIteratorAt = i;
                } else {
                    int comparison = comparator.compare(minimum, candidate);
                    // < 0 would indicate we have the correct minimum
                    // = 0 means either is fine
                    // > 0 means we should change our minimum
                    if (comparison > 0) {
                        minimum = candidate;
                        nextCameFromIteratorAt = i;
                    }
                }
            }
        }
        return nextCameFromIteratorAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V next() {
        int minimumFromPeeks = findMinimumFromPeeks();
        if (minimumFromPeeks < 0) {
            throw new NoSuchElementException();
        }
        return listOfOrderedIterators.get(minimumFromPeeks).next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
