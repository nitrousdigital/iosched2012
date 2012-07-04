package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.EventHandler;

public abstract class SimpleEventHandler<EventType> implements EventHandler {
	public abstract void handleEvent(EventType event);
}
