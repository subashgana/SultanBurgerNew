package com.sultanburger.helper.imagecache;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

class CustomBaseImageDownloader extends BaseImageDownloader {

    private static final String TAG = CustomBaseImageDownloader.class.getSimpleName();

    public CustomBaseImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        if (imageUri.startsWith(ImageUtil.IMAGE_STREAM))
            return (InputStream) extra;
        else
            return super.getStreamFromOtherSource(imageUri, extra);
    }

    @Override
    protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse(imageUri);

        if (imageUri.startsWith("content://com.android.contacts/"))
            return contentResolver.openInputStream(uri);
        else
            return contentResolver.openInputStream(uri);
    }
}
