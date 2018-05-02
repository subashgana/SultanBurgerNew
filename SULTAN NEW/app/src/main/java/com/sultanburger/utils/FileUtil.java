package com.sultanburger.utils;

import android.content.ClipData;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static File getRootDirectory(@NonNull String appName) {
        return new File(android.os.Environment.getExternalStorageDirectory(), appName);
    }

    public static File makeDirectory(@NonNull String appName, boolean createNoMediaFile, String... paths) throws Exception {
        String tempPath = "";
        for (String path : paths) {
            tempPath += File.separator + path;
        }

        File file = null;
        if (Validator.isValid(tempPath)) {
            file = new File(getRootDirectory(appName), File.separator + tempPath);
            file.mkdirs();
        }

        if (Validator.isValid(file) && createNoMediaFile)
            createNoMediaFile(file);

        return file;
    }

    public static boolean createNoMediaFile(File file) throws Exception {
        boolean status;

        try {
            File noMediaFile = new File(file, ".nomedia");
            status = noMediaFile.createNewFile();
        } catch (IOException e) {
            throw new Exception(e);
        }

        return status;
    }

    public static boolean copyFile(@NonNull File sourceFile, @NonNull File destinationFile) throws Exception {
        if (!sourceFile.exists())
            throw new FileNotFoundException("Source file does not exists");

        try {
            FileChannel fileChannelSource;
            fileChannelSource = new FileInputStream(sourceFile).getChannel();

            FileChannel fileChannelDestination;
            fileChannelDestination = new FileOutputStream(destinationFile).getChannel();

            if (fileChannelDestination != null && fileChannelSource != null)
                fileChannelDestination.transferFrom(fileChannelSource, 0, fileChannelSource.size());

            fileChannelSource.close();
            fileChannelDestination.close();
        } catch (IOException e) {
            throw new Exception(e);
        }

        return true;
    }

    public static ArrayList<File> processSelectedImages(@NonNull Context context, @NonNull Intent data) {
        ArrayList<File> retVal = new ArrayList<>();

        if (Validator.isValid(data)) {
            try {
//                Uri uri = data.getData();
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                if (VersionUtil.isJellyBean()) {
                    ClipData clipdata = data.getClipData();
                    if (Validator.isValid(clipdata)) {
                        for (int i = 0; i < clipdata.getItemCount(); i++) {
                            Uri uri = clipdata.getItemAt(i).getUri();

                            if (uri.toString().startsWith("content://com.google.android.apps.photos.contentprovider/0/1"))
                                uri = Uri.parse(getImageFromGooglePhoto(context, uri));

                            retVal.add(new File(getAbsolutePathFromURI(context, uri)));
                        }
                    } else {
                        retVal.add(new File(getAbsolutePathFromURI(context, data.getData())));
                    }
                } else {
                    retVal.add(new File(getAbsolutePathFromURI(context, data.getData())));
                }
            } catch (Exception e) {
                Logger.writeLog(TAG, "processSelectedImages -> " + e.getLocalizedMessage());
            }
        } else {
            Logger.writeLog(TAG, "processSelectedImages -> Intent data is null");
        }

        return retVal;
    }

    public static String getImageFromGooglePhoto(@NonNull Context context, @NonNull Uri uri) {
        String retVal = null;

        InputStream inputStream = null;

        try {
            if (Validator.isValid(uri.getAuthority())) {
                inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                retVal = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", "Description");
            }
        } catch (FileNotFoundException e) {
            Logger.writeLog(TAG, "getImageFromGooglePhoto -> " + e.getLocalizedMessage());
        } finally {
            try {
                if (Validator.isValid(inputStream))
                    inputStream.close();
            } catch (IOException e) {
                Logger.writeLog(TAG, "getImageFromGooglePhoto -> " + e.getLocalizedMessage());
            }
        }

        return retVal;
    }

    public static String getAbsolutePathFromURI(@NonNull Context context, @NonNull Uri uri) {
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
            cursor = loader.loadInBackground();

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (Validator.isValid(cursor))
                cursor.close();
        }
    }
}
