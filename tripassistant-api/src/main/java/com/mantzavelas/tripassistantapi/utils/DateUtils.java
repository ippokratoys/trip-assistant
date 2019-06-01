package com.mantzavelas.tripassistantapi.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String dateToUnixTimestamp(Date date) {
        long unixTime = (date.getTime()/1000);

        return Long.toString(unixTime);
    }

    public static Date addDaysToDate(Date date, int days) {
    	return addToDate(date,days, 0, 0, 0);
	}

    public static Date addSecondsToDate(Date date, int secondsToAdd) {
        return addToDate(date, 0, 0, 0, secondsToAdd);
    }

	public static Date addToDate(Date date, int days, int hours, int minutes, int secondsToAdd) {
		if (date!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			calendar.add(Calendar.DATE, days);
			calendar.add(Calendar.HOUR, hours);
			calendar.add(Calendar.MINUTE, minutes);
			calendar.add(Calendar.SECOND, secondsToAdd);

			return calendar.getTime();
		}

		return null;
	}

	public static boolean isDateWithinAWeek(Date date) {
    	Date aWeekAhead = addDaysToDate(new Date(), 7);

    	return !date.after(aWeekAhead);
	}

    public static int daysToMs(int days) {
    	return hoursToMs(24 * days);
	}

	public static int hoursToMs(int hours) {
    	return minsToMs(60 * hours);
	}

	public static int minsToMs(int minutes) {
    	return secsToMs(60 * minutes);
	}

	public static int secsToMs(int seconds) {
    	return 1000* seconds;
	}

	public static void main(String[] args) {
    	int result = daysToMs(1);
    	System.out.println(result);

    	Date result2 = addDaysToDate(new Date(), -3);
		System.out.println(result2);
	}
}
