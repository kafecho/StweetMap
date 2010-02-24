/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Main GWT class.
 * @author Guillaume Belrose
 */
public class StweetMap implements EntryPoint,StatusMessageViewListener{
	
	private GeolocationService geolocator = new GeolocationService();
	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	
	private SearchInputPanel searchInputPanel;
	private FlowPanel searchResultsPanel;
	private GoogleMapPopupPanel googleMapView = null;
	private GoogleMapPanel		googleMapPanel = null;
	
	///////////////////////////////////////////////////////////////////
	//			UI Layout stuff                                      //
	///////////////////////////////////////////////////////////////////
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad(){
		buildUI();
	}

	
	private void buildUI(){
		RootPanel root = RootPanel.get();

		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName("mainPanel");
		root.add(mainPanel);
	
		FlowPanel title = new FlowPanel();
		title.addStyleName("title");

		title.add( new HTML("<span id='applicationName'>sTweetMap</span><span id='applicationDescription'>Put tweets on the map</span>"));
		
		searchInputPanel = new SearchInputPanel();
		searchResultsPanel = new FlowPanel();
		searchResultsPanel.addStyleName("searchResultsPanel");

		searchInputPanel.getQuery().addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent event) {
				if ( event.getCharCode() == KeyCodes.KEY_ENTER){
					if ( ! searchInputPanel.getQuery().getText().isEmpty()){
						doSearch();
					}
				}
			}});
		
		searchInputPanel.getSearch().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				doSearch();
			}});
	
		mainPanel.add(title);
		
		TabPanel tabPanel = new TabPanel();
		mainPanel.add(searchInputPanel);
		mainPanel.add(tabPanel);
		
		FlowPanel bottomPanel = new FlowPanel();
		
		
		HTML stweetMapOnGitHub = new HTML("<a target = '_blank' href='http://github.com/kafecho/StweetMap'>Get the code from GitHub</a>");
		stweetMapOnGitHub.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(bottomPanel);
		bottomPanel.add(stweetMapOnGitHub);
		bottomPanel.addStyleName("bottomPanel");
		
		tabPanel.setWidth("100%");

		googleMapView = new GoogleMapPopupPanel(400,400,8);
		
		FlowPanel googleMapPanelContainer = new FlowPanel();
		tabPanel.add(googleMapPanelContainer,"map");
		tabPanel.add(searchResultsPanel,"tweets");
		
		googleMapPanel = new GoogleMapPanel(googleMapPanelContainer,800,400,2);
		tabPanel.selectTab(0);
	}

	///////////////////////////////////////////////////////////////////
	//			Search stuff                                         //
	///////////////////////////////////////////////////////////////////
	
	protected void doSearch() {
		searchResultsPanel.clear();
		googleMapPanel.clearMarkers();
		String query = searchInputPanel.getQuery().getText();
		searchInputPanel.getBusy().setVisible(true);
		
		String encodedQuery = URL.encodeComponent(query);
		GWT.log("Encoded query: " + encodedQuery);
		
		JSONPUtility.get("http://search.twitter.com/search.json?q=" + encodedQuery + "&rpp=" + Config.returnsPerSearch + "&callback=", new JSONPRequestHandler(){
		public void onRequestComplete(JavaScriptObject obj) {
			searchInputPanel.getBusy().setVisible(false);
			JSONObject json = new JSONObject(obj);
			JSONArray posts = (JSONArray) json.get("results");
			for ( int i = 0 ; i < posts.size() ; i ++){
				JSONObject post = (JSONObject) posts.get(i);
				StatusMessage statusMessage = new StatusMessage(post);
				displayStatusMessage(statusMessage);
				if (Config.isGeolocationEnabled)
					checkLocation( statusMessage );
			}
		}});
	}

	private void displayStatusMessage(StatusMessage statusMessage){
		StatusMessageView view = new StatusMessageView( statusMessage );
		statusMessage.setListener ( view);
		view.setListener(this);
		searchResultsPanel.add(view);
	}
	
	///////////////////////////////////////////////////////////////////
	//			Geolocation stuff                                    //
	///////////////////////////////////////////////////////////////////

	private void checkLocation(final StatusMessage tweet){
		GWT.log("Checking location...",null);
		twitterService.getUserInfo(tweet.getUser(), new AsyncCallback<String>(){
			public void onFailure(Throwable caught){
				System.out.println("Error fetching information.");
				GWT.log("Error when fetching user information.", caught);
			}
			public void onSuccess(String result){
				System.out.println(result);
				GWT.log("User information: " + result,null);
				JSONObject obj = (JSONObject) JSONParser.parse(result);
				JSONValue value = obj.get("location");
				if (value != null && value instanceof JSONString){
					tweet.setLocation(((JSONString) value).stringValue());
					geolocate(tweet);
				}
			}});
	}

	private void geolocate(final StatusMessage statusMessage){
		final String where = statusMessage.getLocation();
		
		if (where!=null && !where.isEmpty()){

			if (where.startsWith("iPhone: ")){
				parseIPhoneLocation(statusMessage);
			}else{
			geolocator.lookup(where, new GeolocationRequestHandler(){
				public void onRequestComplete(JavaScriptObject result){
					JSONObject json = new JSONObject(result);
					GWT.log("Parsing "  + json,null);
					updateLatLong(statusMessage,json);
					GWT.log("Geolocated tweet: " + where + ": " + statusMessage.getLat() + "," + statusMessage.getLng(),null);
				
					if (statusMessage.getLat() != null){
						googleMapPanel.createMarker(statusMessage.getLat(),statusMessage.getLng(),statusMessage.getUser() + ": " + statusMessage.getText());
					}
				}

				@Override
				public void onRequestFailed() {
					GWT.log("Unable to geolocate " + statusMessage.getLocation(), null);
					// TODO Auto-generated method stub
				}});
			}
		}
	}

	
	/**
	 * Some iPhone users specify their location with (lat, long) info in their profile.
	 */
	private void parseIPhoneLocation(StatusMessage statusMessage){
		String latLngString = statusMessage.getLocation().split("iPhone: ")[1];
		String[] tokens = latLngString.split(",");
		statusMessage.setLatLng(Double.valueOf(tokens[0]),Double.valueOf(tokens[1]));
		GWT.log("iPhone located tweet: " + statusMessage.getLat() + "," + statusMessage.getLng(),null);
		googleMapPanel.createMarker(statusMessage.getLat(),statusMessage.getLng(),statusMessage.getUser() + ": " + statusMessage.getText());
	}

	
	private void updateLatLong(StatusMessage statusMessage,JSONObject json){
		Set<String> keys = json.keySet();
		for (String key : keys){
			JSONObject entry = (JSONObject) json.get(key);
			JSONObject geometry = (JSONObject) entry.get("geometry");
			JSONObject location = (JSONObject) geometry.get("location");
			String[] locationInfo = new String[2];
			locationInfo = location.keySet().toArray(locationInfo);
			for ( String s : locationInfo){
				GWT.log(s);
			}

			if (locationInfo.length >= 2){
				JSONNumber latitude = (JSONNumber) location.get(locationInfo[0]);
				JSONNumber longitude = (JSONNumber) location.get(locationInfo[1]);
				statusMessage.setLatLng(latitude.doubleValue(),longitude.doubleValue());
			}
		}
	}

	@Override
	public void onShowLocationSelected(StatusMessageView view) {
		googleMapView.setCenter(view.getTweet().getLat(),view.getTweet().getLng(),8, view.getTweet().getUser());
		googleMapView.setVisible(true);
		googleMapView.setModal(true);
		googleMapView.center();
	}
}
