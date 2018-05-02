package com.sultanburger.helper.permission;

public class PermissionException extends Exception {

    private static final String TAG = PermissionException.class.getSimpleName();

    public PermissionException() {

    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
