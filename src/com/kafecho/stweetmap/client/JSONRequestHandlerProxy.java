/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The proxy is used to wrap the {@link JSONPRequestHandler} for garbage collection purposes.
 * @author guillaume
 */
public class JSONRequestHandlerProxy implements JSONPRequestHandler{

	private JSONPRequestHandler handler ;

	public JSONRequestHandlerProxy(JSONPRequestHandler _handler){
		handler = _handler;
	}
	
	/**
	 * Invoked when a request has completed. Forward the result to the real handler and clean up the DOM.
	 */
	public void onRequestComplete(JavaScriptObject obj) {
		handler.onRequestComplete(obj);
		destroyScript(handler.hashCode());
	} 
	
	public static native void destroyScript(int id)/*-{
		var s = document.getElementById(id);
		document.getElementsByTagName("body")[0].removeChild(s);
	}-*/;
}