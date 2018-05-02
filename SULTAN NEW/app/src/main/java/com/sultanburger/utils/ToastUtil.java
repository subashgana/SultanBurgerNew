package com.sultanburger.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

public class ToastUtil {

    private static final String TAG = ToastUtil.class.getSimpleName();

    private static ArrayList<Toast> toastList = null;

    public static void showToast(@NonNull final Context context, @NonNull final String message) {
        toast(context, message, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    }

    public static void showToast(@NonNull final Context context, @NonNull final String message, @NonNull int duration) {
        toast(context, message, Gravity.BOTTOM, duration);
    }

    public static void showToast(@NonNull final Context context, @NonNull final String message, @NonNull ToastPosition toastPosition) {
        switch (toastPosition) {
            case TOP:
                toast(context, message, Gravity.TOP, Toast.LENGTH_SHORT);
                break;

            case BOTTOM:
                toast(context, message, Gravity.BOTTOM, Toast.LENGTH_SHORT);
                break;

            case CENTER:
                toast(context, message, Gravity.CENTER, Toast.LENGTH_SHORT);
                break;
        }
    }

    public static void showToast(@NonNull final Context context, @NonNull final String message, @NonNull ToastPosition toastPosition, @NonNull int duration) {
        switch (toastPosition) {
            case TOP:
                toast(context, message, Gravity.TOP, duration);
                break;

            case BOTTOM:
                toast(context, message, Gravity.BOTTOM, duration);
                break;

            case CENTER:
                toast(context, message, Gravity.CENTER, duration);
                break;
        }
    }

    public static void clearAllToast() {
        if (Validator.isValid(toastList)) {
            for (Toast toast : toastList) {
                if (Validator.isValid(toast))
                    toast.cancel();
            }

            toastList.clear();
        }
    }

    private static void toast(@NonNull final Context context, @NonNull final String message, @NonNull final int gravity, @NonNull final int duration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, message, duration);
                toast.setGravity(gravity, 0, 100);
                toast.show();

                if (!Validator.isValid(toastList))
                    toastList = new ArrayList<>();

                toastList.add(toast);
            }
        });
    }

    public enum ToastPosition {
        TOP,
        BOTTOM,
        CENTER
    }
}
