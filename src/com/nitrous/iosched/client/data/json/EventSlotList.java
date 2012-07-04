package com.nitrous.iosched.client.data.json;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The event slots for a single date
 * 
 * <pre>
 * "event_timeslots": "[{'date': '2012-06-27', 'slot1': (u'09:30', u'11:45')}, {'date': '2012-06-28', 'slot1': (u'10:00', u'11:30')}]",
 * </pre>
 * 
 * @author nick
 *
 */
public final class EventSlotList extends JavaScriptObject {
	protected EventSlotList() {
	}
	
	public int getSlotCount() {
		int count = 0;
		for (int idx = 1; ; idx++) {
			if (getSlotStart(idx) != null) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}
	
	public native String getSlotStart(int idx) /*-{
		var ret = eval('this.slot'+idx);
		if (ret) {
			return ret[0];
		} else {
			return null;
		}
	}-*/;
	
	public native String getSlotEnd(int idx) /*-{
		var ret = eval('this.slot'+idx);
		if (ret) {
			return ret[1];
		} else {
			return null;
		}
	}-*/;
	
	/**
	 * @return yyyy-MM-dd
	 */
	public native String getDate() /*-{
		return this.date;
	}-*/;
}
