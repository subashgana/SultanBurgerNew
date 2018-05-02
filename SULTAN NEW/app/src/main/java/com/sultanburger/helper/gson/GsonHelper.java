package com.sultanburger.helper.gson;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sultanburger.utils.Validator;

import java.lang.reflect.Type;
import java.util.List;

public class GsonHelper {

    private static final String TAG = GsonHelper.class.getSimpleName();

    private static GsonHelper gsonHelper;
    private static Gson gson;

    private Context context;

    public GsonHelper(Context context) {
        this.context = context;

        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    public static GsonHelper init(@NonNull Context context) {
        if (!Validator.isValid(gsonHelper))
            gsonHelper = new GsonHelper(context);

        return gsonHelper;
    }

    public static String toString(Object data) {
        return gson.toJson(data);
    }

    public static String toString(List<?> data) {
        return gson.toJson(data);
    }

    public static <T> List<T> fromString(String data, Class<T> clazz) {
        Type type = new ListParameterizeType<>(clazz);
        return gson.fromJson(data, type);
    }
}
