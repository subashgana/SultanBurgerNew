package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuCategoryOutput {

    @Expose
    @SerializedName("menu_category_details")
    private List<MenuCategory> menuCategoryDetails;

    public MenuCategoryOutput() {

    }

    public List<MenuCategory> getMenuCategoryDetails() {
        return menuCategoryDetails;
    }

    public void setMenuCategoryDetails(List<MenuCategory> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }
}
