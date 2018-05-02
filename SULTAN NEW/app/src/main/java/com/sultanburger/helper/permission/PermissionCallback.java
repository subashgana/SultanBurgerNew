package com.sultanburger.helper.permission;

import com.sultanburger.data.DangerousPermission;

import java.util.List;

public interface PermissionCallback {
    void result(List<DangerousPermission> granted, List<DangerousPermission> denied);

    boolean shouldShowRequestPermissionRationale();

    void permissionRationalePositiveClicked();

    void permissionRationaleNegativeClicked();
}
