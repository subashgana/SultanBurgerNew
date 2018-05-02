package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class SignUpOTPInput implements Validate {

    @Expose
    @SerializedName("otp")
    private String otp;

    public SignUpOTPInput() {

    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        if (!Validator.isValid(otp)) {
            ToastUtil.showToast(context, ENTER + " OTP");
            retVal = false;
        }

        return retVal;
    }
}
