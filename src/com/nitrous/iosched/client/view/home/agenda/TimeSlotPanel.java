package com.nitrous.iosched.client.view.home.agenda;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.data.ConferenceSchedule;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.EventType;
import com.nitrous.iosched.client.data.SessionStore;
import com.nitrous.iosched.client.data.json.EventSlotWrapper;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.ShowFilteredSessionSelectionViewEvent;
import com.nitrous.iosched.client.event.ShowSessionDetailEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.util.Util;
import com.nitrous.iosched.client.view.session.TimeLocationRenderer;

public class TimeSlotPanel implements IsWidget {
	private Grid layout;

	private EventType eventType;
	private EventSlotWrapper slotDef;
	
	// the event that currently occupies this slot or null if empty
	private EventDataWrapper event;
	private boolean isExpired = false;

	private static final DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("h:mmaa");
	private static final int TIME_COL_WIDTH = 55;
	
	private Label slotTitle;
	private Label locationLabel;
	public TimeSlotPanel(EventType type, EventSlotWrapper slotDef) {
		this.eventType = type;
		this.slotDef = slotDef;
	
		StringBuffer timeStr = new StringBuffer();
		timeStr.append(TIME_FORMAT.format(slotDef.getStartTime(), Util.TIMEZONE));
		HorizontalPanel timePanel = new HorizontalPanel();
		timePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		timePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		timePanel.setWidth(TIME_COL_WIDTH+"px");
		timePanel.setHeight("100%");
		Label time = new Label(timeStr.toString().toLowerCase());
		time.setWidth(TIME_COL_WIDTH+"px");
		time.setHeight("100%");
		time.setStylePrimaryName("agenda-session-time-text");
		timePanel.add(time);
		
		VerticalPanel slotDetail = new VerticalPanel();
		slotDetail.setWidth("100%");
		slotDetail.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		slotTitle = new Label();
		slotTitle.setWidth("100%");
		slotTitle.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onTitleClick();
			}
		});
		slotDetail.add(slotTitle);
		
		locationLabel = new Label();
		locationLabel.setWidth("100%");
		locationLabel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onLocationClick();
			}
		});
		slotDetail.add(locationLabel);

		layout = new Grid(1, 2);
		layout.setWidth("100%");
		layout.setStylePrimaryName("agenda-slot-row");
		
		layout.getCellFormatter().setWidth(0,  0, TIME_COL_WIDTH+"px");
		layout.getCellFormatter().setHeight(0,  0, "100%");
		layout.getCellFormatter().setWidth(0,  1, "100%");
		layout.getCellFormatter().setStylePrimaryName(0,  0, "agenda-session-time-cell");
		layout.getCellFormatter().setStylePrimaryName(0,  1, "agenda-session-slot-detail-panel");
		layout.setWidget(0, 0, timePanel);
		layout.setWidget(0, 1, slotDetail);
		
		onRefresh();
	}
	
	private void onTitleClick() {
		if (event == null) {			
			browseSessions();
		} else {
			viewEventDetails();
		}
	}
	private void onLocationClick() {
		if (event == null) {			
			browseSessions();
		} else {
			viewEventDetails();
		}
	}
	
	private void viewEventDetails() {
		if (event == null) {
			return;
		}
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(RootViewChangeEvent.View.SESSION_DETAIL));
		Registry.get().getEventBus().fireEvent(new ShowSessionDetailEvent(event, View.HOME));
	}
	private void browseSessions() {
		Registry.get().getEventBus().fireEvent(new ShowFilteredSessionSelectionViewEvent(eventType, slotDef.getStartTime()));
		Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(RootViewChangeEvent.View.AGENDA_SESSION_SELECTION));
	}
	
	private void onRefresh() {
		AgendaManager.get().getEventId(eventType, slotDef, new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("Failed to load time slot assignment", caught);
				onLoad(null);
			}

			@Override
			public void onSuccess(String result) {
				onLoad(result);
			}
		});
	}

	public void refreshExpired() {
		if (slotDef == null) {
			return;
		}
		
		if (slotDef.getEndTime().getTime() < Util.getCurrentTimeMillis()) {
			if (!isExpired) {
				isExpired = true;
				this.slotTitle.addStyleDependentName("expired");
				this.locationLabel.addStyleDependentName("expired");
			}
		} else {
			if (isExpired) {
				isExpired = false;
				this.slotTitle.removeStyleDependentName("expired");
				this.locationLabel.removeStyleDependentName("expired");
			}
		}
	}
	
	/**
	 * Display the event identified by the specified ID in this slot or mark as empty
	 * @param eventId The ID of the event to display or null to show the slot as available
	 */
	private void onLoad(final String eventId) {
		refreshExpired();
		if (eventId == null) {
			showEmpty();
			return;
		}
		
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){

			@Override
			public void onFailure(Throwable caught) {
				ConsoleLogger.error("TimeSlotPanel - Failed to load schedule");
				showEmpty();
			}

			@Override
			public void onSuccess(ConferenceSchedule result) {
				onLoad(eventId, result);				
			}
			
		}, false);
	}
	
	private void onLoad(String eventId, ConferenceSchedule schedule) {
		EventDataWrapper event = schedule.getEvent(eventId);
		if (event == null) {
			ConsoleLogger.error("TimeSlotPanel - Failed to locate event with id '"+eventId+"'");
			showEmpty();
			return;
		}
		showSession(event);
	}

	private void showSession(EventDataWrapper event) {
		this.event = event;
		this.slotTitle.setText(event.getData().getTitle());
		this.locationLabel.setText(TimeLocationRenderer.renderLocation(event));
		this.slotTitle.setStylePrimaryName("agenda-slot-title-text-used");
		this.locationLabel.setStylePrimaryName("agenda-slot-location-text-used");
	}
	
	private void showEmpty() {
		this.event = null;
		switch (eventType) {
		case CODELAB:
			this.slotTitle.setText("Browse code labs...");
			break;
		case KEYNOTE:
		case SESSION:
			this.slotTitle.setText("Browse sessions...");
			break;
		}
		this.locationLabel.setText("Empty slot");
		this.slotTitle.setStylePrimaryName("agenda-slot-title-text-empty");
		this.locationLabel.setStylePrimaryName("agenda-slot-location-text-empty");
	}
	
	
	public EventType getEventType() {
		return eventType;
	}

	public EventSlotWrapper getSlot() {
		return slotDef;
	}

	public Date getStartTime() {
		return slotDef.getStartTime();
	}

	public Date getEndTime() {
		return slotDef.getEndTime();
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
