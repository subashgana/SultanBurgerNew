package com.sultanburger.utils;

import android.text.TextUtils;

public class Validator {

    private static final String TAG = Validator.class.getSimpleName();

    public static boolean isValid(Object object) {
        if (object == null)
            return false;
        else
            return true;
    }

    public static boolean isValid(int data) {
        if (data == 0)
            return false;
        else
            return true;
    }

    public static boolean isValid(String data) {
        if (TextUtils.isEmpty(data))
            return false;
        else
            return true;
    }

    public static boolean isValid(String[] data) {
        if (data != null && data.length > 0)
            return true;
        else
            return false;
    }

    public static boolean isValidFirstName(String firstName) {
        if (!TextUtils.isEmpty(firstName) && firstName.matches("[a-zA-Z]*"))
            return true;
        else
            return false;
    }

    public static boolean isValidLastName(String lastName) {
        if (lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*"))
            return true;
        else
            return false;
    }

    public static boolean isValidEmail(String email) {
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
            return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 10)
            return true;
        else
            return false;
    }

    public static String returnValidData(String data) {
        if (TextUtils.isEmpty(data))
            return "";
        else if (data.equalsIgnoreCase("null"))
            return "";
        else
            return data;
    }

    public static boolean contains(String data, String contains) {
        boolean retVal = false;

        if (Validator.isValid(data) && Validator.isValid(contains) && data.contains(contains))
            return true;

        return retVal;
    }
}
