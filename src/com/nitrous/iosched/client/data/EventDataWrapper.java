package com.nitrous.iosched.client.data;

import com.nitrous.iosched.client.data.json.EventData;
import com.nitrous.iosched.client.data.json.SessionDataWrapper;

/**
 * A wrapper for EventData that holds the parsed TimeSlot
 * @author nick
 *
 */
public class EventDataWrapper {
	private EventData data;
	private TimeSlot slot;
	private SessionDataWrapper session;
	public EventDataWrapper(EventData data, SessionDataWrapper session) {
		this.data = data;
		this.slot = TimeSlot.parse(data);
		this.session = session;
	}
	public SessionDataWrapper getSession() {
		return this.session;
	}
	public EventData getData() {
		return data;
	}
	public TimeSlot getSlot() {
		return slot;
	}
	
	
}
