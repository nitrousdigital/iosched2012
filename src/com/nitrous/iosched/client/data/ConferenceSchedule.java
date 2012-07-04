package com.nitrous.iosched.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.nitrous.iosched.client.data.json.EventData;
import com.nitrous.iosched.client.data.json.SessionDataWrapper;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.view.session.EventComparator;

/**
 * The schedule for the entire duration of the conference
 * @author nick
 *
 */
public class ConferenceSchedule {
	// Compares dates by year, month, day, hour, minute and second.
	private static final EventSlotDateComparator DATETIME_COMPARATOR = new EventSlotDateComparator();
	
	private ArrayList<SessionDataWrapper> sessionData;
	private TreeSet<Date> conferenceDates;
	
	private Map<String, EventDataWrapper> eventsById;
	private TreeSet<EventDataWrapper> allEvents;
	private Map<SessionTrack, TreeSet<EventDataWrapper>> eventsByTrack;
	private TreeMap<Date, TreeSet<EventDataWrapper>> eventsByDate;
	
	public ConferenceSchedule(ArrayList<SessionDataWrapper> sessionData) {
		this.sessionData = sessionData;
		this.conferenceDates = new TreeSet<Date>();
		
		this.eventsById = new HashMap<String, EventDataWrapper>();
		this.allEvents = new TreeSet<EventDataWrapper>(new EventComparator());
		this.eventsByTrack = new HashMap<SessionTrack, TreeSet<EventDataWrapper>>();
		this.eventsByDate = new TreeMap<Date, TreeSet<EventDataWrapper>>();
		
		for (SessionDataWrapper session : sessionData) {
			JsArray<EventData> events = session.getData().getEvents();
			for (int eventIdx = 0, eventCount = events.length() ; eventIdx < eventCount; eventIdx++) {
				EventData event = events.get(eventIdx);
				EventDataWrapper eventDataWrapper = new EventDataWrapper(event, session);
				
				// cache by date
				Date date = eventDataWrapper.getSlot().getStartDate();
				TreeSet<EventDataWrapper> byDate = eventsByDate.get(date);
				if (byDate == null) {
					byDate = new TreeSet<EventDataWrapper>(new EventComparator());
					eventsByDate.put(date, byDate);
				}
				byDate.add(eventDataWrapper);
				
				
				this.conferenceDates.add(date);
				this.eventsById.put(event.getId(), eventDataWrapper);
				this.allEvents.add(eventDataWrapper);
				
				// add to lookup by track
				JsArrayString trackNames = event.getTracks();
				if (trackNames != null) {
					for (int trackIdx = 0, trackCnt = trackNames.length(); trackIdx < trackCnt; trackIdx++) {
						String trackName = trackNames.get(trackIdx);
						SessionTrack track = SessionTrack.parseTrackName(trackName);
						if (track == null) {
							ConsoleLogger.error("Unrecognized track name: '"+trackName+"'");
							continue;
						}
						TreeSet<EventDataWrapper> trackSessions = eventsByTrack.get(track);
						if (trackSessions == null) {
							trackSessions = new TreeSet<EventDataWrapper>(new EventComparator());
							eventsByTrack.put(track, trackSessions);
						}
						trackSessions.add(eventDataWrapper);							
					}
				}
			}
		}
	}
	
	public TreeSet<Date> getDates() {
		return conferenceDates;
	}
	
	public TreeSet<EventDataWrapper> getAllEvents() {
		return allEvents;
	}
	
	/**
	 * Retrieve events filtered by track
	 * @param filter The filter to restrict returned events. Null and SessionTrack.All returns all events
	 * @return Events filtered by the specified track. Null and SessionTrack.All returns all events
	 */
	public TreeSet<EventDataWrapper> getAllEvents(SessionTrack filter) {
		if (filter == null || SessionTrack.All.equals(filter)) {
			return getAllEvents();
		} else {
			return eventsByTrack.get(filter);
		}
	}

	/**
	 * Find an event by id
	 * @param id
	 * @return The id of the event
	 */
	public EventDataWrapper getEvent(String id) {
		if (id == null) {
			return null;
		}
		return eventsById.get(id);
	}
	
	/**
	 * Retrieve the sorted events starting at the specified time and of the specified event type (codelab, session or keynote)
	 * @param startTime The start time
	 * @param eventType the event type filter
	 * @return The sorted events starting at the specified time and of the specified event type (codelab, session or keynote)
	 */
	@SuppressWarnings("deprecation")
	public TreeSet<EventDataWrapper> getEvents(Date startTime, EventType eventType) {
		Date dateOnly = new Date(startTime.getTime());
		dateOnly.setHours(0);
		dateOnly.setMinutes(0);
		dateOnly.setSeconds(0);
		TreeSet<EventDataWrapper> sameDayEvents = eventsByDate.get(dateOnly);
		TreeSet<EventDataWrapper> filtered = new TreeSet<EventDataWrapper>(new EventComparator());
		if (sameDayEvents != null) {
			for (EventDataWrapper eventDataWrapper: sameDayEvents) {
				if (acceptStartTime(startTime, eventDataWrapper) && acceptEventType(eventType, eventDataWrapper)) {
					filtered.add(eventDataWrapper);
				}
			}
		}
		return filtered;
	}
	
	private static boolean acceptStartTime(Date startTime, EventDataWrapper eventDataWrapper) {
		return DATETIME_COMPARATOR.compare(startTime, eventDataWrapper.getSlot().getStartTime()) == 0;
	}
	
	private static boolean acceptEventType(EventType eventType, EventDataWrapper event) {
		boolean eventTypeMatch = false;
		String evtType = event.getSession().getData().getEventType();
		EventType curEventType = EventType.parse(evtType);
		if (curEventType == null) {
			ConsoleLogger.error("Unrecognized event type: '"+evtType+"'");
		} else {
			if (EventType.SESSION.equals(eventType)) {
				if (EventType.SESSION.equals(curEventType) || EventType.KEYNOTE.equals(curEventType)) {
					eventTypeMatch = true;
				}
			} else if (EventType.CODELAB.equals(eventType)) {
				eventTypeMatch = EventType.CODELAB.equals(curEventType);
			} else if (EventType.KEYNOTE.equals(eventType)) {
				eventTypeMatch = EventType.KEYNOTE.equals(curEventType);
			}
		}
		return eventTypeMatch;
	}
	
	public ArrayList<SessionDataWrapper> getSessionData() {
		return sessionData;
	}
		
}
