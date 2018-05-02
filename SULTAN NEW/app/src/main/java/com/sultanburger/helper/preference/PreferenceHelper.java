package com.sultanburger.helper.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.sultanburger.helper.gson.GsonHelper;
import com.sultanburger.utils.Validator;

import java.util.List;

public class PreferenceHelper {

    private static final String TAG = PreferenceHelper.class.getSimpleName();

    private static PreferenceHelper preferenceHelper;
    private static String preferenceName;
    private Context context;
    private GsonHelper gsonHelper;

    private PreferenceHelper(Context context, String preferenceName) {
        this.context = context;
        this.preferenceName = preferenceName;

        this.gsonHelper = new GsonHelper(context);
    }

    public static PreferenceHelper init(@NonNull Context context, @NonNull String preferenceName) {
        if (!Validator.isValid(preferenceHelper))
            preferenceHelper = new PreferenceHelper(context, preferenceName);

        return preferenceHelper;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public void remove(@NonNull String... key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        for (int i = 0; i < key.length; i++) {
            editor.remove(key[i].toString());
            editor.commit();
        }
    }

    public boolean contains(@NonNull String key) {
        return getSharedPreferences(context).contains(key);
    }

    public void setString(@NonNull String key, String data) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String getString(@NonNull String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public void setBoolean(@NonNull String key, boolean data) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public boolean getBoolean(@NonNull String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }

    public void setListData(@NonNull String key, List listData) {
        String toJson = gsonHelper.toString(listData);

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, toJson);
        editor.commit();
    }

    public <T> List getListData(@NonNull String key, @NonNull Class<T> clazz) {
//        Type type = new TypeToken<Map<String, String>>(){}.getType();

        List<T> listData = gsonHelper.fromString(getSharedPreferences(context).getString(key, ""), clazz);
        return listData;
    }

    public void setObjectData(@NonNull String key, Object object) {
        String toJson = gsonHelper.toString(object);

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, toJson);
        editor.commit();
    }

    public Object getObjectData(@NonNull String key, @NonNull Class<?> clazz) {
        return gsonHelper.fromString(getSharedPreferences(context).getString(key, ""), clazz);
    }
}
