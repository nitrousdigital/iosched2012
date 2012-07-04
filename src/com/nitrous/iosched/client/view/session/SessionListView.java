package com.nitrous.iosched.client.view.session;

import java.util.TreeSet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.ShowSessionDetailEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.event.TrackSelectionEvent;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.model.SessionTrack;

public class SessionListView implements IsWidget {
	private SessionListComponent listComponent;
	
	private ConferenceSchedule conferenceSchedule;
	private SessionTrack track;

	public static final String SESSION_TRACK_HISTORY_KEY = "track";
	
	public SessionListView() {
		listComponent = new SessionListComponent();
		listComponent.setSessionSelectionListener(new SessionSelectionListener(){
			@Override
			public void onSessionSelected(EventDataWrapper event) {
				Registry.get().getEventBus().fireEvent(new ShowSessionDetailEvent(event, RootViewChangeEvent.View.SESSION_LIST));
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_DETAIL));
			}
		});
		
		Registry.get().getEventBus().addHandler(TrackSelectionEvent.TYPE, new SimpleEventHandler<TrackSelectionEvent>(){
			@Override
			public void handleEvent(TrackSelectionEvent event) {
				setTrack(event.getTrack());
			}
		});
		
		loadSchedule();
	}
	
	public void onRefresh() {
		loadSchedule();
	}
	
	public Bookmark saveState(Bookmark bookmark) {
		if (track != null) {
			bookmark.addStateToken(SESSION_TRACK_HISTORY_KEY, track.getHistoryToken());
		}
		return bookmark;
	}
	
	public void restoreState(Bookmark bookmark) {
		String track = bookmark.getStateValue(SESSION_TRACK_HISTORY_KEY);
		SessionTrack sessionTrack = SessionTrack.parseHistoryToken(track);
		Registry.get().getEventBus().fireEvent(new TrackSelectionEvent(sessionTrack));
	}
	
	public void setTrack(SessionTrack track) {
		if ((this.track == null && track != null)
				|| (this.track != null && (!this.track.equals(track)))) {
			this.track = track;
			repaint();
		}
	}
	
	private void loadSchedule() {
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
				setSchedule(result);
			}
			
		}, false);
	}
	
	private void showError(String error) {
		listComponent.showError(error);
	}
	
	/**
	 * Replace the current schedule data and update the view
	 * @param schedule The new schedule data
	 */
	private void setSchedule(ConferenceSchedule schedule) {		
		this.conferenceSchedule = schedule;
		repaint();
	}
	
	private void repaint() {
		TreeSet<EventDataWrapper> events = conferenceSchedule != null ? conferenceSchedule.getAllEvents(track) : null;
		listComponent.showEvents(events);
	}

	@Override
	public Widget asWidget() {
		return listComponent.asWidget();
	}
}
