package com.nitrous.iosched.client.data;

import java.util.Comparator;
import java.util.Date;

import com.nitrous.iosched.client.view.home.agenda.EventTypeComparator;
import com.nitrous.iosched.client.view.home.agenda.TimeSlotPanel;

/**
 * Sort time slots by start time and then event type
 * @author nick
 *
 */
public class TimeSlotPanelComparator implements Comparator<TimeSlotPanel> {
	private static final EventTypeComparator TYPE_SORT = new EventTypeComparator();
	private static final EventSlotDateComparator DATE_SORT = new EventSlotDateComparator();
	
	@Override
	public int compare(TimeSlotPanel slot0, TimeSlotPanel slot1) {
		if (slot0 == slot1) {
			return 0;
		}
		if (slot0 == null && slot1 != null) {
			return 1;
		} else if (slot0 != null && slot1 == null) {
			return -1;
		}
		
		// 1. sort by start time
		Date start0 = slot0.getStartTime();
		Date start1 = slot1.getStartTime();
		int result = DATE_SORT.compare(start0, start1);
		
		// 2. sort by event type
		if (result == 0) {
			EventType type0 = slot0.getEventType();
			EventType type1 = slot1.getEventType();
			result = TYPE_SORT.compare(type0, type1);
		}
		
		return result;
	}

}
