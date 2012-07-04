package com.nitrous.iosched.client.view.home.explore;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.history.Bookmark;

/**
 * The card on the home screen that allows the user to select a track to launch the session/sandbox view or launch the map view
 * 
 * @author nick
 *
 */
public class ExploreView implements IsWidget {
	private DockLayoutPanel layout;
	private TrackList tracks;
	public ExploreView() {
		
		MapButton mapButton = new MapButton();
		tracks = new TrackList();
		
		VerticalPanel scrollingContent = new VerticalPanel();
		scrollingContent.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		scrollingContent.setWidth("100%");
		scrollingContent.add(mapButton);
		scrollingContent.add(tracks);
		
		ScrollPanel scroll = new ScrollPanel(scrollingContent);
		scroll.setSize("100%", "100%");
		
		layout = new DockLayoutPanel(Unit.PX);
		layout.setSize("100%", "100%");
		layout.add(scroll);
	}
	
	public void onRefresh() {
	}
	
	public Bookmark saveState(Bookmark bookmark) {
		return bookmark;
	}
	
	public void restoreState(Bookmark bookmark) {
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
