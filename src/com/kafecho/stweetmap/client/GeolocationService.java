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
 * The {@link GeolocationService} is a thin GWT wrapper around the Geocoder object part of Google Maps V3.
 * It is used to translate location strings into (latitude,longitude) information.
 * @author Guillaume Belrose
 */
public class GeolocationService {
	private JavaScriptObject geocoder = null;

	/**
	 * Constructor.
	 */
	public GeolocationService(){
		geocoder = initGeocoder();
	}

	/**
	 * Initializes the geocoder. 
	 * @return A {@link JavaScriptObject} corresponding to the geocoder instance.
	 */
	private native static JavaScriptObject initGeocoder()/*-{
		var geocoder = new $wnd.google.maps.Geocoder();
		return geocoder;
	}-*/;

	/**
	 * Asynchronous lookup of address.
	 * @param address Address to lookup.
	 * @param handler A {@link GeolocationRequestHandler} to be notified with lookup results.
	 */
	public void lookup(String address, GeolocationRequestHandler handler){
		lookup ( geocoder , address, handler);
	}

	/**
	 * JSNI method to perform the lookup
	 * @param geocoder Geocoder {@link JavaScriptObject} instance. 
	 * @param address Address to look up
	 * @param handler A {@link GeolocationRequestHandler} to be notified with lookup results.
	 */
	private native static void lookup (JavaScriptObject geocoder, String address, GeolocationRequestHandler handler)/*-{
		geocoder.geocode( { address: address}, function(results, status){
			if (status == $wnd.google.maps.GeocoderStatus.OK){
				handler.@com.kafecho.stweetmap.client.GeolocationRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
          	};
            else{
				handler.@com.kafecho.stweetmap.client.GeolocationRequestHandler::onRequestFailed();
            }
    	});
	}-*/;
}
