package com.sultanburger.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageView;

import com.sultanburger.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawableUtil {

    private static final String TAG = DrawableUtil.class.getSimpleName();

    public static int getPrimaryColor(@NonNull Context context) {
        return getColorFromAttr(context, R.attr.colorPrimary);
    }

    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorFromAttr(context, R.attr.colorPrimaryDark);
    }

    private static int getColorFromAttr(@NonNull Context context, @AttrRes final int attributeColor) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, typedValue, true);
        return typedValue.data;
    }

    public static int getColor(@NonNull Context context, @ColorRes int color) {
        if (VersionUtil.isMarshmallow())
            return ContextCompat.getColor(context, color);
        else
            return context.getResources().getColor(color);
    }

    public static Drawable getDefaultDrawable(@NonNull Context context) {
        if (VersionUtil.isMarshmallow())
            return ContextCompat.getDrawable(context, android.R.mipmap.sym_def_app_icon);
        else
            return context.getResources().getDrawable(android.R.mipmap.sym_def_app_icon);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resource) {
        if (VersionUtil.isMarshmallow())
            return ContextCompat.getDrawable(context, resource);
        else
            return context.getResources().getDrawable(resource);
    }

    public static final Uri getUriFromResource(@NonNull Context context, @AnyRes int resId) throws Exception {
        try {
            Resources resources = context.getResources();
            Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + resources.getResourcePackageName(resId)
                    + '/' + resources.getResourceTypeName(resId)
                    + '/' + resources.getResourceEntryName(resId));
            return resUri;
        } catch (Resources.NotFoundException e) {
            throw new Exception(e);
        }
    }

    public static Bitmap getBitmap(@NonNull Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (Validator.isValid(bitmapDrawable.getBitmap()))
                return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Drawable resizeDrawable(@NonNull Context context, @NonNull Drawable drawable, int width, int height) {
        if ((Validator.isValid(drawable)) || !(drawable instanceof BitmapDrawable))
            return drawable;

        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), width, height, false);
        drawable = new BitmapDrawable(context.getResources(), bitmap);

        return drawable;
    }

    public static void changeImageColor(@NonNull final ImageView imageView, @ColorInt int fromColor, @ColorInt int toColor) {
        ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        imageColorChangeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                imageView.setColorFilter((Integer) animator.getAnimatedValue());
            }
        });
        imageColorChangeAnimation.setDuration(150);
        imageColorChangeAnimation.start();
    }

    public static Bitmap changeImageColor(@NonNull Bitmap bitmap, @ColorInt int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);

        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(color, 1));

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);

        return resultBitmap;
    }

    public static File compressImage(@NonNull File file, @NonNull float compressImageWidth, @NonNull float compressImageHeight) throws Exception {
        if (!file.exists())
            throw new FileNotFoundException("File does not exists");

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // By setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            int actualImageWidth = options.outWidth;
            int actualImageHeight = options.outHeight;

            float actualImageRatio = actualImageWidth / actualImageHeight;
            float compressImageRatio = compressImageWidth / compressImageHeight;

//          Width and Height values are set maintaining the aspect ratio of the image
            if (actualImageHeight > compressImageHeight || actualImageWidth > compressImageWidth) {
                if (actualImageRatio < compressImageRatio) {
                    actualImageRatio = compressImageHeight / actualImageHeight;
                    actualImageWidth = (int) (actualImageRatio * actualImageWidth);
                    actualImageHeight = (int) compressImageHeight;
                } else if (actualImageRatio > compressImageRatio) {
                    actualImageRatio = compressImageWidth / actualImageWidth;
                    actualImageHeight = (int) (actualImageRatio * actualImageHeight);
                    actualImageWidth = (int) compressImageWidth;
                } else {
                    actualImageHeight = (int) compressImageHeight;
                    actualImageWidth = (int) compressImageWidth;
                }
            }

//          Setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualImageWidth, actualImageHeight);
//          inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;
//          This options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            Bitmap scaledBitmap = Bitmap.createBitmap(actualImageWidth, actualImageHeight, Bitmap.Config.ARGB_8888);

            float ratioX = actualImageWidth / (float) options.outWidth;
            float ratioY = actualImageHeight / (float) options.outHeight;
            float middleX = actualImageWidth / 2.0f;
            float middleY = actualImageHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            Matrix matrix = new Matrix();
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            if (orientation == 6)
                matrix.postRotate(90);
            else if (orientation == 3)
                matrix.postRotate(180);
            else if (orientation == 8)
                matrix.postRotate(270);

            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            File compressImageFile = file;
            if (!compressImageFile.exists())
                compressImageFile.mkdirs();

            FileOutputStream fileOutputStream = new FileOutputStream(compressImageFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);

            return compressImageFile;
        } catch (OutOfMemoryError e) {
            throw new Exception(e);
        } catch (FileNotFoundException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredImageWidth, int requiredImageHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredImageHeight || width > requiredImageWidth) {
            final int heightRatio = Math.round((float) height / (float) requiredImageHeight);
            final int widthRatio = Math.round((float) width / (float) requiredImageWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = requiredImageWidth * requiredImageHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
