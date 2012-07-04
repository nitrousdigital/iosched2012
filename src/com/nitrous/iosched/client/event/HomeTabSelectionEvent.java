package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Select a specified tab in the home tabs
 * @author nick
 *
 */
public class HomeTabSelectionEvent extends GwtEvent<SimpleEventHandler<HomeTabSelectionEvent>> {
	public static enum Tab {
		EXPLORE,
		MY_AGENDA
	}
	private Tab tab;
	public HomeTabSelectionEvent(Tab tab) {
		this.tab = tab;
	}
	
	public Tab getTab() {
		return tab;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<HomeTabSelectionEvent>> TYPE = new Type<SimpleEventHandler<HomeTabSelectionEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<HomeTabSelectionEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<HomeTabSelectionEvent> handler) {
		handler.handleEvent(this);
	}

}
