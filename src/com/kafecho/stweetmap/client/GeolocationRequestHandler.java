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
 * Handler passed to the {@link GeolocationService} for asynchronous address lookup.
 * @author Guillaume Belrose
 */
public interface GeolocationRequestHandler {
	
	/**
	 * Notification of successful request lookup.
	 * @param json A json object {@link JavaScriptObject} containing latitude, longitude data. 
	 */
	public void onRequestComplete(JavaScriptObject json);
	
	/**
	 * Notification of failed request lookup.
	 */
	public void onRequestFailed();
}
