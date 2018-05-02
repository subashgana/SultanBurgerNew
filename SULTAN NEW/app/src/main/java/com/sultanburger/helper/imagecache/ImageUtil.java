package com.sultanburger.helper.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.sultanburger.R;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Validator;

import java.io.File;

public class ImageUtil implements ImageCacheConstants {

    private static final String TAG = ImageUtil.class.getSimpleName();

    private static ImageCacheHelper imageCacheHelper;

    public static ImageLoader getImageLoader(Context context) {
        imageCacheHelper = ImageCacheHelper.init(context);
        return imageCacheHelper.getImageLoader();
    }

    public static void setImage(@NonNull Context context, @NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImage(context, imageView, path, defaultImage, imageCacheHelper.getDefaultDIO());
    }

    public static void setImage(@NonNull Context context, @NonNull final ImageView imageView, String path, @DrawableRes int defaultImage, @NonNull final ImageCacheCallback imageCacheCallback) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImage(context, imageView, path, defaultImage, imageCacheHelper.getDefaultDIO(), imageCacheCallback);
    }

    public static void setImageFromLocal(@NonNull Context context, @NonNull ImageView imageView, String localPath, @DrawableRes int defaultImage) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImageFromLocal(context, imageView, localPath, defaultImage, imageCacheHelper.getDefaultDIO());
    }

    public static void setCircularImage(@NonNull Context context, @NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        renderCircularImage(context, imageView, path, defaultImage, -1, -1);
    }

    public static void setCircularImage(@NonNull Context context, @NonNull ImageView imageView, String path, @DrawableRes int defaultImage, @NonNull int strokeColor, @NonNull int backgroundColor) {
        renderCircularImage(context, imageView, path, defaultImage, strokeColor, backgroundColor);
    }

    public static void setCircularImageFromLocal(@NonNull Context context, @NonNull ImageView imageView, String localPath, @DrawableRes int defaultImage) {
        renderCircularImageFromLocal(context, imageView, localPath, defaultImage, -1, -1);
    }

    public static void setCircularImageFromLocal(@NonNull Context context, @NonNull ImageView imageView, String localPath, @DrawableRes int defaultImage, @NonNull int strokeColor, @NonNull int backgroundColor) {
        renderCircularImageFromLocal(context, imageView, localPath, defaultImage, strokeColor, backgroundColor);
    }

    public static void setBannerImage(@NonNull Context context, @NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImage(context, imageView, path, defaultImage, imageCacheHelper.getBannerDIO());
    }

    public static void setBannerImage(@NonNull Context context, @NonNull final ImageView imageView, String path, @DrawableRes int defaultImage, @NonNull final ImageCacheCallback imageCacheCallback) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImage(context, imageView, path, defaultImage, imageCacheHelper.getBannerDIO(), imageCacheCallback);
    }

    public static void setBannerImageFromLocal(@NonNull Context context, @NonNull ImageView imageView, String localPath, @DrawableRes int defaultImage) {
        imageCacheHelper = ImageCacheHelper.init(context);
        renderImageFromLocal(context, imageView, localPath, defaultImage, imageCacheHelper.getBannerDIO());
    }

    public static void reomveImageFromCache(@NonNull Context context, @NonNull String imagePath) {
        removeImageFromDiscCache(context, imagePath);
        removeImageFromMemoryCache(context, imagePath);
    }

    public static void clearFromDiscCache(@NonNull Context context) {
        imageCacheHelper = ImageCacheHelper.init(context);
        imageCacheHelper.getImageLoader().clearDiskCache();
    }

    public static void removeImageFromDiscCache(@NonNull Context context, @NonNull String imagePath) {
        imageCacheHelper = ImageCacheHelper.init(context);

        DiskCache diskCache = imageCacheHelper.getImageLoader().getDiskCache();
        File file = diskCache.get(imagePath);
        if (file.exists())
            file.delete();
    }

    public static void clearFromMemoryCache(@NonNull Context context) {
        imageCacheHelper = ImageCacheHelper.init(context);
        imageCacheHelper.getImageLoader().clearMemoryCache();
    }

    public static void removeImageFromMemoryCache(@NonNull Context context, @NonNull String imagePath) {
        imageCacheHelper = ImageCacheHelper.init(context);

        MemoryCache memoryCache = imageCacheHelper.getImageLoader().getMemoryCache();
        memoryCache.remove(imagePath);
    }

    private static void renderImage(Context context, ImageView imageView, String path, int defaultImage, DisplayImageOptions displayImageOptions) {
        imageCacheHelper = ImageCacheHelper.init(context);

        if (Validator.isValid(path) && !path.equals("")) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(path)) {
                imageCacheHelper.getImageLoader().displayImage(path, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(path);
            }
        } else {
            renderDefaultImage(context, imageView, defaultImage, displayImageOptions);
        }
    }

    private static void renderImage(Context context, final ImageView imageView, String path, int defaultImage, DisplayImageOptions displayImageOptions, final ImageCacheCallback imageCacheCallback) {
        imageCacheHelper = ImageCacheHelper.init(context);

        if (Validator.isValid(path) && !path.equals("")) {
            imageCacheHelper.getImageLoader().displayImage(path, new ImageViewAware(imageView, false), displayImageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    imageCacheCallback.onStarted();
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    String reason = "Unknown error";

                    switch (failReason.getType()) {
                        case IO_ERROR:
                            reason = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            reason = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            reason = "Network download denied";
                            break;
                        case OUT_OF_MEMORY:
                            reason = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            reason = "Unknown error";
                            break;
                    }

                    imageCacheCallback.onFailed(reason);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    imageView.setTag(s);
                    imageCacheCallback.onCompleted();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    imageCacheCallback.onCancelled();
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String s, View view, int current, int total) {
                    imageCacheCallback.onLoading(current, total);
                }
            });
        } else {
            renderDefaultImage(context, imageView, defaultImage, displayImageOptions);
        }
    }

    private static void renderImageFromLocal(Context context, ImageView imageView, String localPath, int defaultImage, DisplayImageOptions displayImageOptions) {
        imageCacheHelper = ImageCacheHelper.init(context);

        if (Validator.isValid(localPath) && (localPath.startsWith(IMAGE_DRAWABLE) || localPath.startsWith(IMAGE_CONTENT) || localPath.startsWith(IMAGE_ASSETS) || localPath.startsWith(IMAGE_FILE))) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(localPath)) {
                imageCacheHelper.getImageLoader().displayImage(localPath, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(localPath);
            }
        } else {
            renderDefaultImage(context, imageView, defaultImage, displayImageOptions);
        }
    }

    private static void renderDefaultImage(Context context, ImageView imageView, int defaultImage, DisplayImageOptions displayImageOptions) {
        if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(defaultImage)) {
            Drawable drawable = DrawableUtil.getDrawable(context, defaultImage);
            if (drawable instanceof BitmapDrawable) {
                imageCacheHelper.getImageLoader().displayImage(IMAGE_DRAWABLE + defaultImage, new ImageViewAware(imageView, false), displayImageOptions);
            } else {
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), defaultImage, null);
                Bitmap bitmap = DrawableUtil.getBitmap(vectorDrawableCompat);

                DisplayImageOptions bitmapDIO = displayImageOptions;
                bitmapDIO.getDisplayer().display(bitmap, new ImageViewAware(imageView, false), LoadedFrom.MEMORY_CACHE);
            }

            imageView.setTag(defaultImage);
        }
    }

    private static void renderCircularImage(Context context, ImageView imageView, String path, int defaultImage, int strokeColor, int backgroundColor) {
        imageCacheHelper = ImageCacheHelper.init(context);

        if (Validator.isValid(path) && !path.equals("")) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(path)) {
                DisplayImageOptions displayImageOptions = imageCacheHelper.getCircularDIO();

                imageCacheHelper.getImageLoader().displayImage(path, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(path);
            }
        } else {
            renderDefaultCircularImage(context, imageView, defaultImage, strokeColor, backgroundColor);
        }
    }

    private static void renderCircularImageFromLocal(Context context, ImageView imageView, String localPath, int defaultImage, int strokeColor, int backgroundColor) {
        imageCacheHelper = ImageCacheHelper.init(context);

        if (Validator.isValid(localPath) && (localPath.startsWith(IMAGE_DRAWABLE) || localPath.startsWith(IMAGE_CONTENT) || localPath.startsWith(IMAGE_ASSETS) || localPath.startsWith(IMAGE_FILE))) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(localPath)) {
                DisplayImageOptions displayImageOptions = imageCacheHelper.getCircularDIO();

                imageCacheHelper.getImageLoader().displayImage(localPath, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(localPath);
            }
        } else {
            renderDefaultCircularImage(context, imageView, defaultImage, strokeColor, backgroundColor);
        }
    }

    private static void renderDefaultCircularImage(Context context, ImageView imageView, int defaultImage, int strokeColor, int backgroundColor) {
        if (strokeColor == -1)
            strokeColor = DrawableUtil.getColor(context, R.color.white);

        if (backgroundColor == -1)
            backgroundColor = DrawableUtil.getColor(context, R.color.windowBackground);

        if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(defaultImage)) {
            Drawable drawable = DrawableUtil.getDrawable(context, defaultImage);
            if (drawable instanceof BitmapDrawable) {
                imageCacheHelper.getImageLoader().displayImage(IMAGE_DRAWABLE + defaultImage, new ImageViewAware(imageView, false), imageCacheHelper.getCircularDIO());
            } else {
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), defaultImage, null);
                Bitmap bitmap = DrawableUtil.getBitmap(vectorDrawableCompat);

                DisplayImageOptions bitmapDIO = imageCacheHelper.setCustomCircularDIO(strokeColor, backgroundColor);
                bitmapDIO.getDisplayer().display(bitmap, new ImageViewAware(imageView, false), LoadedFrom.MEMORY_CACHE);
            }

            imageView.setTag(defaultImage);
        }
    }
}
