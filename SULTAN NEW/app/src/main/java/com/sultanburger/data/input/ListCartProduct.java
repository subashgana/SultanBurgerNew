package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

public class ListCartProduct implements Validate {

    @Expose
    @SerializedName("user_address_id")
    private String userAddressId;

    @Expose
    @SerializedName("branch_id")
    private String branchId;


    @Expose
    @SerializedName("menu_item_id")
    private String menuItemId;

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    @Expose
    @SerializedName("order_type")
    private int orderType;


    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        return retVal;
    }
}
