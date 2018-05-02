package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedBranchOutput {

    @Expose
    @SerializedName("current_page")
    private String currentPage;

    @Expose
    @SerializedName("next_page")
    private String nextPage;

    @Expose
    @SerializedName("branch_lists")
    private List<BranchOutput> branchLists;

    public PagedBranchOutput() {

    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<BranchOutput> getBranchLists() {
        return branchLists;
    }

    public void setBranchLists(List<BranchOutput> branchLists) {
        this.branchLists = branchLists;
    }
}
