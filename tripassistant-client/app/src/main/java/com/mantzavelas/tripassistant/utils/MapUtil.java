package com.mantzavelas.tripassistant.utils;

import com.mantzavelas.tripassistant.exceptions.UnParseableCoordinateException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum MapUtil {;

    private static final String GET_TOKEN = "get";

    public static <T> String getLatitudeFromReflection(T object) {
        try {
            Method getLatMethod =  object.getClass().getDeclaredMethod("getLatitude");
            return parseCoordinateObject(getLatMethod.invoke(object));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T> String getLongitudeFromReflection(T object) {
        try {
            Method getLatMethod = object.getClass().getMethod(GET_TOKEN + "Longitude");
            return parseCoordinateObject(getLatMethod.invoke(object));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T> String getMapPointTitleFromReflection(T object) throws InvocationTargetException, IllegalAccessException {
        Method getTitleMethod;
        try {
            getTitleMethod = object.getClass().getMethod(GET_TOKEN + "Name");
        } catch (NoSuchMethodException e) {
            try {
                getTitleMethod = object.getClass().getMethod(GET_TOKEN + "Title");
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
                throw new RuntimeException("No suitable method found for title");
            }
        }

        return (String)getTitleMethod.invoke(object);
    }

    private static String parseCoordinateObject(Object coordinate) {
        if (coordinate instanceof String) {
            return (String)coordinate;
        } else if (coordinate instanceof Double) {
            return Double.toString((double)coordinate);
        } else if (coordinate instanceof Integer) {
            return Integer.toString((int)coordinate);
        }

        throw new UnParseableCoordinateException("Could not parse co-ordinate:: " + coordinate);
    }
}
