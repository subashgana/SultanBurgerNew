package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

import java.util.List;

public class AddToCartInput implements Validate {

    @Expose
    @SerializedName("branch_id")
    private String branchId;

    @Expose
    @SerializedName("order_type")
    private String orderType;

    @Expose
    @SerializedName("user_address_id")
    private String userAddressId;

    @Expose
    @SerializedName("menu_item_id")
    private String menuItemId;

    @Expose
    @SerializedName("modifiers")
    private List<Integer> modifiers;

    @Expose
    @SerializedName("addons")
    private List<Integer> addons;

    public AddToCartInput() {

    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public List<Integer> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Integer> modifiers) {
        this.modifiers = modifiers;
    }

    public List<Integer> getAddons() {
        return addons;
    }

    public void setAddons(List<Integer> addons) {
        this.addons = addons;
    }

    @Override
    public boolean isValid(Context context) {
        boolean retVal = true;

        return retVal;
    }
}
