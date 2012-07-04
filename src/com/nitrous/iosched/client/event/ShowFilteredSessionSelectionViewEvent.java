package com.nitrous.iosched.client.event;

import java.util.Date;

import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.history.Bookmark;

/**
 * Display a list of sessions, filtered by time and event type (keynote, session, codelab)
 * and allow the user to select a session to be inserted in to the users agenda.
 * 
 * @author nick
 *
 */
public class ShowFilteredSessionSelectionViewEvent extends AbstractNavigationEvent<ShowFilteredSessionSelectionViewEvent> {
	private EventType eventType;
	private Date startTime;
	
	public ShowFilteredSessionSelectionViewEvent(EventType eventType, Date startTime) {
		this(eventType, startTime, null);
	}
	
	public ShowFilteredSessionSelectionViewEvent(EventType eventType, Date startTime, Bookmark bookmark) {
		super(bookmark);
		this.eventType = eventType;
		this.startTime = startTime;
	}

	public EventType getEventType() {
		return eventType;
	}

	public Date getStartTime() {
		return startTime;
	}

	///// boiler plate
	public static final Type<SimpleEventHandler<ShowFilteredSessionSelectionViewEvent>> TYPE = new Type<SimpleEventHandler<ShowFilteredSessionSelectionViewEvent>>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SimpleEventHandler<ShowFilteredSessionSelectionViewEvent>> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimpleEventHandler<ShowFilteredSessionSelectionViewEvent> handler) {
		handler.handleEvent(this);
	}

}
