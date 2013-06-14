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

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * A Buffered and re-ordering iterator that can make local corrections 
 * to the ordering returned by its backing iterator, as long as those 
 * corrections can be made within the buffer window.  
 * To the extent 
 * that it is possible, the ordering is enforced using the input Comparator.
 * Throws a RuntimeException if an out-of-order condition is detected, where a 
 * previously-returned value should have come after an about-to-be-returned value.
 * 
 * @author Ken McCracken
 *
 */
public class BufferedOrderedIterator<V> implements Iterator<V> {

    private Iterator<V> iterator;
    private Comparator<V> comparator;
    private int bufferCapacity;
    private TreeSet<V> buffer;

    public BufferedOrderedIterator(Iterator<V> iterator, Comparator<V> comparator, int bufferCapacity) {
        this.iterator = iterator;
        this.comparator = comparator;
        this.bufferCapacity = bufferCapacity;
        this.buffer = new TreeSet<V>(comparator);
        fill();
    }
    
    private void fill() {
        while (iterator.hasNext() && buffer.size() < bufferCapacity) {
            V next = iterator.next();
            buffer.add(next);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        fill();
        return buffer.size() > 0;
    }
    
    private V prev;

    /**
     * {@inheritDoc}
     */
    @Override
    public V next() {
        fill();
        V current = buffer.pollFirst();
        if (null != prev) {
            int comparison = comparator.compare(prev, current);
            if (comparison > 0) {
                // OUT OF ORDER
                throw new RuntimeException("out-of-order condition detected, prev "
                        +prev+", current "+current+", comparison "+comparison);
            }
        }
        prev = current;
        return current;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
        
    }
    
    
}
