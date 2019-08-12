package com.mantzavelas.tripassistantapi.models;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public enum Seasons {
	AUTUMN(8, 9, 10),
	WINTER(11, 0, 0),
	SPRING(2, 3, 4),
	SUMMER(5, 6, 7),
	NONE(-1);

	int[] months;
	public int[] getMonths() { return months; }
	public void setMonths(int[] months) { this.months = months; }

	Seasons(int... months) { this.months = months; }

	public static Seasons getSeason(int month) {
		return Arrays.stream(Seasons.values())
			  .filter(s -> ArrayUtils.contains(s.getMonths(), month))
			  .findFirst()
			  .orElse(NONE);
	}

	public static Seasons getCurrentSeason() {
		Date date = Calendar.getInstance().getTime();

		return Seasons.getSeason(date.getMonth());
	}

	public static boolean isInCurrentSeason(Date date) {

		if (date == null) {
			return false;
		}

		return getSeason(date.getMonth()).equals(getCurrentSeason());
	}
}
