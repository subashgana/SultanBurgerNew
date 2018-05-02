package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class SignUpInput implements Validate {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("email_id")
    private String email;

    @Expose
    @SerializedName("phone_number")
    private String mobileNumber;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("confirm_password")
    private String confirmPassword;

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

    public SignUpInput() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

        if (!Validator.isValid(name)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_up_name));
            retVal = false;
        }

        if (!Validator.isValid(email)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_up_email));
            retVal = false;
        }

        if (!Validator.isValid(mobileNumber)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_up_mobile_number));
            retVal = false;
        }

        if (!Validator.isValid(password)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_up_password));
            retVal = false;
        }

        if (!Validator.isValid(confirmPassword)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.sign_up_confirm_password));
            retVal = false;
        }

        return retVal;
    }
}
