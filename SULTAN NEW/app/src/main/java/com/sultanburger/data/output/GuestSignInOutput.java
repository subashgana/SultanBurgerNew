package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestSignInOutput extends AuthToken {

    @Expose
    @SerializedName("user_id")
    private long userId;

    public GuestSignInOutput() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
