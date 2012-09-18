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

package com.browseengine.bobo.server.protocol;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.browseengine.bobo.serialize.JSONSerializable;
import com.browseengine.bobo.serialize.JSONSerializer;
import com.browseengine.bobo.serialize.JSONSerializable.JSONSerializationException;

public class JSONHandler extends ProtocolHandler {
	private static final String protocol="json";
	@Override
	public Object deserializeRequest(Class reqClass, HttpServletRequest req)
			throws IOException {
		String reqString=req.getParameter("req");
		if (null == reqString) {
			throw new IOException("no 'req' parameter specified on requet for deserialization of class "+reqClass.toString());
		}
		try {
			JSONObject jsonObj=new JSONObject(reqString);
			return JSONSerializer.deSerialize(reqClass, jsonObj);
		} catch (Exception e) {
			throw new IOException("deserialize request with class "+reqClass.toString()+": "+e.toString());
		} 
	}

	@Override
	public String getSupportedProtocol() {
		return protocol;
	}

	@Override
	public byte[] serializeResult(Object result) throws IOException {
		JSONObject jsonObj;
		try {
			jsonObj = JSONSerializer.serializeJSONObject((JSONSerializable)result);
			return jsonObj.toString().getBytes("UTF-8");
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		
	}

	@Override
	public Object deserializeRequest(Class reqClass, byte[] req) throws IOException {
		try {
			JSONObject jsonObj=new JSONObject(new String(req,"UTF-8"));
			return JSONSerializer.deSerialize(reqClass, jsonObj);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} 
	}

}
