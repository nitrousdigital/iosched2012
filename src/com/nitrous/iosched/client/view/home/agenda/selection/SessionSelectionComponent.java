package com.nitrous.iosched.client.view.home.agenda.selection;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.EventSlotDateComparator;
import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.ShowFilteredSessionSelectionViewEvent;
import com.nitrous.iosched.client.event.ShowSessionDetailEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.event.TrackSelectionEvent;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.view.session.SessionListComponent;
import com.nitrous.iosched.client.view.session.SessionSelectionListener;

/**
 * The component that allows the user to select a session from a list filtered by event type and start time
 * @author nick
 *
 */
public class SessionSelectionComponent implements IsWidget {
	// Compares dates by year, month, day, hour, minute and second.
	private static final EventSlotDateComparator DATETIME_COMPARATOR = new EventSlotDateComparator();
	
	private SessionListComponent listComponent;
	
	private EventType eventType;
	private Date startTime;
	
	public SessionSelectionComponent() {
		listComponent = new SessionListComponent();
		listComponent.setSessionSelectionListener(new SessionSelectionListener(){
			@Override
			public void onSessionSelected(EventDataWrapper event) {
				Registry.get().getEventBus().fireEvent(new TrackSelectionEvent(SessionTrack.All));
				Registry.get().getEventBus().fireEvent(new ShowSessionDetailEvent(event, RootViewChangeEvent.View.AGENDA_SESSION_SELECTION));
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_DETAIL));
			}
		});
		Registry.get().getEventBus().addHandler(ShowFilteredSessionSelectionViewEvent.TYPE, new SimpleEventHandler<ShowFilteredSessionSelectionViewEvent>(){

			@Override
			public void handleEvent(ShowFilteredSessionSelectionViewEvent event) {
				setFilter(event.getEventType(), event.getStartTime());				
			}			
		});
	}

	public void restoreState(Bookmark bookmark) {
		String type = bookmark.getStateValue("eventtype");
		String start = bookmark.getStateValue("start");
		EventType eventType = EventType.parse(type);
		if (eventType != null && start != null && start.length() > 0) {
			try {
				Date time = BOOKMARK_TIME_FORMAT.parse(start);
				Registry.get().getEventBus().fireEvent(new ShowFilteredSessionSelectionViewEvent(eventType, time, bookmark));
			} catch (Throwable ex) {
				ConsoleLogger.error("failed to parse start time from bookmark '"+start+"'");
			}
		}
	}
	
	private static final DateTimeFormat BOOKMARK_TIME_FORMAT = DateTimeFormat.getFormat("yyyyMMddHHmm");
	public Bookmark saveState(Bookmark bookmark) {
		if (eventType != null) {
			bookmark.addStateToken("eventtype", eventType.getId());
		}
		if (startTime != null) {
			bookmark.addStateToken("start", BOOKMARK_TIME_FORMAT.format(startTime));
		}
		return bookmark;
	}
	
	private void setFilter(EventType eventType, Date startTime) {
		if (equals(eventType, this.eventType) 
				&& equals(startTime, this.startTime)) {
			return;
		}
		this.eventType = eventType;
		this.startTime = startTime;
		onRefresh(false);
	}
	
	public void onRefresh() {
		onRefresh(true);
	}
	
	private void showError(String error) {
		listComponent.showError(error);
	}
	
	private void onRefresh(boolean reloadSchedule) {
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){

			@Override
			public void onFailure(Throwable caught) {
				String err = null;
				if (caught != null) {
					err = caught.getMessage();
				}
				if (err == null) {
					err = "Failed to load session data";
				}
				showError(err);				
			}

			@Override
			public void onSuccess(ConferenceSchedule result) {
				onScheduleLoaded(result);				
			}			
		}, reloadSchedule);
	}
	
	private void onScheduleLoaded(ConferenceSchedule schedule) {
		listComponent.showEvents(schedule != null 
				? schedule.getEvents(startTime, eventType) 
				: null);
	}
	
	@Override
	public Widget asWidget() {
		return listComponent.asWidget();
	}
	
	private static boolean equals(EventType eventType0, EventType eventType1) {
		if (eventType0 == null && eventType1 == null) {
			return true;
		} else if (eventType0 != null) {
			return eventType0.equals(eventType1);
		} else {
			return eventType1.equals(eventType0);
		}			
	}
	private static boolean equals(Date time0, Date time1) {
		return DATETIME_COMPARATOR.compare(time0,  time1) == 0;
	}
}
