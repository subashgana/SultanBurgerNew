package com.sultanburger.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class VersionUtil {

    private static final String TAG = VersionUtil.class.getSimpleName();

    public static int getCurrentVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isHoneyComb() {
        if (getCurrentVersion() >= Build.VERSION_CODES.HONEYCOMB)
            return true;
        else
            return false;
    }

    public static boolean isHoneyCombMR1() {
        if (getCurrentVersion() >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return true;
        else
            return false;
    }

    public static boolean isHoneyCombMR2() {
        if (getCurrentVersion() >= Build.VERSION_CODES.HONEYCOMB_MR2)
            return true;
        else
            return false;
    }

    public static boolean isIceCreamSandwich() {
        if (getCurrentVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return true;
        else
            return false;
    }

    public static boolean isIceCreamSandwichMR1() {
        if (getCurrentVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            return true;
        else
            return false;
    }

    public static boolean isJellyBean() {
        if (getCurrentVersion() >= Build.VERSION_CODES.JELLY_BEAN)
            return true;
        else
            return false;
    }

    public static boolean isJellyBeanMR1() {
        if (getCurrentVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return true;
        else
            return false;
    }

    public static boolean isJellyBeanMR2() {
        if (getCurrentVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            return true;
        else
            return false;
    }

    public static boolean isKitkat() {
        if (getCurrentVersion() >= Build.VERSION_CODES.KITKAT)
            return true;
        else
            return false;
    }

    public static boolean isLollipop() {
        if (getCurrentVersion() >= Build.VERSION_CODES.LOLLIPOP)
            return true;
        else
            return false;
    }

    public static boolean isLollipopMR1() {
        if (getCurrentVersion() >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return true;
        else
            return false;
    }

    public static boolean isMarshmallow() {
        if (getCurrentVersion() >= Build.VERSION_CODES.M)
            return true;
        else
            return false;
    }

    public static boolean isNougat() {
        if (getCurrentVersion() >= Build.VERSION_CODES.N)
            return true;
        else
            return false;
    }

    public static boolean isNougatMR1() {
        if (getCurrentVersion() >= Build.VERSION_CODES.N_MR1)
            return true;
        else
            return false;
    }
}
