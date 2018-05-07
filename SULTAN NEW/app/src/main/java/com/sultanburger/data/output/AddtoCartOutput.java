package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddtoCartOutput {



    @Expose
    @SerializedName("branch_id")
    String branchId;


    @Expose
    @SerializedName("menu_item_id")
    String menuitemid;

    @Expose
    @SerializedName("user_ordered_item_id")
    int userordereditemid;

    @Expose
    @SerializedName("menu_count")
    String menucount;


    @Expose
    @SerializedName("cart_total_products")
    String cart_total_products;





    public String getCart_total_products() {
        return cart_total_products;
    }

    public void setCart_total_products(String cart_total_products) {
        this.cart_total_products = cart_total_products;
    }


    public int getUserordereditemid() {
        return userordereditemid;
    }

    public void setUserordereditemid(int userordereditemid) {
        this.userordereditemid = userordereditemid;
    }
    public String getMenuitemid() {
        return menuitemid;
    }

    public void setMenuitemid(String menuitemid) {
        this.menuitemid = menuitemid;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getMenucount() {
        return menucount;
    }

    public void setMenucount(String menucount) {
        this.menucount = menucount;
    }




    public AddtoCartOutput() {

    }






}
