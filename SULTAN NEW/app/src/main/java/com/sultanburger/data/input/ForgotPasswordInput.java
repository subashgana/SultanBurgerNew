package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class ForgotPasswordInput implements Validate {

    @Expose
    @SerializedName("user_name")
    private String mobileNumber;

    @Expose
    @SerializedName("device_type_id")
    private String deviceTypeId;

    @Expose
    @SerializedName("device_id")
    private String deviceId;

    @Expose
    @SerializedName("device_token")
    private String deviceToken;


    public ForgotPasswordInput() {

    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        if (!Validator.isValid(mobileNumber)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.forgot_password_mobile_number));
            retVal = false;
        }

        return retVal;
    }
}
