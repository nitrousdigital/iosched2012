package com.nitrous.iosched.client.view.home.agenda;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DateSectionRow implements IsWidget {
	private HorizontalPanel layout;
	private static final DateTimeFormat FORMAT = DateTimeFormat.getFormat("EEEE");
	public DateSectionRow(Date date) {
		layout = new HorizontalPanel();
		layout.setWidth("100%");
		
		Label label = new Label(FORMAT.format(date));
		label.setStyleName("date-section-header-row");
		layout.add(label);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
