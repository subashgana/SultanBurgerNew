package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

import java.util.List;

public class AddMenutToCartInput implements Validate {

    @Expose
    @SerializedName("branch_id")
    private String branchId;


    @Expose
    @SerializedName("order_type")
    private int orderType;

    @Expose
    @SerializedName("user_address_id")
    private String userAddressId;

    public AddMenutToCartInput() {

    }

    @Expose
    @SerializedName("modifiers")
    List<Integer> modifiers;


    @Expose
    @SerializedName("addons")
    private List<Integer> addons;

    @Expose
    @SerializedName("menu_item_id")
    private String menuItemid;


    public String getMenuItemid() {
        return menuItemid;
    }

    public void setMenuItemid(String menuItemid) {
        this.menuItemid = menuItemid;
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
