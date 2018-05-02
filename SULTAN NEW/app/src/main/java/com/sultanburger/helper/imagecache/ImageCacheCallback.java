package com.sultanburger.helper.imagecache;

public interface ImageCacheCallback {
    void onStarted();

    void onFailed(String reason);

    void onCompleted();

    void onCancelled();

    void onLoading(int current, int total);
}
