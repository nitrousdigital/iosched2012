package com.nitrous.iosched.client.view.home.countdown;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.nitrous.iosched.client.util.TimeUtil;

public class CountDown {
	private static final long SECOND = 1000;
	private static final long MINUTE = 60 * SECOND;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;
	
	private static final int MAX_DAYS = 999;
	private int days = 0;
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;
	private Timer timer;
	
	private long targetMillis;
	
	public static interface View {
		void onCountDownUpdate();
	}
	
	private View view;
	
	public CountDown(View view) {
		this(getTargetDate(), view);
	}
	
	public CountDown(Date target, View view) {
		this.view = view;
		long now = TimeUtil.getCurrentTimeMillis();
		targetMillis = target.getTime();
		if (targetMillis > now) {
			long timeRemaining = getTimeRemainingMillis();
			updateClockUnits(timeRemaining);
		}
	}
	
	public CountDown(int days, int hours, int minutes, int seconds) {
		super();
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	private long getTimeRemainingMillis() {
		long now = TimeUtil.getCurrentTimeMillis();
		long remaining = 0;
		if (targetMillis > now) {
			remaining = targetMillis - now;
		}
		return remaining;
	}
	
	private boolean updateClockUnits(long millis) {
		boolean didChange = false;
		
		int newDays = (int)(millis / DAY); 
		millis -= (DAY * newDays);
		if (newDays > MAX_DAYS) {
			newDays = MAX_DAYS;
		}
		
		int newHours = (int)(millis / HOUR);
		millis -= (HOUR * newHours);
		
		int newMinutes = (int)(millis / MINUTE);
		millis -= (MINUTE * newMinutes);

		int newSeconds = (int)(millis / 1000);
		
		if (newDays != days || newHours != hours || newMinutes != minutes || newSeconds != seconds) {
			didChange = true;
			days = newDays;
			hours = newHours;
			minutes = newMinutes;
			seconds = newSeconds;
		}
		return didChange;
	}

	public boolean isExpired() {
		tick();
		int days = Math.max(getDays(), 0);
		int hours = Math.max(getHours(), 0);
		int minutes = Math.max(getMinutes(), 0);
		int seconds = Math.max(getSeconds(), 0);
		return days + hours + minutes + seconds == 0;
	}
	
	/**
	 * Update the countdown
	 * @return True if the countdown days, hours, minutes or seconds did change as a result of this tick.
	 */
	public boolean tick() {
		return updateClockUnits(getTimeRemainingMillis());
	}
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * Retrieve the countdown target date from either the URL argument 'targetdate' or the JavaScript global variable 'targetdate'.
	 * The value of the argument is expected to be in the format "MMddyyyyHHmmZZZZ"
	 * If both are undefined, then a default date is returned.
	 * @return The countdown target date
	 */
	private static Date getTargetDate() {
		String dateStr = null;
		Map<String, List<String>> args = Window.Location.getParameterMap();
		if (args != null) {
			List<String> dates = args.get("targetdate");
			if (dates != null && dates.size() > 0) {
				 dateStr = dates.get(0); 
			}
		}
		if (dateStr == null) {
			dateStr = getNativeDate();
		}
		if (dateStr == null || dateStr.trim().length() == 0) {
			dateStr = "062720120900GMT-07:00";
		}
		
		DateTimeFormat format = DateTimeFormat.getFormat("MMddyyyyHHmmZZZZ");
		return format.parse(dateStr);
	}

	private static native final String getNativeDate()/*-{
		if ($wnd.targetdate) {
			return $wnd.targetdate;
		} else {
			return null;
		}
	}-*/;

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	public void start() {
		stop();
		
		// update the animation 33 times per second
		timer = new com.google.gwt.user.client.Timer(){
			public void run() {
				boolean didChange = tick();
				if (didChange) {
					view.onCountDownUpdate();
				}
			}
		};
		timer.scheduleRepeating(100);
		
		// initial update
		tick();
		view.onCountDownUpdate();
	}
	
}
