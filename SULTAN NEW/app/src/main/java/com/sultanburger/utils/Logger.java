package com.sultanburger.utils;

import android.util.Log;

public class Logger {

    public static void writeLog(String tag, String message) {
        Log.e("-----" + tag + "-----", message);
    }
}
