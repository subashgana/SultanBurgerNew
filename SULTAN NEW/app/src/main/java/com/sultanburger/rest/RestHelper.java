package com.sultanburger.rest;

import android.content.Context;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RestHelper {

    private static final String TAG = RestHelper.class.getSimpleName();

    public static void sendPost(Context context, String uri, Object payload, Type type, ResultReceiver resultReceiver) {
        sendPost(context, uri, payload, type, new ArrayList<Param>(), resultReceiver);
    }

    public static void sendPost(Context context, String uri, Object payload, Type type, List<? extends Param> params, ResultReceiver resultReceiver) {
        new RestExecutor<>(context, uri, payload, type, MethodType.POST, resultReceiver).execute(params.toArray(new Param[0]));
    }

    public static void sendGet(Context context, String uri, Type type, ResultReceiver resultReceiver) {
        sendGet(context, uri, type, new ArrayList<Param>(), resultReceiver);
    }

    public static void sendGet(Context context, String uri, Type type, List<? extends Param> params, ResultReceiver resultReceiver) {
        new RestExecutor<>(context, uri, null, type, MethodType.GET, resultReceiver).execute(params.toArray(new Param[0]));
    }

    public static void sendPut(Context context, String uri, Object payload, Type type, ResultReceiver resultReceiver) {
        sendPut(context, uri, payload, type, new ArrayList<Param>(), resultReceiver);
    }

    public static void sendPut(Context context, String uri, Object payload, Type type, List<? extends Param> params, ResultReceiver resultReceiver) {
        new RestExecutor<>(context, uri, payload, type, MethodType.PUT, resultReceiver).execute(params.toArray(new Param[0]));
    }

    public static void sendDelete(Context context, String uri, Type type, ResultReceiver resultReceiver) {
        sendDelete(context, uri, type, new ArrayList<Param>(), resultReceiver);
    }

    public static void sendDelete(Context context, String uri, Type type, List<? extends Param> params, ResultReceiver resultReceiver) {
        new RestExecutor<>(context, uri, null, type, MethodType.DELETE, resultReceiver).execute(params.toArray(new Param[0]));
    }
}
