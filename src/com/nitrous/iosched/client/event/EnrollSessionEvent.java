package com.nitrous.iosched.client.event;

import com.nitrous.iosched.client.data.EventDataWrapper;

/**
 * Add the specified session to the users agenda
 * @author nick
 *
 */
public class EnrollSessionEvent extends AbstractEventDataWrapperEvent<EnrollSessionEvent> {
	private boolean enroll;
	public EnrollSessionEvent(EventDataWrapper event, boolean enroll) {
		super(event);
		this.enroll = enroll;
	}
	public boolean isEnroll() {
		return enroll;
	}
	
	///// boiler plate
	public static final Type<SimpleEventHandler<EnrollSessionEvent>> TYPE = new Type<SimpleEventHandler<EnrollSessionEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<EnrollSessionEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<EnrollSessionEvent> handler) {
		handler.handleEvent(this);
	}

}
