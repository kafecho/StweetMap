/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import java.util.Vector;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Class that wraps a GoogleMaps V3 map as an {@link HTML} GWT object which can be added to a page.
 * @author Guillaume Belrose
 */
public class GoogleMapPanel extends HTML{
	
	private JavaScriptObject js_map = null;
	private static int nextId = 0;
	private Vector<JavaScriptObject> markers = null;

	/**
	 * Constructor
	 * @param root
	 * @param width widdht of the map.
	 * @param height height of the map.
	 * @param zoom zoom level.
	 */
	public GoogleMapPanel(HasWidgets root, int width, int height, int zoom){
		markers = new Vector<JavaScriptObject>();
		addStyleName("googleMapPanel");
		String id = "map" + (++nextId);
		setHTML("<div id='" + id + "' style='width:" + width + "px; height:" + height + "px'></div>");
		setWidth(width+"px");
		setHeight(height+"px");
		root.add(this);
		js_map = createMap (id,3.05,5.32,zoom);
	}

	/**
	 * JSNI code to create the map
	 * @param id map identifier
	 * @param lat initial latitude
	 * @param lng initial longitde
	 * @param zoom zoom level
	 * @return A {@link JavaScriptObject} corresponding to the map instance.
	 */
	private native static JavaScriptObject createMap(String id,double lat, double lng, int zoom) /*-{
		var latlng = new $wnd.google.maps.LatLng(lat,lng);
    	var myOptions = {
      		zoom: zoom,
      		center: latlng,
      		mapTypeId: $wnd.google.maps.MapTypeId.ROADMAP
    	};
    	var map = new $wnd.google.maps.Map($doc.getElementById(id), myOptions);
    	return map;
	}-*/;

	/**
	 * Center the map on a given point
	 * @param lat Point's latitude.
	 * @param lng Point's longitude
	 * @param zoom Zoom level
	 * @param text If not null, displays the text in a marker.
	 */
	public void setCenter(double lat, double lng, int zoom, String text){
		setCenter (js_map,lat, lng, zoom);
		if (text != null){
			createMarker ( lat,lng,text);
		}
	}
	
	/**
	 * JNSI method to center the map on a given point.
	 * @param map {@link JavaScriptObject} object corresponding to the map to center
	 * @param lat Point's latitude.
	 * @param lng Point's longitude
	 * @param zoom Zoom level.
	 */
	public static native void setCenter(JavaScriptObject map, double lat, double lng, int zoom)/*-{
		map.setCenter( new $wnd.google.maps.LatLng(lat,lng));
		map.setZoom (zoom);
	}-*/;
	
	/**
	 * Add a marker on the map at a given location.
	 * @param lat Point's latitude.
	 * @param lng Point's longitude.
	 * @param text Marker's text.
	 */
	public void createMarker(double lat, double lng, String text){
		markers.add(createMarker(js_map,lat,lng,text));
	}

	/**
	 * JNSI method to add marker on a given point on a given map
	 * @param map {@link JavaScriptObject} object corresponding to the map.
	 * @param lat Point's latitude.
	 * @param lng Point's longitude.
	 * @param text Marker's text.
	 * @return A {@link JavaScriptObject} corresponding to the marker.
	 */
	private static native JavaScriptObject createMarker(JavaScriptObject map, double lat, double lng,String text)/*-{
	var marker = new $wnd.google.maps.Marker({
      	position: new $wnd.google.maps.LatLng(lat,lng), 
      	map: map,
      	title:text});
      	return marker;
	}-*/;

	/**
	 * Remove all markers on the map.
	 */
	public void clearMarkers(){
		for (JavaScriptObject m : markers){
			clearMarker (m);
		}
		markers.clear();
	}
	
	/**
	 * Clear a marker.
	 * @param m The {@link JavaScriptObject} corresponding to the marker to remove.
	 */
	private static native void clearMarker ( JavaScriptObject m)/*-{
		m.setMap(null);
	}-*/; 
	
}
