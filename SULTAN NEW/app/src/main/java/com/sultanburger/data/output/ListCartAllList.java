package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListCartAllList implements Serializable {

    @Expose
    @SerializedName("user_order_id")
    private String userOrdeid;



    public ListCartAllList() {

    }


    public String getUserOrdeid() {
        return userOrdeid;
    }

    public void setUserOrdeid(String userOrdeid) {
        this.userOrdeid = userOrdeid;
    }
}
