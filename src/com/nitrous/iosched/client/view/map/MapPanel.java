package com.nitrous.iosched.client.view.map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.data.Configuration;
import com.nitrous.iosched.client.util.ConsoleLogger;

public class MapPanel implements IsWidget {
	private DockLayoutPanel layout;
	private Frame frame;
	public MapPanel() {
		layout = new DockLayoutPanel(Unit.PX);
		frame = new Frame();
		frame.setUrl(Configuration.getMapUrl());
		frame.setSize("100%", "100%");
		layout.add(frame);		
	}
	
	public void onRefresh() {
		ConsoleLogger.debug("MapPanel::onRefresh()");
		frame.setUrl(Configuration.getMapUrl());
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}
	
}
