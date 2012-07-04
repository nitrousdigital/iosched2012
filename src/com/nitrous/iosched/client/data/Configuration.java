package com.nitrous.iosched.client.data;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


public class Configuration {
    private static final String CONFERENCE_API_KEY = "AIzaSyA2MhtOhocnrkFvc_uyavMbrLj_Qi36Vak";
    
	private Configuration() {
	}
	
	// SESSION DETAILS WEB LINK BASE URL
	private static final String SESSION_WEB_LINK_BASE_URL = "https://developers.google.com/events/io/sessions/";
		
	// SESSION FEED - The live session feed URL used by the Google IO 2012 Android Application
	private static final String LIVE_SESSION_FEED_URL = "https://google-developers.appspot.com/_ah/api/resources/v0.1/sessions?parent_event=googleio2012&api_key=" 
			+ CONFERENCE_API_KEY;
	// SESSION FEED - OFFLINE SNAPSHOT
	private static final String OFFLINE_SESSION_FEED_URL = GWT.getHostPageBaseURL() + "/static/sessions2.json";
	
	// MAP
	private static final String MAP_URL = "http://ioschedmap.appspot.com/embed.html?multitouch=true";
	
	/**
	 * @return True if snapshots should be used (useful for testing)
	 */
	public static boolean isStaticMode() {
		return Window.Location.getParameterMap().containsKey("static");
	}
	
	/**
	 * @param sessionId The ID of the session
	 * @return The Web link to the session details
	 */
	public static String getSessionWebLink(String sessionId) {
		return SESSION_WEB_LINK_BASE_URL + sessionId;
	}
	
	/**
	 * @return The URL to the JSON Session feed
	 */
	public static String getSessionFeed() {
		if (isStaticMode()) {
			return OFFLINE_SESSION_FEED_URL;
		} else {
			return LIVE_SESSION_FEED_URL;
		}
	}
	
	public static String getMapUrl() {
		return MAP_URL;
	}
}
