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
}
