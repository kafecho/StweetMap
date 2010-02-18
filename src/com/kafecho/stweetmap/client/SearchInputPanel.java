package com.kafecho.stweetmap.client;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class SearchInputPanel extends Composite {

	/**
	 * Listens for keyboard events on the query field to enable or disable the search button.
	 * @author Guillaume Belrose
	 */
	public class KeyboardEventHandler implements KeyUpHandler,KeyDownHandler,KeyPressHandler{
		private void checkQuery(){
			boolean enabled = ! query.getText().isEmpty();
			search.setEnabled(enabled);
		}
		public void onKeyUp(KeyUpEvent event){ checkQuery();}
		public void onKeyDown(KeyDownEvent event) { checkQuery();}
		public void onKeyPress(KeyPressEvent event){checkQuery();}
	}
	
	private TextBox query = null;
	private Button search = null;
	private Image busy = null;
	private Label heading = null;
	
	public Image getBusy() {
		return busy;
	}

	public TextBox getQuery() {
		return query;
	}

	public Button getSearch() {
		return search;
	}

	public SearchInputPanel(){
		
		FlowPanel inputPanel = new FlowPanel();
		
		inputPanel.addStyleName("inputPanel");

		query = new TextBox();
		query.addStyleName("query");

		search = new Button("Search");
		search.addStyleName("search");
		search.setTitle("search Twitter");
		search.setEnabled(false);
	
		busy = new Image("ajax-loader.gif");
		busy.addStyleName("busy");
		busy.setVisible(false);
		
		heading = new Label("Where do they tweet about");
		heading.addStyleName("heading");
		
		inputPanel.add(heading);
		inputPanel.add(query);
		inputPanel.add(search);
		inputPanel.add(busy);

		KeyboardEventHandler keh = new KeyboardEventHandler();
		
		query.addKeyDownHandler(keh);
		query.addKeyUpHandler(keh);
		query.addKeyPressHandler(keh);

		initWidget(inputPanel);
	}
}
