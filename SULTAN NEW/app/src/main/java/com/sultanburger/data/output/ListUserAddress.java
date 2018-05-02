package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserAddress {

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("latitude")
    private String latitude;

    @Expose
    @SerializedName("longitude")
    private String longitude;

    @Expose
    @SerializedName("branch_id")
    private String branchId;

    @Expose
    @SerializedName("delivery_branch_details")
    private List<BranchOutput> deliveryBranchDetails;

    public ListUserAddress() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public List<BranchOutput> getDeliveryBranchDetails() {
        return deliveryBranchDetails;
    }

    public void setDeliveryBranchDetails(List<BranchOutput> deliveryBranchDetails) {
        this.deliveryBranchDetails = deliveryBranchDetails;
    }
}
