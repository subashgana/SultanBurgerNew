package com.sultanburger.helper.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;

import com.sultanburger.data.CommonPermission;
import com.sultanburger.data.DangerousPermission;
import com.sultanburger.utils.StringUtil;
import com.sultanburger.utils.Validator;

import java.util.List;

class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();

    public static boolean hasPermission(Context context, DangerousPermission dangerousPermission) throws Exception {
        return hasPermission(context, dangerousPermission.toString());
    }

    public static boolean hasPermission(Context context, CommonPermission commonPermission) throws Exception {
        return hasPermission(context, commonPermission.toString());
    }

    private static boolean hasPermission(Context context, String permission) throws Exception {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (Validator.isValid(packageInfo.requestedPermissions)) {
                for (String tempPermission : packageInfo.requestedPermissions) {
                    if (tempPermission.equals(permission))
                        return true;
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return false;
    }

    public static String getPermissionRationaleMessage(List<String> permissionRationale) {
        String retVal = "";

        if (Validator.isValid(permissionRationale)) {
            retVal = StringUtil.collectionToCSV(permissionRationale);
            retVal += permissionRationale.size() > 1 ? " permission is " : " permissions are ";
            retVal += "required. Did you like to grant permission now?";
        }

        return retVal;
    }

    public static void showDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", onClickListener)
                .create()
                .show();
    }
}
