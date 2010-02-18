/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Wraps a {@link GoogleMapPanel} around a popup panel.
 * @author guillaume
 *
 */
public class GoogleMapPopupPanel extends PopupPanel {

	private GoogleMapPanel map = null;
	
	/**
	 * Construct a popup
	 * @param width width of the popup.
	 * @param height height of the popup.
	 * @param zoom initial zoom level.
	 */
	public GoogleMapPopupPanel(int width, int height, int zoom){
		addStyleName("googleMapPopupPanel");

		FlowPanel container = new FlowPanel();
		add(container);

		Button closeButton = new Button("");
		closeButton.addStyleName("closeButton");
		container.add(closeButton);
	
		show();
		setVisible(false);
		setModal(false);
		setAnimationEnabled(true);
		setAutoHideEnabled(false);
		
		map = new GoogleMapPanel(container,width,height, zoom);
		
		closeButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				setModal(false);
				setVisible(false);
		}});
	}
	
	/**
	 * Center the map and add a text marker at a given location.
	 * @param lat Latitude of map center.
	 * @param lng Longitude of map center.
	 * @param zoom Zoom level.
	 * @param text Marker's text.
	 */
	public void setCenter(double lat, double lng, int zoom, String text){
		map.setCenter(lat, lng, zoom, text);
	}
}
