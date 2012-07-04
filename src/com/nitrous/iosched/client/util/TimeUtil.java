package com.nitrous.iosched.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Window;
import com.nitrous.iosched.client.data.Configuration;

public class TimeUtil {
	public static TimeZone TIMEZONE = TimeZone.createTimeZone(7 * 60);
	public static final String CONFERENCE_TIMEZONE = "GMT-07:00";
	public static final String CONFERENCE_TIMEZONE_SHORT = "-0700";
	public static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("MMddyyyyHHmmZZZZ");
	private static long sessionStartTime = -1;

	// the override session start time specified using the URL argument now=MMddyyyyHHmmZZZ
	private static long virtualTime = -1;
	
	private TimeUtil() {
	}
	
	/**
	 * A utility method to determine the current time. This allows us to test
	 * scheduling functionality by supporting a session start time parameter in 
	 * the URL using the format MMddyyyyHHmmZZZ i.e. now=062720120900-07:00
	 * 
	 * @return The current time
	 */
	public static long getCurrentTimeMillis() {
		if (Configuration.isStaticMode()) {			
			if (sessionStartTime == -1) {
				sessionStartTime = System.currentTimeMillis();
			}
			
			
			// cache the parse date configuration
			if (virtualTime == -1) {
				String timeStr = Window.Location.getParameter("now");
				if (timeStr != null && timeStr.trim().length() > 0) {
					timeStr = timeStr.trim();
					try {
						Date parsed = DATE_TIME_FORMAT.parse(timeStr);
						ConsoleLogger.debug("Parsed virtual time: "+DATE_TIME_FORMAT.format(parsed));
						virtualTime = parsed.getTime();
					} catch (Exception ex) {
						ConsoleLogger.error("Failed to parse session start time '"+timeStr+"'", ex);					
					}
				}
			}
			
			// return a live time offset from the specified date configuration
			if (virtualTime != -1) {
				// determine how much time has passed and update our emulated session time
				long liveClockOffset = System.currentTimeMillis() - sessionStartTime;
				return virtualTime + liveClockOffset;
			}
		}
		
		return System.currentTimeMillis();
	}
	
}
