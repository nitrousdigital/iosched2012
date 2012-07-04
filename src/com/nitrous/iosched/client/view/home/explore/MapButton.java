package com.nitrous.iosched.client.view.home.explore;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;

public class MapButton implements IsWidget {
	private Button button;
	public MapButton() {
		button = new Button("Map");
		button.setStyleName("map-button");
		button.setWidth("100%");
		button.setHeight("48px");
		button.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.MAP));				
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return button;
	}
	
}
