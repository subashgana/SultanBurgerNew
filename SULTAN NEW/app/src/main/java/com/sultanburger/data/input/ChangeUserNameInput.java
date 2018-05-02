package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

public class ChangeUserNameInput implements Validate {

    @Expose
    @SerializedName("name")
    private String name;

    public ChangeUserNameInput() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        return retVal;
    }
}
