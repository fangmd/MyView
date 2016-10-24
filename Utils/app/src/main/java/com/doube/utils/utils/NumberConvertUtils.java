package com.doube.utils.utils;

/**
 * Created by double on 2016/9/29.
 */

public class NumberConvertUtils {

    public static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public static boolean convertToBoolean(Object value, boolean defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value.toString());
        } catch (Exception e) {
            try {
                return Boolean.valueOf(value.toString());
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public static boolean convertToBoolean(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            return false;
        }
        try {
            return Boolean.parseBoolean(value.toString());
        } catch (Exception e) {
            try {
                return Boolean.valueOf(value.toString());
            } catch (Exception e1) {
                return false;
            }
        }
    }

    public static double convertToDouble(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception e1) {
                return 0.0;
            }
        }
    }
}
