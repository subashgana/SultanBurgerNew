package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrders {

    @Expose
    @SerializedName("user_order_id")
    private String userOrderId;

    @Expose
    @SerializedName("order_type")
    private String orderType;

    @Expose
    @SerializedName("order_placed_datetime")
    private String orderPlacedDatetime;

    @Expose
    @SerializedName("order_placed_timestamp")
    private String orderPlacedTimestamp;

    @Expose
    @SerializedName("total")
    private String total;

    @Expose
    @SerializedName("user_order_status_id")
    private String userOrderStatusId;

    @Expose
    @SerializedName("menu_names")
    private String menuNames;

    @Expose
    @SerializedName("branch_name")
    private String branchName;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("status_name")
    private String statusName;

    public MyOrders() {

    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderPlacedDatetime() {
        return orderPlacedDatetime;
    }

    public void setOrderPlacedDatetime(String orderPlacedDatetime) {
        this.orderPlacedDatetime = orderPlacedDatetime;
    }

    public String getOrderPlacedTimestamp() {
        return orderPlacedTimestamp;
    }

    public void setOrderPlacedTimestamp(String orderPlacedTimestamp) {
        this.orderPlacedTimestamp = orderPlacedTimestamp;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserOrderStatusId() {
        return userOrderStatusId;
    }

    public void setUserOrderStatusId(String userOrderStatusId) {
        this.userOrderStatusId = userOrderStatusId;
    }

    public String getMenuNames() {
        return menuNames;
    }

    public void setMenuNames(String menuNames) {
        this.menuNames = menuNames;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
