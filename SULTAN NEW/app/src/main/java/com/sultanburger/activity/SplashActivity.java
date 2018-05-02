package com.sultanburger.activity;

import android.os.Bundle;

import com.sultanburger.ActivityConfig;
import com.sultanburger.AppBaseActivity;
import com.sultanburger.data.DangerousPermission;
import com.sultanburger.helper.permission.PermissionCallback;
import com.sultanburger.helper.permission.PermissionHelper;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppBaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHelper = PermissionHelper.init();

        init();
    }

    @Override
    public void init() {
        checkPermissionAccess();
    }

    private void checkPermissionAccess() {
        final List<DangerousPermission> requestDangerousPermissions = new ArrayList<>();
        requestDangerousPermissions.add(DangerousPermission.READ_PHONE_STATE);
        requestDangerousPermissions.add(DangerousPermission.WRITE_EXTERNAL_STORAGE);
        requestDangerousPermissions.add(DangerousPermission.ACCESS_FINE_LOCATION);
        requestDangerousPermissions.add(DangerousPermission.RECEIVE_SMS);
        requestDangerousPermissions.add(DangerousPermission.CAMERA);

        permissionHelper.requestPermission(this, requestDangerousPermissions, new PermissionCallback() {
            @Override
            public void result(List<DangerousPermission> granted, List<DangerousPermission> denied) {
                if (granted.size() == requestDangerousPermissions.size()) {
                    Utils.makeDelay(2000, new Utils.HandlerCallBack() {
                        @Override
                        public void onDelayCompleted() {
                            ActivityConfig.startOptionActivity(SplashActivity.this);
                        }
                    });
                }

                for (DangerousPermission dangerousPermission : denied) {
                    if (dangerousPermission == DangerousPermission.READ_PHONE_STATE)
                        ToastUtil.showToast(getApplicationContext(), "READ_PHONE_STATE permission denied");

                    if (dangerousPermission == DangerousPermission.WRITE_EXTERNAL_STORAGE)
                        ToastUtil.showToast(getApplicationContext(), "WRITE_EXTERNAL_STORAGE permission denied");

                    if (dangerousPermission == DangerousPermission.ACCESS_FINE_LOCATION)
                        ToastUtil.showToast(getApplicationContext(), "ACCESS_FINE_LOCATION permission denied");

                    if (dangerousPermission == DangerousPermission.RECEIVE_SMS)
                        ToastUtil.showToast(getApplicationContext(), "RECEIVE_SMS permission denied");

                    if (dangerousPermission == DangerousPermission.CAMERA)
                        ToastUtil.showToast(getApplicationContext(), "CAMERA permission denied");
                }
            }

            @Override
            public boolean shouldShowRequestPermissionRationale() {
                return true;
            }

            @Override
            public void permissionRationalePositiveClicked() {
                checkPermissionAccess();
            }

            @Override
            public void permissionRationaleNegativeClicked() {
                ToastUtil.showToast(getApplicationContext(), SOME_PERMISSION_DENIED);
            }
        });
    }
}
