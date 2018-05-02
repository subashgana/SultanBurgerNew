package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class SignInInput implements Validate {

    @Expose
    @SerializedName("user_name")
    private String username;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("device_type_id")
    private String deviceTypeId;

    @Expose
    @SerializedName("device_id")
    private String deviceId;

    @Expose
    @SerializedName("device_token")
    private String deviceToken;

    @Expose
    @SerializedName("language_id")
    private String languageId;

    public SignInInput() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        if (!Validator.isValid(username)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_in_mobile_number_email));
            retVal = false;
        }

        if (!Validator.isValid(password)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_in_password));
            retVal = false;
        }

        return retVal;
    }
}
