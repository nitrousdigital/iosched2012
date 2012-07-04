package com.nitrous.iosched.client.data;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.nitrous.iosched.client.data.json.EventData;
import com.nitrous.iosched.client.util.ConsoleLogger;
import com.nitrous.iosched.client.util.Util;

/**
 * Describes the duration of a session
 * @author nick
 *
 */
public class TimeSlot {
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	private Date startDate;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");
	private static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm Z");
	private static DateTimeFormat hMMFormat = DateTimeFormat.getFormat("H:mm");
	private static DateTimeFormat hhMMFormat = DateTimeFormat.getFormat("HH:mm");
	
	/**
	 * Parse the TimeSlot from the specified EventData
	 * @param data
	 * @return
	 */
	public static TimeSlot parse(EventData data) {
		TimeSlot slot = new TimeSlot(data.getStartDate(), data.getStartTime(), data.getEndDate(), data.getEndTime());
		if (ConsoleLogger.isDebugEnabled()) {
			ConsoleLogger.debug("Parsed slot "
					+data.getStartDate() + " " + data.getStartTime()
					+"->"
					+data.getEndDate() + " " + data.getEndTime()
					+" as "
					+dateTimeFormat.format(slot.getStartTime(), Util.TIMEZONE)
					+"->"
					+dateTimeFormat.format(slot.getEndTime(), Util.TIMEZONE)
					+" " 
					+ data.getTitle());
		}
		return slot;
	}
	
	/**
	 * Constructor
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 */
	@SuppressWarnings("deprecation")
	public TimeSlot(String startDate, String startTime, String endDate, String endTime) {
		// yyyy-MM-ddHH:mmZZZZ
		StringBuffer buf = new StringBuffer(startDate);
		buf.append(" 00:00 -0700");
		this.startDate = dateTimeFormat.parse(buf.toString());
		this.startDate.setHours(0);
		this.startDate.setMinutes(0);
		this.startDate.setSeconds(0);

		buf = new StringBuffer(endDate);
		buf.append(" 00:00 -0700");
		this.endDate = dateTimeFormat.parse(buf.toString());
		this.endDate.setHours(0);
		this.endDate.setMinutes(0);
		this.endDate.setSeconds(0);
		
		String reformattedTime = hhMMFormat.format(hMMFormat.parse(startTime)); 
		buf = new StringBuffer(startDate);
		buf.append(" ").append(reformattedTime).append(" -0700");
		this.startTime = dateTimeFormat.parse(buf.toString());
		this.startTime.setSeconds(0);
		this.startHour = this.startTime.getHours();
		this.startMinute = this.startTime.getMinutes();
		
		reformattedTime = hhMMFormat.format(hMMFormat.parse(endTime));
		buf = new StringBuffer(endDate);
		buf.append(" ").append(reformattedTime).append(" -0700");
		this.endTime = dateTimeFormat.parse(buf.toString());
		this.endTime.setSeconds(0);
		this.endHour = this.endTime.getHours();
		this.endMinute = this.endTime.getMinutes();		
	}
	
	/**
	 * 
	 * @return The start date and time
	 */
	public Date getStartTime() {
		return this.startTime;
	}
	
	/**
	 * 
	 * @return the end date and time
	 */
	public Date getEndTime() {
		return this.endTime;
	}
	
	/**
	 * 
	 * @return the start hour
	 */
	public int getStartHour() {
		return startHour;
	}
	/**
	 * 
	 * @return the start minute
	 */
	public int getStartMinute() {
		return startMinute;
	}
	/**
	 * 
	 * @return the end hour
	 */
	public int getEndHour() {
		return endHour;
	}
	/**
	 * 
	 * @return the end minute
	 */
	public int getEndMinute() {
		return endMinute;
	}
	/**
	 * 
	 * @return the start date (with 0 hours, 0 minutes and 0 seconds)
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 
	 * @return the end date (with 0 hours, 0 minutes and 0 seconds)
	 */
	public Date getEndDate() {
		return endDate;
	}
	
}
