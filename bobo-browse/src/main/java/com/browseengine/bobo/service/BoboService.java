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

package com.browseengine.bobo.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import com.browseengine.bobo.api.BoboBrowser;
import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.BrowseException;
import com.browseengine.bobo.api.BrowseRequest;
import com.browseengine.bobo.api.BrowseResult;

public class BoboService{
	private static Logger logger = Logger.getLogger(BoboService.class);
	
	private final File _idxDir;
	private BoboIndexReader _boboReader;
	
	public BoboService(String path)
	{
		this(new File(path));
	}
	
	public BoboService(File idxDir)
	{
		_idxDir=idxDir;
		_boboReader = null;
	}
	
	public BrowseResult browse(BrowseRequest req)
	{
		BoboBrowser browser=null;
		try
		{
			browser = new BoboBrowser(_boboReader);
			return browser.browse(req);
		}
        catch(Exception e)
		{
			logger.error(e.getMessage(),e);
			return new BrowseResult();
		}
		finally
		{
			if (browser!=null)
			{
				try {
					browser.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	public void start() throws IOException
	{
		IndexReader reader=IndexReader.open(FSDirectory.open(_idxDir),true);
		try
		{
			_boboReader=BoboIndexReader.getInstance(reader);
		}
		catch(IOException ioe)
		{
			if (reader!=null)
			{
				reader.close();
			}
		}
	}
	
	public void shutdown()
	{
		if (_boboReader!=null)
		{
			try {
				_boboReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
