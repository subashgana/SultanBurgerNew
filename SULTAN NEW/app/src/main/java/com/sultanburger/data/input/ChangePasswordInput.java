package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validate;
import com.sultanburger.utils.Validator;

public class ChangePasswordInput implements Validate {

    @Expose
    @SerializedName("old_password")
    private String oldPassword;

    @Expose
    @SerializedName("new_password")
    private String newPassword;

    @Expose
    @SerializedName("confirm_password")
    private String confirmPassword;

    public ChangePasswordInput() {

    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

        if (!Validator.isValid(oldPassword)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.change_password_current_password));
            retVal = false;
        }

        if (!Validator.isValid(newPassword)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.change_password_new_password));
            retVal = false;
        }

        if (!Validator.isValid(confirmPassword)) {
            ToastUtil.showToast(context, ENTER + context.getResources().getString(R.string.change_password_confirm_new_password));
            retVal = false;
        }

        return retVal;
    }
}
