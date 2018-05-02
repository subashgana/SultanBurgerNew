package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddOnsOutput {

    @Expose
    @SerializedName("add_ons_details")
    private List<AddOnsCategory> addOnsDetails;

    public AddOnsOutput() {

    }

    public List<AddOnsCategory> getAddOnsDetails() {
        return addOnsDetails;
    }

    public void setAddOnsDetails(List<AddOnsCategory> addOnsDetails) {
        this.addOnsDetails = addOnsDetails;
    }
}
