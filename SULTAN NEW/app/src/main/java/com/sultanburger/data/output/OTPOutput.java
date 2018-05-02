package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPOutput extends AuthToken {

    @Expose
    @SerializedName("otp")
    private long otp;

    public OTPOutput() {

    }

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }
}
