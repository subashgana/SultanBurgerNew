package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuCategory {

    @Expose
    @SerializedName("menu_category_id")
    private String menuCategoryId;

    @Expose
    @SerializedName("menu_category_skuid")
    private String menuCategorySkuId;

    @Expose
    @SerializedName("category")
    private String category;

    public MenuCategory() {

    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getMenuCategorySkuId() {
        return menuCategorySkuId;
    }

    public void setMenuCategorySkuId(String menuCategorySkuId) {
        this.menuCategorySkuId = menuCategorySkuId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
