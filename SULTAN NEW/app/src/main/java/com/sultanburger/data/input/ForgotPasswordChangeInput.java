package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class ForgotPasswordChangeInput implements Validate {

    @Expose
    @SerializedName("otp")
    private String otp;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("confirm_password")
    private String confirmPassword;

    public ForgotPasswordChangeInput() {

    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        if (!Validator.isValid(otp)) {
            ToastUtil.showToast(context, ENTER + " OTP");
            retVal = false;
        }

        if (!Validator.isValid(password)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.change_password_current_password));
            retVal = false;
        }

        if (!Validator.isValid(confirmPassword)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.change_password_current_password));
            retVal = false;
        }

        return retVal;
    }
}
