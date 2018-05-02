package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListMenuOutput {

    @Expose
    @SerializedName("image_base_url")
    private String imageBaseUrl;

    @Expose
    @SerializedName("current_page")
    private int currentPage;

    @Expose
    @SerializedName("next_page")
    private int nextPage;

    @Expose
    @SerializedName("menu_lists")
    private List<ListMenu> menuLists;

    public ListMenuOutput() {

    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public List<ListMenu> getMenuLists() {
        return menuLists;
    }

    public void setMenuLists(List<ListMenu> menuLists) {
        this.menuLists = menuLists;
    }
}
