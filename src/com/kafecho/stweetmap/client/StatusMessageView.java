/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/
package com.kafecho.stweetmap.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;

/**
 * Graphical representation of a {@link StatusMessage}
 * @author Guillaume Belrose
 */
public class StatusMessageView extends Composite implements StatusMessageListener, EventListener{
	
	private HorizontalPanel statusMessagePanel = null;
	private HTML userPhoto = null;
	private FlowPanel textContainer = null;
	//private HorizontalPanel textContainer = null;

	private PushButton userLocation= null;
	private HTML userName = null;
	private HTML statusMessageText = null;
	
	private StatusMessage statusMessage = null;
	private StatusMessageViewListener listener = null;
	
	public StatusMessageView(StatusMessage _statusMessage){
		statusMessage = _statusMessage;
		
		statusMessagePanel = new HorizontalPanel();
		
		statusMessagePanel.addStyleName("tweetPanel");
		statusMessagePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

		userPhoto = new HTML("<a href='http://www.twitter.com/"+statusMessage.getUser()+"' target='_blank'><img src='"+statusMessage.getProfileImageURL()+"'></a>");
		userPhoto.addStyleName("tweetViewUserPhoto");

		statusMessagePanel.add(userPhoto);

		textContainer = new FlowPanel();
		//textContainer = new HorizontalPanel();
		textContainer.addStyleName("textContainer");
		
		statusMessagePanel.add(textContainer);
		statusMessagePanel.setCellWidth(textContainer,"100%");
		
		userName = new HTML("<a href='http://www.twitter.com/"+statusMessage.getUser()+"' target='_blank'>"+statusMessage.getUser() + "</a>");
		userName.addStyleName("tweetViewUsername");
		
		userLocation = new PushButton("");
		userLocation.setTitle("show " + statusMessage.getUser() + " on the map");
		userLocation.addStyleName("tweetViewUserLocation");
		userLocation.setVisible(false);
		
		statusMessageText = new HTML(statusMessage.getText());
		statusMessageText.addStyleName("tweetViewText");
		
		final StatusMessageView tv = this;
		
		userLocation.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if (listener != null){
					GWT.log("Showing the map",null);
					listener.onShowLocationSelected(tv);
				}
			}});
		
		
		textContainer.add(userName);
		textContainer.add(userLocation);
		textContainer.add(statusMessageText);
		
		//textContainer.setCellWidth(tweetText,"100%");
		
		initWidget(statusMessagePanel);
		
		sinkEvents(Event.ONMOUSEOVER);
		sinkEvents(Event.ONMOUSEOUT);
	}

	@Override
	public void onLatLngSet(){
		userLocation.setText("(from " + statusMessage.getLocation()+ ")");
		userLocation.setVisible(true);
		//tweetText.setWidth("100%");
	}

	@Override
	public void onLocationSet() {
	}

	public void setListener(StatusMessageViewListener _listener){
		listener = _listener;
	}

	public StatusMessage getTweet() {
		return statusMessage;
	}
	
	public void onBrowserEvent(Event e){
		switch(DOM.eventGetType(e)) {
        case Event.ONMOUSEOVER:
        	statusMessagePanel.addStyleDependentName("hover");
        	break;
        case Event.ONMOUSEOUT:
        	statusMessagePanel.removeStyleDependentName("hover");
        		break;

		}
		super.onBrowserEvent(e);
	}
}
