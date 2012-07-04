package com.nitrous.iosched.client.view.session.detail;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.EventDataWrapper;

public class SessionDetailLayoutPanel implements IsWidget {
	private DockLayoutPanel layout;
	
	private SessionDetailHeaderPanel header;
	private SessionDetailPanel detail;
	private static final int HEADER_HEIGHT = 64;
	
	public SessionDetailLayoutPanel() {
		layout = new DockLayoutPanel(Unit.PX);
		layout.setSize("100%", "100%");
		
		header = new SessionDetailHeaderPanel();
		layout.addNorth(header, HEADER_HEIGHT);
				
		detail = new SessionDetailPanel();		
		detail.asWidget().setSize("100%", "100%");
		ScrollPanel detailScroll = new ScrollPanel(detail.asWidget());
		layout.add(detailScroll);		
	}
	
	public void showDetails(EventDataWrapper event) {
		header.showEvent(event);
		detail.showEvent(event);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
