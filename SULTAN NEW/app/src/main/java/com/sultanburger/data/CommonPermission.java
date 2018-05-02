package com.sultanburger.data;

import android.Manifest;

import java.util.HashMap;
import java.util.Map;

public enum CommonPermission {
    ACCESS_LOCATION_EXTRA_COMMANDS(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS),
    ACCESS_NETWORK_STATE(Manifest.permission.ACCESS_NETWORK_STATE),
    ACCESS_NOTIFICATION_POLICY(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
    ACCESS_WIFI_STATE(Manifest.permission.ACCESS_WIFI_STATE),
    BLUETOOTH(Manifest.permission.BLUETOOTH),
    BLUETOOTH_ADMIN(Manifest.permission.BLUETOOTH_ADMIN),
    BROADCAST_STICKY(Manifest.permission.BROADCAST_STICKY),
    CHANGE_NETWORK_STATE(Manifest.permission.CHANGE_NETWORK_STATE),
    CHANGE_WIFI_MULTICAST_STATE(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE),
    CHANGE_WIFI_STATE(Manifest.permission.CHANGE_WIFI_STATE),
    DISABLE_KEYGUARD(Manifest.permission.DISABLE_KEYGUARD),
    EXPAND_STATUS_BAR(Manifest.permission.EXPAND_STATUS_BAR),
    GET_PACKAGE_SIZE(Manifest.permission.GET_PACKAGE_SIZE),
    INSTALL_SHORTCUT(Manifest.permission.INSTALL_SHORTCUT),
    INTERNET(Manifest.permission.INTERNET),
    KILL_BACKGROUND_PROCESSES(Manifest.permission.KILL_BACKGROUND_PROCESSES),
    MODIFY_AUDIO_SETTINGS(Manifest.permission.MODIFY_AUDIO_SETTINGS),
    NFC(Manifest.permission.NFC),
    READ_SYNC_SETTINGS(Manifest.permission.READ_SYNC_SETTINGS),
    READ_SYNC_STATS(Manifest.permission.READ_SYNC_STATS),
    RECEIVE_BOOT_COMPLETED(Manifest.permission.RECEIVE_BOOT_COMPLETED),
    REORDER_TASKS(Manifest.permission.REORDER_TASKS),
    REQUEST_IGNORE_BATTERY_OPTIMIZATIONS(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS),
    REQUEST_INSTALL_PACKAGES(Manifest.permission.REQUEST_INSTALL_PACKAGES),
    SET_ALARM(Manifest.permission.SET_ALARM),
    SET_TIME_ZONE(Manifest.permission.SET_TIME_ZONE),
    SET_WALLPAPER(Manifest.permission.SET_WALLPAPER),
    SET_WALLPAPER_HINTS(Manifest.permission.SET_WALLPAPER_HINTS),
    TRANSMIT_IR(Manifest.permission.TRANSMIT_IR),
    UNINSTALL_SHORTCUT(Manifest.permission.UNINSTALL_SHORTCUT),
    USE_FINGERPRINT(Manifest.permission.USE_FINGERPRINT),
    VIBRATE(Manifest.permission.VIBRATE),
    WAKE_LOCK(Manifest.permission.WAKE_LOCK),
    WRITE_SYNC_SETTINGS(Manifest.permission.WRITE_SYNC_SETTINGS),
    SYSTEM_ALERT_WINDOW(Manifest.permission.SYSTEM_ALERT_WINDOW);

    private static final Map<String, CommonPermission> VALUES = new HashMap<>();

    static {
        for (CommonPermission type : values())
            VALUES.put(type.permissionValue, type);
    }

    private String permissionValue;

    CommonPermission(String permissionValue) {
        this.permissionValue = permissionValue;
    }

    public static CommonPermission toEnum(String permissionValue) {
        CommonPermission retVal = null;

        CommonPermission result = VALUES.get(permissionValue);
        if (result != null)
            retVal = result;

        return retVal;
    }

    @Override
    public String toString() {
        return permissionValue;
    }
}
