package com.sultanburger.data.input;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.utils.Validate;

import java.util.List;

public class AddToCartInput implements Validate {

    @Expose
    @SerializedName("branch_id")
    String branch_id;

    @Expose
    @SerializedName("order_type")
    int order_type;

    @Expose
    @SerializedName("user_address_id")
    String user_address_id;

    @Expose
    @SerializedName("menu_item_id")
    String menu_item_id;

    @Expose
    @SerializedName("modifiers")
    List<Integer> modifiers;

    @Expose
    @SerializedName("addons")
    private List<Integer> addons;



    public AddToCartInput() {

    }


    @Override
    public boolean isValid(Context context) {
        boolean retVal = false;

        return retVal;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public String getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(String user_address_id) {
        this.user_address_id = user_address_id;
    }

    public String getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(String menu_item_id) {
        this.menu_item_id = menu_item_id;
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





}
