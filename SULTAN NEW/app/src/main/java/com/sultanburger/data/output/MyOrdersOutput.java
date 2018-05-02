package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersOutput {

    @Expose
    @SerializedName("orders")
    private List<MyOrders> orders;

    public MyOrdersOutput() {

    }

    public List<MyOrders> getOrders() {
        return orders;
    }

    public void setOrders(List<MyOrders> orders) {
        this.orders = orders;
    }
}
