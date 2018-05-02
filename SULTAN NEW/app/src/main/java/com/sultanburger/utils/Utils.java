package com.sultanburger.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sultanburger.AppConstants;
import com.sultanburger.R;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.item.ListDataItem;
import com.sultanburger.dialog.AlertList;
import com.sultanburger.dialog.handler.ListListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utils implements AppConstants {

    private static final String TAG = Utils.class.getSimpleName();

    public static void doImagePick(final Activity activity, final File fileForCamera) {
        List<ListDataItem> listDatumItems = new ArrayList<>();
        listDatumItems.add(ListDataItem.from(CAMERA, CAMERA));
        listDatumItems.add(ListDataItem.from(GALLERY, GALLERY));

        AlertList.showAlert(activity, AppConstants.SELECT, listDatumItems, new ListListener() {
            @Override
            public void onSelected(ListDataItem listDataItem) {
                switch (listDataItem.getId()) {
                    case CAMERA:
                        try {
                            Utils.doCamera(activity, fileForCamera, INTENT_RESULT_CAMERA);
                        } catch (Exception e) {
                            Logger.writeLog(TAG, "doImagePick -> " + e.getLocalizedMessage());
                        }
                        break;

                    case GALLERY:
                        Utils.doGallery(activity, false, INTENT_RESULT_GALLERY);
                        break;
                }
            }
        });
    }

    public static void doImagePick(Activity activity, final Fragment fragment, final File fileForCamera) {
        List<ListDataItem> listDatumItems = new ArrayList<>();
        listDatumItems.add(ListDataItem.from(CAMERA, CAMERA));
        listDatumItems.add(ListDataItem.from(GALLERY, GALLERY));

        AlertList.showAlert(activity, AppConstants.SELECT, listDatumItems, new ListListener() {
            @Override
            public void onSelected(ListDataItem listDataItem) {
                switch (listDataItem.getId()) {
                    case CAMERA:
                        try {
                            Utils.doCamera(fragment, fileForCamera, INTENT_RESULT_CAMERA);
                        } catch (Exception e) {
                            Logger.writeLog(TAG, "doImagePick -> " + e.getLocalizedMessage());
                        }
                        break;

                    case GALLERY:
                        Utils.doGallery(fragment, false, INTENT_RESULT_GALLERY);
                        break;
                }
            }
        });
    }

    public static void doCamera(@NonNull Activity activity, @NonNull File capturedImageLocation, int requestCode) {
        // Need to add FileProvider permission in app AndroidManifest, and XML file in res
        // (Already added) check for reference
        Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", capturedImageLocation);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    public static void doCamera(@NonNull Fragment fragment, @NonNull File capturedImageLocation, int requestCode) {
        Context context = fragment.getContext();

        // Need to add FileProvider permission in app AndroidManifest, and XML file in res
        // (Already added) check for reference
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", capturedImageLocation);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intentCamera, requestCode);
    }

    public static void doGallery(@NonNull Activity activity, boolean allowMultiSelect, int requestCode) {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
        intentGallery.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), requestCode);
    }

    public static void doGallery(@NonNull Fragment fragment, boolean allowMultiSelect, int requestCode) {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
        intentGallery.setAction(Intent.ACTION_PICK);
        fragment.startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), requestCode);
    }

    public static void makeDelay(long milliSeconds, final HandlerCallBack handlerCallBack) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handlerCallBack.onDelayCompleted();
            }
        }, milliSeconds);
    }

    public static void requestFocus(@NonNull Context context, @NonNull View view) {
        if (view.requestFocus())
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void changeCursorColor(View view) {
        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(view, R.drawable.background_cursor);
        } catch (Exception ignored) {

        }
    }

    public static void hideSoftKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void disableScreenShot(@NonNull Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static boolean isPlayServicesAvailable(@NonNull Context context) {
        boolean retVal = false;

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS)
            retVal = true;

        return retVal;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            return null;
        }
    }

    public static String getDeviceToken(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Dimens getBannerImageSize(@NonNull Context context) {
        int width = DisplayUtil.getDeviceWidthInPixel(context);
        int height = DisplayUtil.getDeviceHeightInPixel(context);

        return Dimens.from(width, (height / 4) - 50);
    }

    public static int getAvatarImageSize(@NonNull Context context) {
        int widthInPixel = DisplayUtil.getDeviceWidthInPixel(context);
        return (widthInPixel / 3) + 100;
    }

    public interface HandlerCallBack {
        void onDelayCompleted();
    }
}
