package com.sultanburger.utils;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import java.net.InetAddress;

public class ConnectivityUtil {

    private static final String TAG = ConnectivityUtil.class.getSimpleName();

    public static boolean isInternetEnabled() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");
            return !inetAddress.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isGPSEnabled(@NonNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            return false;
        }
    }
}
