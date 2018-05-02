package com.sultanburger.rest;

public interface ResultReceiver<T> {
    void onCompleted(Result<T> result);

    void onFailed(Exception exception);
}