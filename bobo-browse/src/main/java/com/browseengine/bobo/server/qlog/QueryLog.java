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

package com.browseengine.bobo.server.qlog;

import org.apache.log4j.Logger;


public class QueryLog {
	private static Logger logger=Logger.getLogger(QueryLog.class);
	
	public static class LogLine{
		String protocol;
		String method;
		String request;
		
		private LogLine(){
			
		}
		public String getMethod() {
			return method;
		}
		
		public String getProtocol() {
			return protocol;
		}
		
		public String getRequest() {
			return request;
		}
		
		
	}
	
	public static void logQuery(String request){
		logger.info(request);
	}
	
	public static LogLine readLog(String line){
		
		LogLine log=new LogLine();
		int index=line.indexOf('#');
		if (index!=-1){
			String header=line.substring(0, index);
			log.request=line.substring(index+1,line.length());
			
			String[] parts=header.split("/");
			log.protocol=parts[0];
			log.method=parts[1];
			
		}
		return log;
	}
}
