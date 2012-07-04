package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An that indicates the identified event enrolled state has changed
 * @author nick
 *
 */
public class SessionEnrolledStateChangeEvent extends GwtEvent<SimpleEventHandler<SessionEnrolledStateChangeEvent>> {
	private boolean isEnrolled;
	private String eventId;
	public SessionEnrolledStateChangeEvent(String eventId, boolean isEnrolled) {
		this.isEnrolled = isEnrolled;
		this.eventId = eventId;
	}
	public String getEventId() {
		return eventId;
	}
	public boolean isEnrolled() {
		return isEnrolled;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<SessionEnrolledStateChangeEvent>> TYPE = new Type<SimpleEventHandler<SessionEnrolledStateChangeEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<SessionEnrolledStateChangeEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<SessionEnrolledStateChangeEvent> handler) {
		handler.handleEvent(this);
	}

}
