/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

/**
 * A utility class to facilitate the creation of JSONP requests to 3rd party services. 
 * @author gubel
 */
public class JSONPUtility{
	
	
	/**
	 * Get the content of the URL.
	 * @param url URL to fetch.
	 * @param handler A {@link JSONPRequestHandler} handler to be notified asynchronously once the content has been fetched.
	 */
	public static void get (String url, JSONPRequestHandler handler){
		int c = handler.hashCode();
		String callbackFunctionName = "callback" + c;
		JSONRequestHandlerProxy proxy =  new JSONRequestHandlerProxy(handler);
		createCallbackFunction(callbackFunctionName, proxy);
		createScript(url + callbackFunctionName, c );
	}
	
	/**
	 * JSNI code to create a script element and add it to the DOM
	 * @param url URL to fetch
	 * @param id Unique id of the script element.
	 */
	private static native void createScript(String url, int id)/*-{
		var s = document.createElement("script");
		s.setAttribute("id",id);
		s.setAttribute("language","Javascript");
		s.setAttribute("src",url);
		document.getElementsByTagName("body")[0].appendChild(s);
	}-*/;

	/**
	 * JSNI method to create a callback function and attach it to a {@link JSONPRequestHandler}
	 * @param name Callback function name.
	 * @param handler A {@link JSONPRequestHandler} invoked when the function is called.
	 */
	public static native void createCallbackFunction(String name, JSONPRequestHandler handler)/*-{
		callback = function (json){
			handler.@com.kafecho.stweetmap.client.JSONPRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(json);
		}
		eval ( "window." + name +  " = callback;");
	}-*/;

	
}
