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

package org.apache.lucene.index;

import java.util.List;

import org.apache.lucene.index.MergePolicy.MergeAbortedException;
import org.apache.lucene.store.Directory;

import com.browseengine.bobo.geosearch.merge.IGeoMergeInfo;

/**
 * Class that contains information about the ongoing Geo Merge
 * 
 * @author Geoff Cooney
 *
 */
public class GeoMergeInfo implements IGeoMergeInfo {
    MergePolicy.OneMerge merge;
    Directory directory;
    
    
    public GeoMergeInfo(MergePolicy.OneMerge merge, Directory directory) {
        this.merge = merge;
        this.directory = directory;
    }
    
    /* (non-Javadoc)
     * @see org.apache.lucene.index.IGeoMergeInfo#checkAborted(org.apache.lucene.store.Directory)
     */
    @Override
    public void checkAborted(Directory dir) throws MergeAbortedException {
        merge.checkAborted(dir);
    }
    
    /* (non-Javadoc)
     * @see org.apache.lucene.index.IGeoMergeInfo#getReaders()
     */
    @Override
    public List<SegmentReader> getReaders() {
        return merge.readerClones;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.index.IGeoMergeInfo#getSegmentsToMerge()
     */
    @Override
    public List<SegmentInfo> getSegmentsToMerge() {
        return merge.segments;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.index.IGeoMergeInfo#getDirectory()
     */
    @Override
    public Directory getDirectory() {
        return merge.info.dir;
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.index.IGeoMergeInfo#getNewSegment()
     */
    @Override
    public SegmentInfo getNewSegment() {
        return merge.info;
    }
    
}
