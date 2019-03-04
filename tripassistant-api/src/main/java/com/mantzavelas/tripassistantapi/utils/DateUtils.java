package com.mantzavelas.tripassistantapi.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String dateToUnixTimestamp(Date date) {
        long unixTime = (date.getTime()/1000);

        return Long.toString(unixTime);
    }

    public static Date addSecondsToDate(Date date, int secondsToAdd) {
        if (date!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, secondsToAdd);

            return calendar.getTime();
        }

        return null;
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
	}
}
