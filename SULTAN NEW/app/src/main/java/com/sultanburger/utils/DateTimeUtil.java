package com.sultanburger.utils;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

    private static final String TAG = DateTimeUtil.class.getSimpleName();

    private static final String dd_MM_yyyy_hh_mm_a = "dd/MM/yyyy hh:mm a";
    private static final String yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";
    private static final String dd_MM_yyyy = "dd/MM/yyyy";
    private static final String hh_mm_a = "hh:mm a";

    public static String getRelativeTime(long time) {
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS).toString();
    }

    public static String getddMMyyyyhhmma(long milliseconds) {
        return getFormattedDate(milliseconds, dd_MM_yyyy_hh_mm_a);
    }

    public static String getyyyyMMddHHmmss(long milliseconds) {
        return getFormattedDate(milliseconds, yyyyMMdd_HHmmss);
    }

    public static String getddMMyyyy(long milliseconds) {
        return getFormattedDate(milliseconds, dd_MM_yyyy);
    }

    public static String gethhmma(long milliseconds) {
        return getFormattedDate(milliseconds, hh_mm_a);
    }

    public static String getFormattedDate(long milliseconds, @NonNull String dateFormat) {
        return DateFormat.format(dateFormat, milliseconds).toString();
    }

    public static String getCountDown(long milliseconds) {
        return String.format("%02d min %02d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
