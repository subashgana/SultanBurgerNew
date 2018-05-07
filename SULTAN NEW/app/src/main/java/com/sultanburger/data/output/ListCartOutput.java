package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCartOutput


{

    @Expose
    @SerializedName("cart_total_products")
    private String cart_total_products;

    @Expose
    @SerializedName("user_order_id")
    private String userorderid;

    @Expose
    @SerializedName("order_type")
    private String orderType;

    @Expose
    @SerializedName("branch_id")
    private String branchId;

    @Expose
    @SerializedName("user_address_id")
    private String useraddressid;


  /*  @Expose
    @SerializedName("cart_product_details")
    private List<ListMenu> menuLists;
*/

    @Expose
    @SerializedName("payment_method_details")
    private List<ListCartAllList> menuLists;

    public ListCartOutput() {

    }


    public String getCart_total_products() {
        return cart_total_products;
    }

    public void setCart_total_products(String cart_total_products) {
        this.cart_total_products = cart_total_products;
    }

    public String getUserorderid() {
        return userorderid;
    }

    public void setUserorderid(String userorderid) {
        this.userorderid = userorderid;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUseraddressid() {
        return useraddressid;
    }

    public void setUseraddressid(String useraddressid) {
        this.useraddressid = useraddressid;
    }

    public List<ListCartAllList> getMenuLists() {
        return menuLists;
    }


    public void setMenuLists(List<ListCartAllList> menuLists) {
        this.menuLists = menuLists;
    }

}
