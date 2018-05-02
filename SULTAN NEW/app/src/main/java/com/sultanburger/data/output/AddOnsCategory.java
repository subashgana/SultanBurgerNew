package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddOnsCategory implements Serializable {

    @Expose
    @SerializedName("add_ons_category_id")
    private String addOnsCategoryId;

    @Expose
    @SerializedName("add_ons_category_en")
    private String addOnsCategoryEn;

    @Expose
    @SerializedName("add_ons_category_ar")
    private String addOnsCategoryAr;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("add_ons_category_name")
    private String addOnsCategoryName;

    @Expose
    @SerializedName("add_ons")
    private List<AddOns> addOns;

    public AddOnsCategory() {

    }

    public String getAddOnsCategoryId() {
        return addOnsCategoryId;
    }

    public void setAddOnsCategoryId(String addOnsCategoryId) {
        this.addOnsCategoryId = addOnsCategoryId;
    }

    public String getAddOnsCategoryEn() {
        return addOnsCategoryEn;
    }

    public void setAddOnsCategoryEn(String addOnsCategoryEn) {
        this.addOnsCategoryEn = addOnsCategoryEn;
    }

    public String getAddOnsCategoryAr() {
        return addOnsCategoryAr;
    }

    public void setAddOnsCategoryAr(String addOnsCategoryAr) {
        this.addOnsCategoryAr = addOnsCategoryAr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddOnsCategoryName() {
        return addOnsCategoryName;
    }

    public void setAddOnsCategoryName(String addOnsCategoryName) {
        this.addOnsCategoryName = addOnsCategoryName;
    }

    public List<AddOns> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOns> addOns) {
        this.addOns = addOns;
    }
}
