package com.nitrous.iosched.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

public class Registry {
	private static Registry INSTANCE;
	
	private EventBus eventBus;
	private Registry() {
		eventBus = new SimpleEventBus();
	}
	
	public static Registry get() {
		if (INSTANCE == null) {
			INSTANCE = new Registry();
		}
		return INSTANCE;
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
}
