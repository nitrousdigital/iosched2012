package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SectionHeader implements IsWidget {
	private HorizontalPanel layout;
	
	public SectionHeader(String text) {		
		Label label = new Label(text);
		label.setWidth("100%");
		
		layout = new HorizontalPanel();
		layout.setWidth("100%");
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.add(label);
		layout.setStyleName("session-detail-section-header-text");
	}

	@Override
	public Widget asWidget() {
		return layout;
	}
	
}
