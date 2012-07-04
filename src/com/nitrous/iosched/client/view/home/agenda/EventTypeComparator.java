package com.nitrous.iosched.client.view.home.agenda;

import java.util.Comparator;

import com.nitrous.iosched.client.data.EventType;

public class EventTypeComparator implements Comparator<EventType> {
	@Override
	public int compare(EventType type0, EventType type1) {
		if (type0 == type1) {
			return 0;
		}
		if (type0 == null && type1 != null) {
			return -1;
		} else if (type0 != null && type1 == null) {
			return 1;
		} 
		
		return getDisplaySequence(type0).compareTo(getDisplaySequence(type1));
	}

	private static Integer getDisplaySequence(EventType type) {
		switch (type) {
		case KEYNOTE:
			return 0;
		case SESSION:
			return 1;
		case CODELAB:
			return 2;
		}
		return 3;
	}
}
