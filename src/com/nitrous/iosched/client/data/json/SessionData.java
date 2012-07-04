package com.nitrous.iosched.client.data.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


/**
 * The SessionData for a particular event type: keynote, sessions or codelab
 * @author nick
 *
 */
public final class SessionData extends JavaScriptObject {
	protected SessionData() {
	}
	
	/**
	 * @return The event slot list for each date
	 */
	public native String getEventSlots() /*-{
		return this.event_timeslots;
	}-*/;
	
	/**
	 * @return The sessions and their times and locations
	 */
	public native JsArray<EventData> getEvents() /*-{
		return this.events;
	}-*/;
	
	/**
	 * @return codelab, session or keynote
	 */
	public native String getEventType() /*-{
		return this.event_type;
	}-*/;
}
