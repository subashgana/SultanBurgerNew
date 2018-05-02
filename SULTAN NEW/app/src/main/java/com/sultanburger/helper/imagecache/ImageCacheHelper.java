package com.sultanburger.helper.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sultanburger.R;
import com.sultanburger.data.Dimens;
import com.sultanburger.utils.DisplayUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

import java.io.File;

class ImageCacheHelper {

    private static final String TAG = ImageCacheHelper.class.getSimpleName();
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static ImageCacheHelper imageCacheHelper;
    private static ImageLoader imageLoader;
    private static DisplayImageOptions defaultDIO;
    private static DisplayImageOptions circularDIO;
    private static DisplayImageOptions customCircularDIO;
    private static DisplayImageOptions bannerDIO;
    private static int width;
    private static int height;
    private static File fileCacheDirectory;
    private static int cacheSize;

    private ImageCacheHelper(Context context) {
        width = DisplayUtil.getDeviceWidthInPixel(context);
        height = DisplayUtil.getDeviceHeightInPixel(context);
        fileCacheDirectory = StorageUtils.getCacheDirectory(context);
        cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache

        imageLoader = imageLoader(context);
        defaultDIO = defaultDIO();
        circularDIO = circularDIO();
        customCircularDIO = customCircularDIO(Color.WHITE, Color.WHITE);

        Dimens dimens = Utils.getBannerImageSize(context);
        bannerDIO = bannerDIO(dimens.getWidth(), dimens.getHeight());
    }

    public static ImageCacheHelper init(@NonNull Context context) {
        if (!Validator.isValid(imageCacheHelper))
            imageCacheHelper = new ImageCacheHelper(context);

        return imageCacheHelper;
    }

    public static DisplayImageOptions setCustomCircularDIO(int strokeColor, int backgroundColor) {
        if (Validator.isValid(imageCacheHelper))
            customCircularDIO = imageCacheHelper.customCircularDIO(strokeColor, backgroundColor);

        return customCircularDIO;
    }

    private ImageLoader imageLoader(Context context) {
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(width, height)
                .diskCacheExtraOptions(width, height, null)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(cacheSize))
                .memoryCacheSize(cacheSize)
                .memoryCacheSizePercentage(8)
                .diskCache(new UnlimitedDiskCache(fileCacheDirectory))
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new CustomBaseImageDownloader(context))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfiguration);
//        ImageLoader.getInstance().denyNetworkDownloads(true);
        return imageLoader;
    }

    private DisplayImageOptions defaultDIO() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_onloading)
//                .showImageForEmptyUri(R.drawable.ic_image_loading_failed)
//                .showImageOnFail(R.drawable.ic_image_loading_failed)
                .resetViewBeforeLoading(false)
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true) // Shows onProgressUpdate if TRUE
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(options)
//                .extraForDownloader()
                .displayer(new SimpleBitmapDisplayer())
                .build();

        return displayImageOptions;
    }

    private DisplayImageOptions circularDIO() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_onloading)
//                .showImageForEmptyUri(R.drawable.ic_image_loading_failed)
//                .showImageOnFail(R.drawable.ic_image_loading_failed)
                .resetViewBeforeLoading(false)
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true) // Shows onProgressUpdate if TRUE
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 10))
                .build();

        return displayImageOptions;
    }

    private DisplayImageOptions customCircularDIO(int strokeColor, int backgroundColor) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_onloading)
//                .showImageForEmptyUri(R.drawable.ic_image_loading_failed)
//                .showImageOnFail(R.drawable.ic_image_loading_failed)
                .resetViewBeforeLoading(false)
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true) // Shows onProgressUpdate if TRUE
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CustomCircleBitmapDisplayer(strokeColor, 10, backgroundColor))
                .build();

        return displayImageOptions;
    }

    private DisplayImageOptions bannerDIO(final int width, final int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_onloading)
//                .showImageForEmptyUri(R.drawable.ic_image_empty_url)
//                .showImageOnFail(R.drawable.ic_image_broken)
                .resetViewBeforeLoading(false)
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true) // Shows onProgressUpdate if TRUE
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(options)
                .postProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bmp) {
                        try {
                            return Bitmap.createScaledBitmap(bmp, width, height, false);
                        } catch (OutOfMemoryError e) {
                            // Add following line in AndroidManifest.xml
                            // Enable largeHeap
                            // <application android:largeHeap="true"
                            return bmp;
                        }
                    }
                })
                .displayer(new RoundedBitmapDisplayer(0))
                .build();

        return displayImageOptions;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void refreshImageLoader(@NonNull Context context) {
        imageLoader.destroy();
        imageLoader = imageLoader(context);
    }

    public DisplayImageOptions getDefaultDIO() {
        return defaultDIO;
    }

    public DisplayImageOptions getCircularDIO() {
        return circularDIO;
    }

    public DisplayImageOptions getBannerDIO() {
        return bannerDIO;
    }
}
