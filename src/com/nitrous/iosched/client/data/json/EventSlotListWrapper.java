package com.nitrous.iosched.client.data.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.nitrous.iosched.client.data.EventSlotDateComparator;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.util.Util;

/**
 * A wrapper to parse the event time slots
 * <pre>
 * "event_timeslots": "[{'date': '2012-06-27', 'slot1': (u'09:30', u'11:45')}, {'date': '2012-06-28', 'slot1': (u'10:00', u'11:30')}]",
 * </pre>

 * @author Nick
 *
 */
public class EventSlotListWrapper {
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static final DateTimeFormat PARSE_TIME_FORMAT = DateTimeFormat.getFormat("HH:mmZZZZ");
	private static final DateTimeFormat DISPLAY_TIME_FORMAT = DateTimeFormat.getFormat("HH:mm");
	private static final DateTimeFormat MMddyyyy_FORMAT = DateTimeFormat.getFormat("MMddyyyy");
	private static final DateTimeFormat HHmm_FORMAT = DateTimeFormat.getFormat("HHmm");
	
	private TreeMap<Date, TreeSet<EventSlotWrapper>> slots;
	public EventSlotListWrapper() {
	}

	public EventSlotListWrapper(TreeMap<Date, TreeSet<EventSlotWrapper>> slots) {
		this.slots = slots;
	}
	
	public TreeMap<Date, TreeSet<EventSlotWrapper>> getSlots() {
		return slots;
	}
	
	public static EventSlotListWrapper parse(String json) {
		if (json == null) {			
			return null;
		}
		json = json.trim();
		if (json.length() == 0) {
			return null;
		}
		
		String[] arr = parseArray(json);
		if (arr == null || arr.length == 0)  {
			return null;
		}
		
		TreeMap<Date, TreeSet<EventSlotWrapper>> slots = new TreeMap<Date, TreeSet<EventSlotWrapper>>(new EventSlotDateComparator()); 
		for (String slotStr : arr) {
			Map<String, String> slotAttributes = parseNamedAttributes(slotStr);
			// slot date
			Date eventDate = parseSlotDate(slotAttributes);
			if (eventDate == null) {
				ConsoleLogger.error("EventSlotListWrapper::parse() '"+slotStr+"' - failed to parse date");
				continue;
			}
			
			// slot times
			int slotIdx = 1;
			while (true) {
				String slotName = "slot" + slotIdx;
				String slotValue = slotAttributes.get(slotName);
				if (slotValue == null) {
					// stop parsing when we find a missing slot definition 
					break;
				}
				
				Date[] slotTimes = parseSlotTimes(eventDate, slotValue);
				if (slotTimes == null) {
					// failed to parse slot times, skip this slot
					continue;
				}
				
				// slot parsed, construct wrapper and store in lookup from date to set of slots
				EventSlotWrapper slot = new EventSlotWrapper(slotName, eventDate, slotTimes[0], slotTimes[1]);				
				TreeSet<EventSlotWrapper> slotsForDate = slots.get(eventDate);
				if (slotsForDate == null) {
					slotsForDate = new TreeSet<EventSlotWrapper>(new EventSlotComparator());
					slots.put(eventDate, slotsForDate);
				}
				slotsForDate.add(slot);
				slotIdx++;
			}
		}
		
		if (slots.size() == 0) {
			return null;
		}
		
		EventSlotListWrapper wrapper = new EventSlotListWrapper(slots);
		
		// debug
		if (!GWT.isScript()) {
			ConsoleLogger.debug("EventSlotListWrapper::parse() - parsed event slot list: "+wrapper.toString());
		}
		
		return wrapper;
	}
	
