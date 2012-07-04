package com.nitrous.iosched.client.view.session;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RefreshViewEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.history.BookmarkCategory;
import com.nitrous.iosched.client.history.HistoryManager;
import com.nitrous.iosched.client.widget.TabPanel;
import com.nitrous.iosched.client.widget.TabPanel.TabChangeListener;

/**
 * The view that shows tabs for session times and sandbox
 * @author nick
 *
 */
public class RootSessionView implements IsWidget, TabChangeListener {
	private DockLayoutPanel layout;
	
	private TabPanel tabs;
	private SessionListView sessionList;
	
	private static final String TAB_ID_HISTORY_TOKEN = "tab";
	private static final String SESSION_LIST_TAB_ID = "sessions";
	
	public RootSessionView() {
		layout = new DockLayoutPanel(Unit.PX);
		
		SessionToolbar toolbar = new SessionToolbar();
		layout.addNorth(toolbar, SessionToolbar.HEIGHT);
	
		tabs = new TabPanel();
		sessionList = new SessionListView();
		tabs.add(sessionList, SESSION_LIST_TAB_ID, "SESSIONS");
		layout.add(tabs);
		
		tabs.showTab(SESSION_LIST_TAB_ID);
		tabs.setTabChangeListener(this);
		
		Registry.get().getEventBus().addHandler(RefreshViewEvent.TYPE, new SimpleEventHandler<RefreshViewEvent>(){
			@Override
			public void handleEvent(RefreshViewEvent event) {
				if (RefreshViewEvent.View.SESSIONS_LIST.equals(event.getView())) {
					onRefresh();
				}
			}			
		});
	}

	private void onRefresh() {
		String tabId = tabs.getSelectedTabId();
		if (SESSION_LIST_TAB_ID.equals(tabId)) {
			sessionList.onRefresh();
		}
	}
	
	@Override
	public Widget asWidget() {
		return layout;
	}

	@Override
	public void onTabSelected(String tabId) {
		Bookmark bookmark = new Bookmark(BookmarkCategory.SESSIONS);
		bookmark.addStateToken(TAB_ID_HISTORY_TOKEN, tabId);
		if (SESSION_LIST_TAB_ID.equals(tabId)) {
			bookmark = sessionList.saveState(bookmark);
		}
		HistoryManager.get().setHistoryToken(bookmark);		
	}

	public Bookmark saveState(Bookmark bookmark) {
		String tabId = tabs.getSelectedTabId();
		if (tabId != null) {
			// save tab selection
			bookmark.addStateToken(TAB_ID_HISTORY_TOKEN, tabId);
			
			// save the state of the active tab
			if (SESSION_LIST_TAB_ID.equals(tabId)) {
			    bookmark = sessionList.saveState(bookmark);
			}
		}		
		return bookmark;
	}
	
	public void restoreState(Bookmark bookmark) {
		// restore the tab selection
		String tabId = bookmark.getStateValue(TAB_ID_HISTORY_TOKEN);
		if (tabId == null) {
			tabId = SESSION_LIST_TAB_ID;
		}
		tabs.showTab(tabId, false);
		
		// restore the state of the selected tab
		if (SESSION_LIST_TAB_ID.equalsIgnoreCase(tabId)) {
			sessionList.restoreState(bookmark);
		}
	}
}
