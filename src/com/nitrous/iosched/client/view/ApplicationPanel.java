package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.SimpleEventHandler;
import com.nitrous.iosched.client.history.Bookmark;
import com.nitrous.iosched.client.history.BookmarkCategory;
import com.nitrous.iosched.client.history.HistoryManager;
import com.nitrous.iosched.client.view.home.RootHomeView;
import com.nitrous.iosched.client.view.home.agenda.selection.RootAgendaSelectionView;
import com.nitrous.iosched.client.view.map.RootMapView;
import com.nitrous.iosched.client.view.session.RootSessionView;
import com.nitrous.iosched.client.view.session.detail.RootSessionDetailView;
import com.nitrous.iosched.client.widget.CardPanel;

public class ApplicationPanel implements IsWidget {
	private CardPanel layout;
	private RootHomeView home;
	private RootMapView map;
	private RootSessionView sessionList;
	private RootSessionDetailView sessionDetail;
	private RootAgendaSelectionView agendaSelectionView;
	
	private static final String HOME_CARD = "HOME";
	private static final String MAP_CARD = "MAP";
	private static final String SESSION_LIST_CARD = "SESSIONS";
	private static final String SESSION_DETAIL_CARD = "SESSION_DETAIL";
	private static final String AGENDA_SESSION_SELECTION_CARD = "SESSION_SELECTION";
	
	public ApplicationPanel() {
		layout = new CardPanel();
		home = new RootHomeView();
		map = new RootMapView();
		sessionList = new RootSessionView();
		sessionDetail = new RootSessionDetailView();
		agendaSelectionView = new RootAgendaSelectionView();
		
		layout.addCard(HOME_CARD, home);
		layout.addCard(MAP_CARD, map);
		layout.addCard(SESSION_LIST_CARD, sessionList);
		layout.addCard(SESSION_DETAIL_CARD, sessionDetail);
		layout.addCard(AGENDA_SESSION_SELECTION_CARD, agendaSelectionView);
		
		Registry.get().getEventBus().addHandler(RootViewChangeEvent.TYPE, new SimpleEventHandler<RootViewChangeEvent>(){
			@Override
			public void handleEvent(RootViewChangeEvent event) {
				switch(event.getView()) {
				case HOME:
					showHomeScreen(event);
					break;
				case MAP:
					showMapView(event);
					break;				
				case SESSION_LIST:
					showSessionView(event);
					break;
				case SESSION_DETAIL:
					showSessionDetailView(event);
					break;
				case AGENDA_SESSION_SELECTION:
					showAgendaSessionSelectionView(event);
					break;
				}				
			}
		});
	}
	
	private void showSessionView(RootViewChangeEvent event) {
		layout.showCard(SESSION_LIST_CARD);
		if (event != null && event.getBookmark() != null) {
			sessionList.restoreState(event.getBookmark());
		} else {
			Bookmark bookmark = new Bookmark(BookmarkCategory.SESSIONS);
			bookmark = sessionList.saveState(bookmark);
			HistoryManager.get().setHistoryToken(bookmark);
		}
	}
	
	private void showSessionDetailView(RootViewChangeEvent event) {
		layout.showCard(SESSION_DETAIL_CARD);
		if (event != null && event.getBookmark() != null) {
			sessionDetail.restoreState(event.getBookmark());
		} else {
			Bookmark bookmark = new Bookmark(BookmarkCategory.SESSION_DETAIL);
			bookmark = sessionDetail.saveState(bookmark);
			HistoryManager.get().setHistoryToken(bookmark);
		}
	}
	
	private void showAgendaSessionSelectionView(RootViewChangeEvent event) {
		layout.showCard(AGENDA_SESSION_SELECTION_CARD);
		if (event != null && event.getBookmark() != null) {
			agendaSelectionView.restoreState(event.getBookmark());
		} else {
			Bookmark bookmark = new Bookmark(BookmarkCategory.SESSION_SELECTION);
			bookmark = agendaSelectionView.saveState(bookmark);
			HistoryManager.get().setHistoryToken(bookmark);
		}
	}
	
	private void showHomeScreen(RootViewChangeEvent event) {
		layout.showCard(HOME_CARD);
		if (event != null && event.getBookmark() != null) {
			home.restoreState(event.getBookmark());
		} else {
			Bookmark bookmark = new Bookmark(BookmarkCategory.HOME);
			bookmark = home.saveState(bookmark);
			HistoryManager.get().setHistoryToken(bookmark);
		}
	}
	
	private void showMapView(RootViewChangeEvent event) {
		layout.showCard(MAP_CARD);
		if (event == null || event.getBookmark() == null) {
			Bookmark bookmark = new Bookmark(BookmarkCategory.MAP);
			HistoryManager.get().setHistoryToken(bookmark);
		}
	}
	
	@Override
	public Widget asWidget() {
		return layout.asWidget();
	}

}