	/**
	 * 
	 * @param json u'09:30', u'11:45'
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Date[] parseSlotTimes(Date eventDate, String json) {
		Date[] slotTimes = new Date[2];
		int idx = 0;
		int len = json.length();
		char c = ' ';
		for (int arrIdx = 0 ; arrIdx < 2; arrIdx++) {
			// find u
			while (idx < len && (c = json.charAt(idx)) != 'u') {
				idx++;
			}
			if (c != 'u') {
				ConsoleLogger.error("Failed to locate time element "+arrIdx+" for slot in json '"+json+"'");
				return null;
			}
			idx++;
			
			// find opening quote
			while (idx < len && (c = json.charAt(idx)) != '\'') {
				idx++;
			}
			if (c != '\'') {
				// didnt find opening quote for attribute name
				ConsoleLogger.error("Failed to locate opening quote for time element "+arrIdx+" for slot in json '"+json+"'");
				return null;
			}
			idx++;
	
			// read time
			StringBuffer buf = new StringBuffer();
			while (idx < len && (c = json.charAt(idx)) != '\'') {
				buf.append(c);
				idx++;
			}
			if (c != '\'') {
				// didnt find closing quote for attribute name
				ConsoleLogger.error("Failed to locate closing quote for time element "+arrIdx+" for slot in json '"+json+"'");
				return null;
			}
			idx++;

			String timeStr = buf.toString().trim();
			if (timeStr.length() == 0) {
				ConsoleLogger.error("Failed to locate time element "+arrIdx+" for slot in json '"+json+"'");
				return null;
			}
			
			Date time = null;
			try {
				// 
				StringBuffer timeBuf = new StringBuffer(timeStr);
				timeBuf.append(Util.CONFERENCE_TIMEZONE);
				time = PARSE_TIME_FORMAT.parse(timeBuf.toString());
			} catch (Throwable t) {
				ConsoleLogger.error("Failed to parse time '"+timeStr+"' in json '"+json+"'", t);
				return null;
			}

			StringBuffer dateTimeBuf = new StringBuffer();
			dateTimeBuf.append(MMddyyyy_FORMAT.format(eventDate));
			dateTimeBuf.append(HHmm_FORMAT.format(time, Util.TIMEZONE));
			dateTimeBuf.append(Util.CONFERENCE_TIMEZONE);
						
			Date theTime = Util.DATE_TIME_FORMAT.parse(dateTimeBuf.toString());
			theTime.setSeconds(0);
			slotTimes[arrIdx] = theTime;
		}
		return slotTimes;
	}
	
	/**
	 * 
	 * @param json {'date': '2012-06-27', 'slot1': (u'09:30', u'11:45')}
	 * @return
	 */
	private static Map<String, String> parseNamedAttributes(String json) {
		HashMap<String, String> map = new HashMap<String, String>();
		int len = json.length();
		int idx = 0;
		char c = ' ';
		while (idx < len) {
			// find opening quote for attribute name
			while (idx < len && (c = json.charAt(idx)) != '\'') {
				idx++;
			}
			if (c != '\'') {
				// didnt find opening quote for attribute name
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - failed to locate opening quote for attribute name");
				break;
			}
			
			// advance to first character of name
			idx++;
			if (idx == len) {
				break;
			}
			
			// read name
			StringBuffer name = new StringBuffer();
			while (idx < len && (c = json.charAt(idx)) != '\'') {
				name.append(c);
				idx++;
			}
			if (c != '\'') {
				// didnt find closing quote for attribute name
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - failed to find closing quote for attribute named '"+name.toString()+"'");
				break;
			}
			
			// find separator
			while (idx < len && (c = json.charAt(idx)) != ':') {
				idx++;
			}
			if (c != ':') {
				// didnt find separator
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - failed to find separator after attribute name '"+name.toString()+"'");
				break;
			}
			idx++;
			
			// skip whitespace
			while (idx < len && isWhitespace((c = json.charAt(idx)))) {
				idx++;
			}
			if (idx == len) {
				// premature end of string
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - failed to value for attribute '"+name.toString()+"'");
				break;
			}
			
			// check for opening quote or opening paren
			char closingChar;
			if (c == '\'') {
				closingChar = '\''; 
			} else if (c == '(') {
				closingChar = ')';
			} else {
				// unexpected opening character for value
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - unexpected opening value delimeter '"+c+"' for attribute '"+name.toString()+"'");
				break;
			}
			idx++;
			
			// read value
			StringBuffer value = new StringBuffer();
			while (idx < len && (c = json.charAt(idx)) != closingChar) {
				value.append(c);
				idx++;
			}
			if (c != closingChar) {
				// didnt find closing character for attribute value
				ConsoleLogger.error("EventSlotListWrapper::parseNamedAttributes("+json+") - failed to find closing delimeter '"+closingChar+"' for attribute '"+name.toString()+"'");
				break;
			}
			idx++;
			map.put(name.toString(), value.toString().trim());
		}
		return map;
	}
	
	private static boolean isWhitespace(char c) {
		return c == ' ' || c == '\n' || c == '\r';
	}
	
	private static String[] parseArray(String json) {
		if (!(json.startsWith("[") && json.endsWith("]"))) {
			return null;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		StringBuffer el = null;
		for (int idx = 0, len = json.length() ; idx < len; idx++) {
			char c = json.charAt(idx);
			if (c == '{') {
				// begin new array element
				el = new StringBuffer();
			} else if (c == '}') {
				// finish current array element
				if (el != null) {
					String str = el.toString().trim();
					if (str.length() > 0) {
						list.add(str);
					}
				}
				el = null;
			} else if (el != null) {
				el.append(c);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	@SuppressWarnings("deprecation")
	private static Date parseSlotDate(Map<String, String> slotAttributes) {
		// slot date
		String dateStr = slotAttributes.get("date");
		if (dateStr == null) {
			ConsoleLogger.error("EventSlotListWrapper::parseSlotDate() - missing date");
			return null;
		}
		
		Date eventDate = null;
		try {
			eventDate = DATE_FORMAT.parse(dateStr);
		} catch (Throwable t) {
			ConsoleLogger.error("EventSlotListWrapper::parseSlotDate() - failed to parse date", t);
			return null;
		}
		eventDate.setHours(0);
		eventDate.setMinutes(0);
		eventDate.setSeconds(0);
		return eventDate;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("EventSlotListWrapper[");
		if (slots != null) {
			for (Map.Entry<Date, TreeSet<EventSlotWrapper>> entry : slots.entrySet()) {
				Date date = entry.getKey();
				TreeSet<EventSlotWrapper> dateSlots = entry.getValue();
				buf.append("(date=");
				buf.append(DATE_FORMAT.format(date));
				for (EventSlotWrapper slot : dateSlots) {
					buf.append(",");
					buf.append(DISPLAY_TIME_FORMAT.format(slot.getStartTime(), Util.TIMEZONE));
					buf.append("-");
					buf.append(DISPLAY_TIME_FORMAT.format(slot.getEndTime(), Util.TIMEZONE));
				}
				buf.append(")");
			}
		}
		buf.append("]");
		return buf.toString();
	}
	
}
