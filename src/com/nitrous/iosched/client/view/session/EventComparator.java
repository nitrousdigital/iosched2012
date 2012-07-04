package com.nitrous.iosched.client.view.session;

import java.util.Comparator;

import com.nitrous.iosched.client.data.EventDataWrapper;
import com.nitrous.iosched.client.data.TimeSlot;
import com.nitrous.iosched.client.data.json.EventData;

/**
 * Sort events by start time, event type and then title
 * @author nick
 *
 */
public class EventComparator implements Comparator<EventDataWrapper> {

	@Override
	public int compare(EventDataWrapper event0, EventDataWrapper event1) {
		if (event0 == event1) {
			return 0;
		}
		
		TimeSlot slot0 = event0.getSlot();
		TimeSlot slot1 = event1.getSlot();
		
		// 1 - compare start date/time
		int result = slot0.getStartTime().compareTo(slot1.getStartTime());
		if (result != 0) {
			return result;
		}

		// 2 - event type (codelab/session/keynote)
		String eventType0 = event0.getSession().getData().getEventType();
		String eventType1 = event1.getSession().getData().getEventType();
		if (eventType0 != null && eventType1 != null) {
			result = eventType0.compareToIgnoreCase(eventType1);
			if (result != 0) {
				return result;
			}
		}
		
		// 3 - event title
		EventData data0 = event0.getData();
		EventData data1 = event1.getData();
		result = data0.getTitle().compareToIgnoreCase(data1.getTitle());
		if (result != 0) {
			return result;
		}
		
		return -1;
	}

}
