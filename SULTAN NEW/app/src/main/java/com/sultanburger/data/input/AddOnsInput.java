package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

public class AddOnsInput implements Validate {

    @Expose
    @SerializedName("menu_item_id")
    private String menuItemId;

    public AddOnsInput() {

    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        return retVal;
    }
}
