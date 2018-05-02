package com.sultanburger.rest;

import com.google.gson.annotations.Expose;

public class Result<T> {

    @Expose
    private boolean status;

    @Expose
    private String message;

    @Expose
    private T data;

    @Expose
    private Exception exception;

    public Result(T data) {
        this.data = data;
    }

    public Result(Exception exception) {
        super();
        this.exception = exception;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}