package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserAddressOutput {

    @Expose
    @SerializedName("user_address_details")
    private List<ListUserAddress> userAddressDetails;

    public ListUserAddressOutput() {

    }

    public List<ListUserAddress> getUserAddressDetails() {
        return userAddressDetails;
    }

    public void setUserAddressDetails(List<ListUserAddress> userAddressDetails) {
        this.userAddressDetails = userAddressDetails;
    }
}
