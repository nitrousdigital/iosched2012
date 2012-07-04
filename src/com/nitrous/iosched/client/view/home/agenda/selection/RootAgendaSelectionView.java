package com.nitrous.iosched.client.view.home.agenda.selection;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.history.Bookmark;

/**
 * The view that allows the user to select a session from a filtered list to add to his/her agenda.
 * @author nick
 *
 */
public class RootAgendaSelectionView implements IsWidget {
	private SessionSelectionComponent sessionList;
	private SessionSelectionToolbar toolbar;
	private DockLayoutPanel layout;
	
	public RootAgendaSelectionView() {
		toolbar = new SessionSelectionToolbar();
		sessionList = new SessionSelectionComponent();
	
		layout = new DockLayoutPanel(Unit.PX);
		layout.addNorth(toolbar, SessionSelectionToolbar.HEIGHT);
		layout.add(sessionList);
	}
	
	public void restoreState(Bookmark bookmark) {
		sessionList.restoreState(bookmark);
	}
	
	public Bookmark saveState(Bookmark bookmark) {
		return sessionList.saveState(bookmark);
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

}
