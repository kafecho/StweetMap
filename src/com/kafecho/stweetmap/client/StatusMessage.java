/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * A class to model a Twitter status update.
 * @author guillaume
 */
public class StatusMessage{
	
	private StatusMessageListener listener = null;
	private String user;
	private String text;
	private String profileImageURL;
	private String location;
	public 	Double lat = null;
	private Double lng = null;

	/**
	 * Constructs a status message from a JSON object.
	 * The structure of the JSON object is described at {@link http://apiwiki.twitter.com/}
	 * @param json A {@link JSONObject} corresponding to a Twitter status update.
	 */
	public StatusMessage(JSONObject json){
		user = ((JSONString) json.get("from_user")).stringValue();
		text = ((JSONString) json.get("text")).stringValue();
		profileImageURL = ((JSONString) json.get("profile_image_url")).stringValue();
	}
	
	/**
	 * Set the status update location.
	 * @param _location
	 */
	public void setLocation(String _location){
		location = _location;
		if (listener != null) listener.onLocationSet();
	}

	/**
	 * Set the status update latitude and longitude.
	 * @param _lat Latitude.
	 * @param _lng Longitude.
	 */
	public void setLatLng(double _lat, double _lng){
		lat = new Double(_lat);
		lng = new Double(_lng);
		if (listener != null) listener.onLatLngSet();
	}

	/**
	 * @return The status update location, or null if not set.
	 */
	public String getLocation(){
		return location;
	}
	
	/**
	 * @return The status update user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return The status update text.
	 */
	public String getText(){
		return text;
	}

	/**
	 * @return The profile URL.
	 */
	public String getProfileImageURL() {
		return profileImageURL;
	}

	/**
	 * Sets the {@link StatusMessageListener}.
	 * @param _listener Listener to set.
	 */
	public void setListener(StatusMessageListener _listener){
		listener = _listener;
	}

	/**
	 * @return The message latitude.
	 */
	public Double getLat() {
		return lat;
	}


	/**
	 * @return The message longitude.
	 */
	public Double getLng() {
		return lng;
	}
}
