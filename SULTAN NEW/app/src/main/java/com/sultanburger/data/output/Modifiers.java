package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Modifiers {

    @Expose
    @SerializedName("modifier_id")
    private String modifierId;

    @Expose
    @SerializedName("modifier_category_id")
    private String modifierCategoryId;

    @Expose
    @SerializedName("modifier_en")
    private String modifierEn;

    @Expose
    @SerializedName("modifier_ar")
    private String modifierAr;

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
    @SerializedName("modifier_name")
    private String modifierName;


    public Modifiers() {

    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierCategoryId() {
        return modifierCategoryId;
    }

    public void setModifierCategoryId(String modifierCategoryId) {
        this.modifierCategoryId = modifierCategoryId;
    }

    public String getModifierEn() {
        return modifierEn;
    }

    public void setModifierEn(String modifierEn) {
        this.modifierEn = modifierEn;
    }

    public String getModifierAr() {
        return modifierAr;
    }

    public void setModifierAr(String modifierAr) {
        this.modifierAr = modifierAr;
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

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }
}
