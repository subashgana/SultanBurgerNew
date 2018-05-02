package com.sultanburger.helper.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sultanburger.data.DangerousPermission;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper implements PermissionConstants {

    private static final String TAG = PermissionHelper.class.getSimpleName();
    private static PermissionHelper permissionHelper;

    private PermissionHelper() {

    }

    public static PermissionHelper init() {
        if (!Validator.isValid(permissionHelper))
            permissionHelper = new PermissionHelper();

        return permissionHelper;
    }

    public boolean checkPermissionStatus(Context context, DangerousPermission dangerousPermission) {
        boolean retVal = false;

        int permissionStatus = ContextCompat.checkSelfPermission(context, dangerousPermission.toString());
        if (permissionStatus == PackageManager.PERMISSION_GRANTED)
            retVal = true;
        else if (permissionStatus == PackageManager.PERMISSION_DENIED)
            retVal = false;

        return retVal;
    }

    public void requestPermission(PermissionActivity activity, List<DangerousPermission> dangerousPermissions, PermissionCallback permissionCallback) {
        List<DangerousPermission> deniedDangerousPermission = getDeniedPermission(activity, dangerousPermissions);
        if (Validator.isValid(deniedDangerousPermission) && !deniedDangerousPermission.isEmpty()) {
            activity.setRequestedPermission(dangerousPermissions);
            activity.setPermissionCallback(permissionCallback);

            String[] requestPermission = new String[deniedDangerousPermission.size()];

            for (int i = 0; i < deniedDangerousPermission.size(); i++) {
                String data = deniedDangerousPermission.get(i).toString();
                requestPermission[i] = data;
            }

            ActivityCompat.requestPermissions(activity, requestPermission, REQUEST_ID_PERMISSION_MULTIPLE);
        } else {
            List<DangerousPermission> grantedDangerousPermission = getGrantedPermission(activity, dangerousPermissions);

            if (Validator.isValid(permissionCallback))
                permissionCallback.result(grantedDangerousPermission, deniedDangerousPermission);
        }
    }

    public void requestPermission(PermissionFragment fragment, List<DangerousPermission> dangerousPermissions, PermissionCallback permissionCallback) {
        List<DangerousPermission> deniedDangerousPermission = getDeniedPermission(fragment.getActivity(), dangerousPermissions);
        if (Validator.isValid(deniedDangerousPermission) && !deniedDangerousPermission.isEmpty()) {
            fragment.setRequestedPermission(dangerousPermissions);
            fragment.setPermissionCallback(permissionCallback);

            String[] requestPermission = new String[deniedDangerousPermission.size()];

            for (int i = 0; i < deniedDangerousPermission.size(); i++) {
                String data = deniedDangerousPermission.get(i).toString();
                requestPermission[i] = data;
            }

            fragment.requestPermissions(requestPermission, REQUEST_ID_PERMISSION_MULTIPLE);
        } else {
            List<DangerousPermission> grantedDangerousPermission = getGrantedPermission(fragment.getActivity(), dangerousPermissions);

            if (Validator.isValid(permissionCallback))
                permissionCallback.result(grantedDangerousPermission, deniedDangerousPermission);
        }
    }

    private List<DangerousPermission> getGrantedPermission(@NonNull Context context, @NonNull List<DangerousPermission> dangerousPermissions) {
        List<DangerousPermission> retVal = new ArrayList<>();

        Map<DangerousPermission, Integer> interResult = checkPermissionStatus(context, dangerousPermissions);
        for (DangerousPermission dangerousPermission : interResult.keySet()) {
            int permissionStatus = interResult.get(dangerousPermission);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED)
                retVal.add(dangerousPermission);
        }

        return retVal;
    }

    private List<DangerousPermission> getDeniedPermission(@NonNull Context context, @NonNull List<DangerousPermission> dangerousPermissions) {
        List<DangerousPermission> retVal = new ArrayList<>();

        Map<DangerousPermission, Integer> interResult = checkPermissionStatus(context, dangerousPermissions);
        for (DangerousPermission dangerousPermission : interResult.keySet()) {
            int permissionStatus = interResult.get(dangerousPermission);
            if (permissionStatus == PackageManager.PERMISSION_DENIED)
                retVal.add(dangerousPermission);
        }

        return retVal;
    }

    private Map<DangerousPermission, Integer> checkPermissionStatus(Context context, List<DangerousPermission> dangerousPermissions) {
        Map<DangerousPermission, Integer> retVal = new HashMap<>();

        if (Validator.isValid(dangerousPermissions)) {
            for (int i = 0; i < dangerousPermissions.size(); i++) {
                DangerousPermission dangerousPermission = dangerousPermissions.get(i);

                boolean permissionStatus = checkPermissionStatus(context, dangerousPermission);
                if (permissionStatus)
                    retVal.put(dangerousPermission, PackageManager.PERMISSION_GRANTED);
                else
                    retVal.put(dangerousPermission, PackageManager.PERMISSION_DENIED);
            }
        }

        return retVal;
    }
}
