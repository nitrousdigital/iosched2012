package com.nitrous.iosched.client.data.json;

import java.util.Date;

public class EventSlotWrapper {
	private Date slotDate;
	private Date startTime;
	private Date endTime;
	private String name;
	public EventSlotWrapper(String name, Date slotDate, Date startTime, Date endTime) {
		this.name = name;
		this.slotDate = slotDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public Date getSlotDate() {
		return slotDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
}
