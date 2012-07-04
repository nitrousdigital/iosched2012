package com.nitrous.iosched.client.view.session;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.util.Util;

public class SessionRow implements IsWidget, ClickHandler {
	private VerticalPanel layout;
	private EventDataWrapper event;
	
	private SessionSelectionListener listener = null;
		
	private Label sessionTitleLabel;
	private HorizontalPanel titleRow;
	private HTML sessionTimeLabel;
	private HorizontalPanel timeLocRow;
	private boolean expired = false;
	
	public SessionRow(EventDataWrapper event) {
		this.event = event;
		
		// title
		sessionTitleLabel = new Label(event.getData().getTitle());
		sessionTitleLabel.setStylePrimaryName("session-list-entry-title-text");
		sessionTitleLabel.setWidth("100%");
		sessionTitleLabel.addClickHandler(this);
		
		titleRow = new HorizontalPanel();
		titleRow.setStylePrimaryName("session-list-entry-title-row");
		titleRow.setWidth("100%");
		titleRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		titleRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		titleRow.add(sessionTitleLabel);		
		
		// time & location
		String timeLoc = TimeLocationRenderer.renderDateTimeLocation(event);
		sessionTimeLabel = new HTML(timeLoc);
		sessionTimeLabel.setStylePrimaryName("session-list-entry-time-text");
		sessionTimeLabel.addClickHandler(this);
		timeLocRow = new HorizontalPanel();
		timeLocRow.setWidth("100%");
		timeLocRow.setStylePrimaryName("session-list-entry-location-row");
		timeLocRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		timeLocRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		timeLocRow.add(sessionTimeLabel);
		
		layout = new VerticalPanel();
		layout.setStylePrimaryName("session-list-entry");
		layout.setWidth("100%");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.add(titleRow);
		layout.add(sessionTimeLabel);	
		
		refreshExpired();
	}
	
	public void refreshExpired() {
		if (event == null) {
			return;
		}
		
		if (event.getSlot().getEndTime().getTime() < Util.getCurrentTimeMillis()) {
			if (!expired) {
				if (ConsoleLogger.isDebugEnabled()) {
					ConsoleLogger.debug(
							"Expired session '"+event.getData().getTitle()
							+"' since end time "+Util.DATE_TIME_FORMAT.format(event.getSlot().getEndTime())
							+" < " + Util.DATE_TIME_FORMAT.format(new Date(Util.getCurrentTimeMillis())));
				}
				expired = true;
				sessionTitleLabel.addStyleDependentName("expired");
				sessionTimeLabel.addStyleDependentName("expired");
				titleRow.addStyleDependentName("expired");
				timeLocRow.addStyleDependentName("expired");
				layout.addStyleDependentName("expired");
			}
		} else {
			if (expired) {
				expired = false;
				sessionTitleLabel.removeStyleDependentName("expired");
				sessionTimeLabel.removeStyleDependentName("expired");
				titleRow.removeStyleDependentName("expired");
				timeLocRow.removeStyleDependentName("expired");
				layout.removeStyleDependentName("expired");
			}
		}
	}
	
	public void setSelectionListener(SessionSelectionListener listener) {
		this.listener = listener;		
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

	@Override
	public void onClick(ClickEvent evt) {
		if (listener != null) {
			listener.onSessionSelected(event);
		}
	}
	
	public void dispose() {
		this.listener = null;
		this.event = null;
	}
}
