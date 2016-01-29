package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

public class DurationFormatter {
	private long minutes;
	private long hours;
	private long days;
	private long weeks;
	
	private final static long MINUTES_IN_HOUR = 60;
	private final static long HOURS_IN_DAY = 24;
	private final static long MINUTES_IN_DAY = MINUTES_IN_HOUR * HOURS_IN_DAY;
	private final static long DAYS_IN_WEEK = 7;
	private final static long MINUTES_IN_WEEK = MINUTES_IN_DAY * DAYS_IN_WEEK;
	
	public DurationFormatter(long minutes) {
		this.minutes = 0;
		this.hours = 0;
		this.days = 0;
		this.weeks = 0;
		parseMinutes(minutes);
	}
	
	private final void parseMinutes(long minutes) {
		if (minutes <= 0) return;
		
		if (minutes >= MINUTES_IN_WEEK) {
			this.weeks = minutes / MINUTES_IN_WEEK;
			minutes = minutes % MINUTES_IN_WEEK;
		}
		if (minutes >= MINUTES_IN_DAY) {
			this.days = minutes / MINUTES_IN_DAY;
			minutes = minutes % MINUTES_IN_DAY;
		}
		if (minutes >= MINUTES_IN_HOUR) {
			this.hours = minutes / MINUTES_IN_HOUR;
			minutes = minutes % MINUTES_IN_HOUR;
		}
		this.minutes = minutes;
	}
	
	public String format() {
		StringBuilder out = new StringBuilder();
		
		if (this.weeks > 0) {
			if (this.weeks == 1) {
				out.append("1 week");
			} else {
				out.append(this.weeks + " weeks");
			}
		}
		
		if (this.days > 0) {
			if (out.length() > 0) {
				out.append(", ");
			}
			if (this.days == 1) {
				out.append("1 day");
			} else {
				out.append(this.days + " days");
			}
		}
		
		if (this.hours > 0) {
			if (out.length() > 0) {
				out.append(", ");
			}
			if (this.hours == 1) {
				out.append("1 hour");
			} else {
				out.append(this.hours + " hours");
			}
		}
		
		if (out.length() == 0 || this.minutes > 0) {
			if (out.length() > 0) {
				out.append(", ");
			}
			if (this.minutes == 1) {
				out.append("1 minute");
			} else {
				out.append(this.minutes + " minutes");
			}
		}
			
		return out.toString();
	}
	
	@Override
	public String toString() {
		return this.format();
	}
}
