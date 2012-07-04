package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.event.ShowSessionDetailEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.event.TrackSelectionEvent;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.view.session.SessionListView;

public class RootSessionDetailView implements IsWidget {
	private static final String EVENT_ID_HISTORY_KEY = "id";
	
	private DockLayoutPanel layout;
	private SessionDetailLayoutPanel detail;
	private EventDataWrapper event;
	private SessionTrack track;
	
	public RootSessionDetailView() {
		layout = new DockLayoutPanel(Unit.PX);
		
		SessionDetailTopToolbar topToolbar = new SessionDetailTopToolbar();
		layout.addNorth(topToolbar, SessionDetailTopToolbar.HEIGHT);
		
		detail = new SessionDetailLayoutPanel();
		layout.add(detail);
		
		Registry.get().getEventBus().addHandler(ShowSessionDetailEvent.TYPE, new SimpleEventHandler<ShowSessionDetailEvent>(){
			@Override
			public void handleEvent(ShowSessionDetailEvent event) {
				showDetails(event.getEvent());
			}
		});
		
		Registry.get().getEventBus().addHandler(TrackSelectionEvent.TYPE, new SimpleEventHandler<TrackSelectionEvent>(){
			@Override
			public void handleEvent(TrackSelectionEvent event) {
				setTrack(event.getTrack());
			}
		});
		
		Registry.get().getEventBus().addHandler(RefreshViewEvent.TYPE, new SimpleEventHandler<RefreshViewEvent>(){
			@Override
			public void handleEvent(RefreshViewEvent event) {
				if (RefreshViewEvent.View.SESSION_DETAIL.equals(event.getView())) {
					onRefresh();
				}
			}			
		});		
		
	}
	
	private void onRefresh() {
		if (event == null) {
			return;
		}
		final String id = event.getData().getId();
				
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){
			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("Failed to load schedule", caught);
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST));
			}

			@Override
			public void onSuccess(ConferenceSchedule schedule) {
				EventDataWrapper event = schedule.getEvent(id);
				if (event == null) {
					Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST));
					return;
				}
				Registry.get().getEventBus().fireEvent(new ShowSessionDetailEvent(event, null));
			}
		}, true);
	}
	
	private void setTrack(SessionTrack track) {
		this.track = track;
	}
	
	public Bookmark saveState(Bookmark bookmark) {
		if (event != null) {
			bookmark.addStateToken(EVENT_ID_HISTORY_KEY, event.getData().getId());
		}
		if (track != null) {
			bookmark.addStateToken(SessionListView.SESSION_TRACK_HISTORY_KEY, track.getHistoryToken());
		}
		return bookmark;
	}
	
	public void restoreState(final Bookmark bookmark) {
		final String id = bookmark.getStateValue(EVENT_ID_HISTORY_KEY);
		if (id == null) {
			// no id in bookmark so return to session list
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST, bookmark));
			return;
		}
		
		// find the session details
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){
			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("Failed to load schedule", caught);
				Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST, bookmark));
			}

			@Override
			public void onSuccess(ConferenceSchedule result) {
				restoreState(id, bookmark, result);
			}
		}, false);
	}
	
	private void restoreState(String eventId, Bookmark bookmark, ConferenceSchedule schedule) {
		EventDataWrapper event = schedule.getEvent(eventId);
		if (event == null) {
			ConsoleLogger.debug("Failed to find event by id "+eventId);
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST, bookmark));
		} else {
			// restore track selection
			String trackId = bookmark.getStateValue(SessionListView.SESSION_TRACK_HISTORY_KEY);
			SessionTrack track = SessionTrack.parseHistoryToken(trackId);
			Registry.get().getEventBus().fireEvent(new TrackSelectionEvent(track));
			
			// restore details view
			//TODO: maybe persist parent view so it can be restored. For now we always assume the parent view is the session list when loading from a bookmark.
			Registry.get().getEventBus().fireEvent(new ShowSessionDetailEvent(event, View.SESSION_LIST));
		}
	}
	
	private void showDetails(EventDataWrapper event) {
		this.event = event;
		detail.showDetails(event);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
