package com.nitrous.iosched.client.data.json;

/**
 * A wrapper for the session data and timeslots of a particular event type (i.e. sessions, codelab or keynote)
 * @author nick
 *
 */
public class SessionDataWrapper {
	private SessionData data;
	private EventSlotListWrapper eventSlots;
	public SessionDataWrapper(SessionData data) {
		this.data = data;
		this.eventSlots = EventSlotListWrapper.parse(data.getEventSlots());
	}

	public EventSlotListWrapper getEventSlots() {
		return this.eventSlots;
	}
	
	public SessionData getData() {
		return this.data;
	}
}
