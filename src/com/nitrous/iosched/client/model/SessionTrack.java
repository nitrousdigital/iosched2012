package com.nitrous.iosched.client.model;

public enum SessionTrack {
	All("(All tracks)", "all", null),
	Android("Android", "android", "#a5be42"),
	Chrome("Chrome", "chrome", "#42b2e7"),	
	CloudPlatform("Cloud Platform", "cloudplatform", "#2175bd"),
	CodeLabs("Code Labs", "codelabs", "#e7388c"),
	Commerce("Commerce", "commerce", "#4aaa42"),
	Entrepreneurship("Entrepreneurship", "entrepreneurship", "#94b2de"),
	GoogleAPIs("Google APIs", "googleapis", "#007539"),
	GoogleDrive("Google Drive", "googledrive", "#f78618"),
	GoogleMaps("Google Maps", "googlemaps", "#4aaa42"),
	GoogleTv("Google TV", "googletv", "#31515a"),
	GooglePlus("Google+", "googleplus", "#de4d31"),
	TechTalk("Tech Talk", "techtalk", "#a5619c"),
	YouTube("YouTube", "youtube", "#e72c29"),
	KeyNote("Keynote", "keynote", null),
	AfterHours("After Hours", "afterhours", null);
	
	private String label;
	private String historyToken;
	private String color;
	SessionTrack(String label, String token, String color) {
		this.label = label;
		this.historyToken = token;
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getLabel() {
		return label;
	}
	
	/**
	 * @return The display label for this session track
	 */
	public String toString() {
		return label;
	}
	public String getHistoryToken() {
		return historyToken;
	}
	
	public static SessionTrack parseTrackName(String name) {
		if (name == null) {
			return null;
		}
		name = name.trim();
		
		if ("Code Labs 1".equalsIgnoreCase(name) ||
				"Code Labs 2".equalsIgnoreCase(name)) {
			return CodeLabs;
		} else if ("Tech talks".equalsIgnoreCase(name)) {
			return TechTalk;
		}
		
		for (SessionTrack track : values()) {
			if (track.label.equalsIgnoreCase(name)) {
				return track;
			}
		}
		return null;
	}
	
	public static SessionTrack parseHistoryToken(String token) {
		if (token == null || token.trim().length() == 0) {
			return All;
		}
		
		for (SessionTrack track : values()) {
			if (track.historyToken != null && track.historyToken.equalsIgnoreCase(token)) {
				return track;
			}
		}
		return All;
	}
}
