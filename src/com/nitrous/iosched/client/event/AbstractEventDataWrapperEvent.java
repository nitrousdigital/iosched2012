package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.nitrous.iosched.client.data.EventDataWrapper;

/**
 * The base class for events that involve an EventDataWrapper
 * @author nick
 *
 */
public abstract class AbstractEventDataWrapperEvent<T extends AbstractEventDataWrapperEvent<?>> extends GwtEvent<SimpleEventHandler<T>> {
	private EventDataWrapper event;
	
	public AbstractEventDataWrapperEvent(EventDataWrapper event) {
		this.event = event;
	}
	
	public EventDataWrapper getEvent() {
		return event;
	}	
}
