package com.nitrous.iosched.client.history;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.nitrous.iosched.client.Registry;
import com.nitrous.iosched.client.event.RootViewChangeEvent;
import com.nitrous.iosched.client.event.RootViewChangeEvent.View;

public class HistoryManager {
	private static HistoryManager INSTANCE;
	private HistoryManager() {
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			  public void onValueChange(ValueChangeEvent<String> event) {
				  onHistoryChange(event.getValue());
			  }
		});
	}
	
	public void init() {
		String initialToken = History.getToken();
		onHistoryChange(initialToken);		
	}
	
	public static HistoryManager get() {
		if (INSTANCE == null) {
			INSTANCE = new HistoryManager();
		}
		return INSTANCE;
	}
	
	private void onHistoryChange(String token) {
		if (token == null || token.trim().length() == 0) {
			// initial page load
			History.newItem(BookmarkCategory.HOME.toString());
		} else {
			Bookmark bookmark = Bookmark.parse(token);
			if (bookmark != null) {
				navigateTo(bookmark);
			}
		}
	}
	
	public void setHistoryToken(Bookmark bookmark) {
		setHistoryToken(bookmark.toString());
	}
	
	public void setHistoryToken(String token) {
		String old = History.getToken();
		if (old != null && !old.equals(token)) {
			History.newItem(token, false);
		}
	}
	
	/**
	 * Respond to history navigation
	 * @param bookmark The bookmark extracted from the history stack
	 */
	private void navigateTo(Bookmark bookmark) {
		
		switch (bookmark.getCategory()) {
		case HOME:
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.HOME, bookmark));
			break;
		case MAP:
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.MAP, bookmark));
			break;
		case SESSIONS:
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_LIST, bookmark));
			break;
		case SESSION_DETAIL:
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.SESSION_DETAIL, bookmark));
			break;
		case SESSION_SELECTION:
			Registry.get().getEventBus().fireEvent(new RootViewChangeEvent(View.AGENDA_SESSION_SELECTION, bookmark));
			break;
			
		}
	}
	
}
