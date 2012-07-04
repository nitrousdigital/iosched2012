package com.nitrous.iosched.client.view.home.agenda;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.data.json.EventSlotWrapper;
import com.nitrous.iosched.client.event.EnrollSessionEvent;
import com.nitrous.iosched.client.event.ReloadAndDisplayAgendaEvent;
import com.nitrous.iosched.client.event.SessionEnrolledStateChangeEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class AgendaManager {
	private static final String ENROLLED_EVENT_IDS_COOKIE = "enrolled-event-ids"; 
	private static final String EVENT_ID_SEP_CHAR = ";";
	private static AgendaManager INSTANCE;
	private static final DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("yyyyMMddHHmm");
	
	private AgendaManager() {
	}
	
	public static AgendaManager get() {
		if (INSTANCE == null) {
			INSTANCE = new AgendaManager();
		}
		return INSTANCE;
	}

	/**
	 * Register event handlers
	 */
	public void register() {
		Registry.get().getEventBus().addHandler(EnrollSessionEvent.TYPE, new SimpleEventHandler<EnrollSessionEvent>(){
			@Override
			public void handleEvent(EnrollSessionEvent event) {
				enroll(event.getEvent(), event.isEnroll());
			}
		});
	}
	
	private void clearSlot(String cookieName) {
		String oldEventId = Cookies.getCookie(cookieName);
		if (oldEventId != null) {
			Cookies.removeCookie(cookieName);
			removeEnrolledEventId(oldEventId);
			ConsoleLogger.debug("Cleared event "+oldEventId+" from slot");
			Registry.get().getEventBus().fireEvent(new SessionEnrolledStateChangeEvent(oldEventId, false));
		}
	}
	
	private void enroll(EventDataWrapper wrapper, boolean enroll) {
		String eventId = wrapper.getData().getId();
		String type = wrapper.getSession().getData().getEventType();
		Date startTime = wrapper.getSlot().getStartTime();
		
		String cookieName = getCookieName(type, startTime);
		
		// clear the time slot
		clearSlot(cookieName);
		
		// save event id in time slot
		if (enroll) {			
			// fill the slot with the new event
			Cookies.setCookie(cookieName, eventId);
			// save event id in list of enrolled events
			addEnrolledEventId(eventId);
			
			// notify the view
			Registry.get().getEventBus().fireEvent(new SessionEnrolledStateChangeEvent(eventId, true));
		}
		
		// reload and display the agenda
		Registry.get().getEventBus().fireEvent(new ReloadAndDisplayAgendaEvent());
		
	}
	
	private void addEnrolledEventId(String id) {
		Set<String> ids = getEnrolledEventIds();
		if (ids.add(id)) {
			saveEnrolledEventIds(ids);
		}
	}
	private void removeEnrolledEventId(String id) {
		Set<String> ids = getEnrolledEventIds();
		if (ids.remove(id)) {
			saveEnrolledEventIds(ids);
		}
	}
	
	private void saveEnrolledEventIds(Set<String> ids) {
		if (ids.size() == 0) {			
			Cookies.removeCookie(ENROLLED_EVENT_IDS_COOKIE);
		} else {
			StringBuffer buf = new StringBuffer();
			for (String id : ids) {
				if (buf.length() > 0) {
					buf.append(EVENT_ID_SEP_CHAR);
				}
				buf.append(id);
			}
			Cookies.setCookie(ENROLLED_EVENT_IDS_COOKIE, buf.toString());
		}		
	}
	
	private Set<String> getEnrolledEventIds() {
		String cookie = Cookies.getCookie(ENROLLED_EVENT_IDS_COOKIE);
		Set<String> ids = new HashSet<String>();
		if (cookie != null) {
			if (cookie != null && cookie.trim().length() > 0) {
				String[] parts = cookie.split(EVENT_ID_SEP_CHAR);
				for (String part : parts) {
					ids.add(part);
				}
			}
		}
		return ids;
	}
	
	/**
	 * Determine whether the user is enrolled in the specified event
	 * @param eventId The event id
	 * @param callback the callback to be notified
	 */
	public void isEnrolled(String eventId, AsyncCallback<Boolean> callback) {
		try {
			boolean isEnrolled = getEnrolledEventIds().contains(eventId);
			callback.onSuccess(isEnrolled);
		} catch (Throwable t) {
			callback.onFailure(t);
		}
	}
	
	/**
	 * Load the ID of the event assigned to the specified agenda slot
	 * @param type The event type
	 * @param slotDef The time slot
	 * @param callback The callback to be notified with the ID of the event assigned to the specified slot or null if not found
	 */
	public void getEventId(EventType type, EventSlotWrapper slotDef, AsyncCallback<String> callback) {
		try {
			String cookie = getCookie(type, slotDef);
			callback.onSuccess(cookie);
		} catch (Throwable t) {
			callback.onFailure(new Exception("Failed to load cookie", t));
		}
	}
	
	private static String getCookie(EventType type, EventSlotWrapper slotDef) {
		return Cookies.getCookie(getCookieName(type, slotDef));
	}
	private static String getCookieName(String eventType, Date startTime) {
		StringBuffer buf = new StringBuffer();
		buf.append(eventType);
		buf.append(TIME_FORMAT.format(startTime));
		return buf.toString();
	}
	private static String getCookieName(EventType type, EventSlotWrapper slotDef) {
		return getCookieName(type.getId(), slotDef.getStartTime());
	}
}
