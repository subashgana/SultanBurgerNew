package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddUserAddressOutput {

    @Expose
    @SerializedName("user_address_id")
    private String userAddressId;

    @Expose
    @SerializedName("branch_id")
    private String branchId;

    @Expose
    @SerializedName("delivery_branch_details")
    private List<BranchOutput> deliveryBranchDetails;

    public AddUserAddressOutput() {

    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
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
