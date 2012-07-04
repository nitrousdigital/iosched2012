package com.nitrous.iosched.client.data;

import java.util.Comparator;
import java.util.Date;

/**
 * Sort dates by year, month, day, hour, minute and second.
 * Dates are considered equal if they have share year, month, day, hour, minute and second values.
 * @author Nick
 */
public class EventSlotDateComparator implements Comparator<Date> {

	@SuppressWarnings("deprecation")
	@Override
	public int compare(Date o1, Date o2) {
		if (o1 == o2) {
			return 0;
		}
		
		if (o1 == null && o2 != null) {
			return 1;
		} else if (o1 != null && o2 == null) {
			return -1;
		}
		
		int result = compare(o1.getYear(), o2.getYear());
		if (result != 0) {
			return result;
		}
		
		result = compare(o1.getMonth(), o2.getMonth());
		if (result != 0) {
			return result;
		}
		
		result = compare(o1.getDate(), o2.getDate());
		if (result != 0) {
			return result;
		}
		
		result = compare(o1.getHours(), o2.getHours());
		if (result != 0) {
			return result;
		}
		
		result = compare(o1.getMinutes(), o2.getMinutes());
		if (result != 0) {
			return result;
		}
		
		result = compare(o1.getSeconds(), o2.getSeconds());
		return result;
	}
	
	private static int compare(Integer i1, Integer i2) {
		return i1.compareTo(i2);
	}

}
