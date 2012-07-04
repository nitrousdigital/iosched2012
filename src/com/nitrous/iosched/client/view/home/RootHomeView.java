package com.nitrous.iosched.client.view.home;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.HomeTabSelectionEvent;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.history.BookmarkCategory;
import com.nitrous.iosched.client.history.HistoryManager;
import com.nitrous.iosched.client.view.home.agenda.MyAgendaView;
import com.nitrous.iosched.client.view.home.countdown.CountDownContainer;
import com.nitrous.iosched.client.view.home.countdown.CountDownPanel;
import com.nitrous.iosched.client.view.home.explore.ExploreView;
import com.nitrous.iosched.client.widget.TabPanel;
import com.nitrous.iosched.client.widget.TabPanel.TabChangeListener;

/**
 * The home screen that shows the tool bar with google IO logo, search and refresh buttons and
 * a tab panel containing the My Agenda, Explore and Stream tabs.
 * 
 * @author nick
 *
 */
public class RootHomeView implements IsWidget, CountDownContainer, TabChangeListener {
	private DockLayoutPanel layout;
	private TabPanel tabPanel;
	
	private static final String TAB_ID_HISTORY_TOKEN = "tab";
	
	private ExploreView exploreView;
	private MyAgendaView agendaView;
	
	private static final String AGENDA_TAB = "agenda";
	private static final String EXPLORE_TAB = "explore";
	
	private CountDownPanel countdown;
	
	public RootHomeView() {
		
		RootHomeViewToolbar toolbar = new RootHomeViewToolbar();

		tabPanel = new TabPanel();
		tabPanel.asWidget().setSize("100%", "100%");
		agendaView = new MyAgendaView();
		exploreView = new ExploreView();
		
		tabPanel.add(agendaView, AGENDA_TAB, "MY AGENDA");
		tabPanel.add(exploreView, EXPLORE_TAB, "EXPLORE");
		
				
		
		layout = new DockLayoutPanel(Unit.PX);
		layout.addNorth(toolbar, RootHomeViewToolbar.HEIGHT);
		countdown = new CountDownPanel(this);
		if (!countdown.isExpired()) {
			layout.addSouth(countdown, CountDownPanel.HEIGHT);
		} else {
			countdown = null;
		}
		layout.add(tabPanel);
		
		tabPanel.showTab(AGENDA_TAB);
		
		tabPanel.setTabChangeListener(this);
		
		Registry.get().getEventBus().addHandler(RefreshViewEvent.TYPE, new SimpleEventHandler<RefreshViewEvent>(){
			@Override
			public void handleEvent(RefreshViewEvent event) {
				if (RefreshViewEvent.View.HOME.equals(event.getView())) {
					onRefresh();
				}
			}			
		});		
		Registry.get().getEventBus().addHandler(HomeTabSelectionEvent.TYPE, new SimpleEventHandler<HomeTabSelectionEvent>() {
			@Override
			public void handleEvent(HomeTabSelectionEvent event) {				
				switch (event.getTab()) {
				case EXPLORE:
					showExploreView(event);
					break;
				case MY_AGENDA:
					showMyAgendaView(event);
					break;
				}
			}
		});
	}
	
	private void showExploreView(HomeTabSelectionEvent event) {
		tabPanel.showTab(EXPLORE_TAB, false);
	}
	private void showMyAgendaView(HomeTabSelectionEvent event) {
		tabPanel.showTab(AGENDA_TAB, false);
	}
	
	private void onRefresh() {
		String id = tabPanel.getSelectedTabId();
		if (EXPLORE_TAB.equals(id)) {
			exploreView.onRefresh();
		} else if (AGENDA_TAB.equals(id)) {
			agendaView.onRefresh();
		}
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

	@Override
	public void onCountDownExpired() {
		if (countdown != null) {
			layout.remove(countdown);
			layout.forceLayout();
		}
	}

	public Bookmark saveState(Bookmark bookmark) {
		String tabId = tabPanel.getSelectedTabId();
		if (tabId != null) {
			// save tab selection
			bookmark.addStateToken(TAB_ID_HISTORY_TOKEN, tabId);
			
			// save the state of the active tab
			if (EXPLORE_TAB.equals(tabId)) {
			    bookmark = exploreView.saveState(bookmark);
			} 
		}		
		return bookmark;
	}
	
	public void restoreState(Bookmark bookmark) {
		if (bookmark == null) {
			return;
		}
		String tabId = bookmark.getStateValue(TAB_ID_HISTORY_TOKEN);
		if (tabId != null && tabId.trim().length() > 0) {
			// restore tab selection
			tabPanel.showTab(tabId, false);
			
			// restore active tab state
			if (EXPLORE_TAB.equals(tabId)) {
				exploreView.restoreState(bookmark);
			}
		}
	}
	
	@Override
	public void onTabSelected(String tabId) {
		Bookmark bookmark = new Bookmark(BookmarkCategory.HOME);
		bookmark.addStateToken(TAB_ID_HISTORY_TOKEN, tabId);
		HistoryManager.get().setHistoryToken(bookmark);		
	}
	
}
