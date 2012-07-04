package com.nitrous.iosched.client.view.map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;

public class RootMapView implements IsWidget {
	private DockLayoutPanel layout;
	private MapPanel map;
	public RootMapView() {
		layout = new DockLayoutPanel(Unit.PX);
		
		MapToolbar toolbar = new MapToolbar();
		layout.addNorth(toolbar,  MapToolbar.HEIGHT);
		
		map = new MapPanel();
		layout.add(map);
		
		Registry.get().getEventBus().addHandler(RefreshViewEvent.TYPE, new SimpleEventHandler<RefreshViewEvent>(){
			@Override
			public void handleEvent(RefreshViewEvent event) {
				if (RefreshViewEvent.View.MAP.equals(event.getView())) {
					onRefresh();
				}
			}			
		});		
	}
	
	private void onRefresh() {
		map.onRefresh();
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
