package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.view.session.TimeLocationRenderer;

public class SessionDetailHeaderPanel implements IsWidget {
	private VerticalPanel layout;
	private Label title;
	private HTML timeAndLocation;
	
	public SessionDetailHeaderPanel() {
		
		HorizontalPanel titleRow = new HorizontalPanel();
		titleRow.setWidth("100%");
		title = new Label();
		title.setStyleName("session-detail-header-title-text");
		title.setWidth("100%");
		titleRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		titleRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		titleRow.add(title);
		
		HorizontalPanel timeLocationRow = new HorizontalPanel();
		timeLocationRow.setWidth("100%");
		timeAndLocation = new HTML();
		timeAndLocation.setStyleName("session-detail-header-location-text");
		timeAndLocation.setWidth("100%");
		timeLocationRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		timeLocationRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		timeLocationRow.add(timeAndLocation);
		
		layout = new VerticalPanel();
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setStyleName("session-detail-header");
		layout.setWidth("100%");
		layout.add(titleRow);
		layout.add(timeLocationRow);
	}

	public void showEvent(EventDataWrapper event) {
		title.setText(event.getData().getTitle());
		
		String timeLoc = TimeLocationRenderer.renderDateTimeLocation(event);
		timeAndLocation.setHTML(timeLoc);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
