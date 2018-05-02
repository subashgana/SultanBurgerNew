package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOns {

    @Expose
    @SerializedName("add_ons_id")
    private String addOnsId;

    @Expose
    @SerializedName("add_ons_category_id")
    private String addOnsCategoryId;

    @Expose
    @SerializedName("add_ons_en")
    private String addOnsEn;

    @Expose
    @SerializedName("add_ons_ar")
    private String addOnsAr;

    @Expose
    @SerializedName("mrp")
    private String mrp;

    @Expose
    @SerializedName("sales_price")
    private String salesPrice;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("add_ons_name")
    private String addOnsName;

    @Expose
    @SerializedName("add_ons_present")
    private String addOnsPresent;

    @Expose
    @SerializedName("modifiers_present")
    private String modifiersPresent;

    @Expose
    @SerializedName("menu_name")
    private String menuName;

    public AddOns() {

    }

    public String getAddOnsId() {
        return addOnsId;
    }

    public void setAddOnsId(String addOnsId) {
        this.addOnsId = addOnsId;
    }

    public String getAddOnsCategoryId() {
        return addOnsCategoryId;
    }

    public void setAddOnsCategoryId(String addOnsCategoryId) {
        this.addOnsCategoryId = addOnsCategoryId;
    }

    public String getAddOnsEn() {
        return addOnsEn;
    }

    public void setAddOnsEn(String addOnsEn) {
        this.addOnsEn = addOnsEn;
    }

    public String getAddOnsAr() {
        return addOnsAr;
    }

    public void setAddOnsAr(String addOnsAr) {
        this.addOnsAr = addOnsAr;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddOnsName() {
        return addOnsName;
    }

    public void setAddOnsName(String addOnsName) {
        this.addOnsName = addOnsName;
    }

    public String getAddOnsPresent() {
        return addOnsPresent;
    }

    public void setAddOnsPresent(String addOnsPresent) {
        this.addOnsPresent = addOnsPresent;
    }

    public String getModifiersPresent() {
        return modifiersPresent;
    }

    public void setModifiersPresent(String modifiersPresent) {
        this.modifiersPresent = modifiersPresent;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
