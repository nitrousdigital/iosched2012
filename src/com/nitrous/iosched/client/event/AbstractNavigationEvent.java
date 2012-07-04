package com.nitrous.iosched.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.nitrous.iosched.client.history.Bookmark;

public abstract class AbstractNavigationEvent<T> extends GwtEvent<SimpleEventHandler<T>> {
	private Bookmark bookmark;
	public AbstractNavigationEvent() {
	}
	public AbstractNavigationEvent(Bookmark bookmark) {
		this.bookmark = bookmark;
	}
	public Bookmark getBookmark() {
		return bookmark;
	}
	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
	}
}
