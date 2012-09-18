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

package com.browseengine.solr;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.IndexReaderFactory;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.BoboIndexReader.WorkArea;

public class BoboIndexReaderFactory extends IndexReaderFactory {
	public void init(NamedList conf) {
	}

	@Override
	public IndexReader newReader(Directory indexDir, boolean readOnly)
			throws IOException {
		IndexReader reader=IndexReader.open(indexDir,null,readOnly,termInfosIndexDivisor);
		BoboIndexReader boboReader=null;
		try{
			WorkArea workArea = new WorkArea();
			workArea.put(getClass().getClassLoader());
			boboReader=BoboIndexReader.getInstance(reader,workArea);
			return boboReader;
		}
		catch(IOException e){
			if (reader!=null){
				reader.close();
			}
			throw e;
		}
	}
}
