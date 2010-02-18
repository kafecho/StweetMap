/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.kafecho.stweetmap.client.TwitterService;

@SuppressWarnings("serial")
public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {
	
	private HashMap<String, String> infos = new HashMap<String, String>();
	public String getUserInfo(String username){
		String result = infos.get(username);
		if (result == null){
			try{
				URL url = new URL("http://twitter.com/users/show.json?screen_name=" + username);
				url.openConnection();
				StringBuffer buffer = new StringBuffer();
				BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream())); 
				String inputLine; 
				while ((inputLine = in.readLine()) != null) { 
					buffer.append(inputLine);
				} 
				in.close(); 
				result = buffer.toString();
			}catch (Exception e){
			}
			infos.put(username,result);
		}
		return result;
	}
	

	
}
