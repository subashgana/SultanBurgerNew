package com.sultanburger.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;

public class StringUtil {

    private static final String TAG = StringUtil.class.getSimpleName();

    public static String getValidData(String data) {
        if (TextUtils.isEmpty(data))
            return "";
        else if (data.equalsIgnoreCase("null"))
            return "";
        else
            return data;
    }

    public static String getValidData(String data, @NonNull String returnData) {
        if (TextUtils.isEmpty(data))
            return returnData;
        else if (data.equalsIgnoreCase("null"))
            return returnData;
        else
            return data;
    }

    public static String[] stringToArray(@NonNull String data) {
        String[] retVal = new String[data.length()];
        for (int i = 0; i < data.length(); i++) {
            retVal[i] = String.valueOf(data.charAt(i));
        }

        return retVal;
    }

    public static String collectionToCSV(@NonNull Collection<?> data) {
        return TextUtils.join(", ", data);
    }

    public static String mapValueToCSV(@NonNull Map<?, ?> data) {
        String retVal = null;

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (Validator.isValid(retVal))
                retVal += ", " + entry.getValue();
            else
                retVal = (String) entry.getValue();
        }

        return retVal;
    }

    public static String mapKeyToCSV(@NonNull Map<?, ?> data) {
        String retVal = null;

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (Validator.isValid(retVal))
                retVal += ", " + entry.getKey();
            else
                retVal = (String) entry.getKey();
        }

        return retVal;
    }

    public static String limitString(@NonNull String data, int length) {
        if (data.length() <= length)
            return data;
        else
            return data.substring(0, length - 2) + "...";
    }

    public static String doPadding(int data) {
        return data >= 10 ? String.valueOf(data) : ("0" + data);
    }


    public static String smartUpperCase(String data) {
        String retVal = null;

        if (Validator.isValid(data))
            retVal = Character.toUpperCase(data.charAt(0)) + (data.length() > 1 ? data.substring(1) : "");

        return retVal;
    }

    public static String smartLowerCase(String data) {
        String retVal = null;

        if (Validator.isValid(data))
            retVal = Character.toLowerCase(data.charAt(0)) + (data.length() > 1 ? data.substring(1) : "");

        return retVal;
    }
}
