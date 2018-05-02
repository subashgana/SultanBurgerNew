package com.sultanburger.helper.permission;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.sultanburger.data.DangerousPermission;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity implements PermissionConstants {

    private static final String TAG = PermissionActivity.class.getSimpleName();
    private Map<DangerousPermission, Integer> requestedPermissions;
    private PermissionCallback permissionCallback;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_PERMISSION_MULTIPLE:
                if (grantResults.length > 0) {
                    List<DangerousPermission> granted = new ArrayList<>();
                    List<DangerousPermission> denied = new ArrayList<>();

                    boolean isAllPermissionsGranted = true;
                    List<String> permissionRationale = new ArrayList<>();

                    for (int i = 0; i < permissions.length; i++)
                        requestedPermissions.put(DangerousPermission.toEnum(permissions[i]), grantResults[i]);

                    for (DangerousPermission key : requestedPermissions.keySet()) {
                        if (Validator.isValid(key)) {
                            if (requestedPermissions.get(key) == PackageManager.PERMISSION_GRANTED) {
                                granted.add(key);
                            } else {
                                isAllPermissionsGranted = false;
                                denied.add(key);
                            }

                            boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, key.toString());
                            if (shouldShowRequestPermissionRationale)
                                permissionRationale.add(key.toString());
                        }
                    }

                    if (Validator.isValid(permissionCallback)) {
                        if (isAllPermissionsGranted) {
                            permissionCallback.result(granted, denied);
                        } else {
                            if (!permissionRationale.isEmpty() && permissionCallback.shouldShowRequestPermissionRationale()) {
                                String permissionRationaleMessage = PermissionUtil.getPermissionRationaleMessage(permissionRationale);

                                PermissionUtil.showDialog(this, permissionRationaleMessage, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                permissionCallback.permissionRationalePositiveClicked();
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                permissionCallback.permissionRationaleNegativeClicked();
                                                break;
                                        }
                                    }
                                });
                            } else {
                                ToastUtil.showToast(getApplicationContext(), SOME_PERMISSION_DENIED);
                            }
                        }
                    }
                }
        }
    }

    public void setRequestedPermission(List<DangerousPermission> dangerousPermissions) {
        if (!Validator.isValid(requestedPermissions))
            requestedPermissions = new HashMap<>();

        for (DangerousPermission dangerousPermission : dangerousPermissions) {
            requestedPermissions.put(dangerousPermission, PackageManager.PERMISSION_GRANTED);
        }
    }

    public void setPermissionCallback(PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
    }
}
