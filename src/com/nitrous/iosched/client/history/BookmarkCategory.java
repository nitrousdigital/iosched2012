package com.nitrous.iosched.client.history;

public enum BookmarkCategory {
	HOME("home"),
	SESSIONS("sessions"),
	SESSION_DETAIL("sessiondetail"),
	MAP("map"),
	SESSION_SELECTION("sessionselection");
	private String label;
	private BookmarkCategory(String label) {
		this.label = label;
	}
	public String toString() {
		return label;
	}
}
