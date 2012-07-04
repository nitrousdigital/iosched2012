package com.nitrous.iosched.client.data.json;

import java.util.Comparator;

import com.nitrous.iosched.client.data.EventSlotDateComparator;

/**
 * Sort EventSlotWrappers by start date, start time and slot name.
 * 
 * If the slot start date, start time and slot name are the same, they are considered to be the same slot. 
 * @author Nick
 */
public class EventSlotComparator implements Comparator<EventSlotWrapper> {
	private static final EventSlotDateComparator DATE_COMPARATOR = new EventSlotDateComparator();
	
	@Override
	public int compare(EventSlotWrapper o1, EventSlotWrapper o2) {
		if (o1 == o2) {
			return 0;
		}
		
		int result = DATE_COMPARATOR.compare(o1.getSlotDate(), o2.getSlotDate());
		if (result != 0) {
			return result;
		}
		
		result = DATE_COMPARATOR.compare(o1.getStartTime(), o2.getStartTime());
		if (result != 0) {
			return result;
		}
		
		result = o1.getName().compareTo(o2.getName());
		return result; 
	}

}
