package com.nitrous.iosched.client.view.session;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.TimeSlot;
import com.nitrous.iosched.client.util.TimeUtil;

public class TimeLocationRenderer {
	private static DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("h:mmaa");
	private static DateTimeFormat DAY_OF_WEEK_FORMAT = DateTimeFormat.getFormat("EEE");
	public static String renderDateTime(TimeSlot slot) {
		StringBuffer buf = new StringBuffer();		
		buf.append(TIME_FORMAT.format(slot.getStartTime(), TimeUtil.TIMEZONE));
		buf.append(" - ");
		buf.append(TIME_FORMAT.format(slot.getEndTime(), TimeUtil.TIMEZONE));
		buf.append(", ");
		buf.append(DAY_OF_WEEK_FORMAT.format(slot.getStartTime(), TimeUtil.TIMEZONE));			
		return buf.toString();
	}
	
	public static String renderDateTimeLocation(EventDataWrapper event) {
		StringBuffer timeLocText = new StringBuffer();
		timeLocText.append(TimeLocationRenderer.renderDateTime(event.getSlot()));
		timeLocText.append(" in ");
		timeLocText.append(TimeLocationRenderer.renderLocation(event));
		
		String liveUrl = event.getData().getLiveStreamUrl();
		if (liveUrl != null && liveUrl.trim().length() > 0) {
			timeLocText.append(" <b>LIVE</b>");				
		}
		return timeLocText.toString();		
	}
	
	public static String renderLocation(EventDataWrapper event) {
		String room = event.getData().getRoom();
		StringBuffer buf = new StringBuffer();
		if (room != null && room.trim().length() > 0) {
			buf.append("Room ");
			buf.append(room.trim());
		} else {
			buf.append("(Unspecified Room)");
		}
		return buf.toString();
	}
	
}
