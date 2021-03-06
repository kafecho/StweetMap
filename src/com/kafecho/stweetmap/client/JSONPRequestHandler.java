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
 * Interface for a handler to be notified when a JSONP request completes.
 * @author Guillaume Belrose
 */
public interface JSONPRequestHandler{
	/**
	 * Notification that the JSONP request has completed.
	 * @param obj The {@link JavaScriptObject} that has been fetched.
	 */
	public void onRequestComplete(JavaScriptObject obj);
}
