package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class DurationFormatterTest {

	@Test
	public void zeroMinutes() {
		DurationFormatter fmt = new DurationFormatter(0);
		assertEquals("0 minutes", fmt.format());
	}
	
	@Test
	public void oneMinute() {
		DurationFormatter fmt = new DurationFormatter(1);
		assertEquals("1 minute", fmt.format());
	}
	
	@Test
	public void oneHour() {
		DurationFormatter fmt = new DurationFormatter(60);
		assertEquals("1 hour", fmt.format());
	}
	
	@Test
	public void twoHours() {
		DurationFormatter fmt = new DurationFormatter(2*60);
		assertEquals("2 hours", fmt.format());
	}
	
	@Test
	public void twoHoursThirty() {
		DurationFormatter fmt = new DurationFormatter(2*60+30);
		assertEquals("2 hours, 30 minutes", fmt.format());
	}
	
	@Test
	public void oneDay() {
		DurationFormatter fmt = new DurationFormatter(60*24);
		assertEquals("1 day", fmt.format());
	}
	
	@Test
	public void twoDaysTwelveHours() {
		DurationFormatter fmt = new DurationFormatter(60*60);
		assertEquals("2 days, 12 hours", fmt.format());
	}
	
	@Test
	public void oneWeek() {
		DurationFormatter fmt = new DurationFormatter(60*24*7);
		assertEquals("1 week", fmt.format());
	}

	@Test
	public void oneWeekThreeDays() {
		DurationFormatter fmt = new DurationFormatter(60*24*17);
		assertEquals("2 weeks, 3 days", fmt.format());
	}
}
