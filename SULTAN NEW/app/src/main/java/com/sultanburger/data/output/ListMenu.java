package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListMenu implements Serializable {

    @Expose
    @SerializedName("menu_item_id")
    private String menuItemId;

    @Expose
    @SerializedName("menu_category_id")
    private String menuCategoryId;

    @Expose
    @SerializedName("menu_item_skuid")
    private String menuItemSkuId;

    @Expose
    @SerializedName("menu_name_en")
    private String menuNameEn;

    @Expose
    @SerializedName("menu_name_ar")
    private String menuNameAr;

    @Expose
    @SerializedName("menu_image_url")
    private String menuImageUrl;

    @Expose
    @SerializedName("mrp")
    private String mrp;

    @Expose
    @SerializedName("sales_price")
    private String salesPrice;

    @Expose
    @SerializedName("add_ons_present")
    private String addOnsPresent;

    @Expose
    @SerializedName("modifiers_present")
    private String modifiersPresent;

    @Expose
    @SerializedName("menu_name")
    private String menuName;

    public ListMenu() {

    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getMenuItemSkuId() {
        return menuItemSkuId;
    }

    public void setMenuItemSkuId(String menuItemSkuId) {
        this.menuItemSkuId = menuItemSkuId;
    }

    public String getMenuNameEn() {
        return menuNameEn;
    }

    public void setMenuNameEn(String menuNameEn) {
        this.menuNameEn = menuNameEn;
    }

    public String getMenuNameAr() {
        return menuNameAr;
    }

    public void setMenuNameAr(String menuNameAr) {
        this.menuNameAr = menuNameAr;
    }

    public String getMenuImageUrl() {
        return menuImageUrl;
    }

    public void setMenuImageUrl(String menuImageUrl) {
        this.menuImageUrl = menuImageUrl;
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
