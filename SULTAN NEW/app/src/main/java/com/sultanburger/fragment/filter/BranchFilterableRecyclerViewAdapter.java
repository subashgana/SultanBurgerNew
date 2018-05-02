package com.sultanburger.fragment.filter;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import com.sultanburger.data.output.BranchOutput;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchFilterableRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    private static final String TAG = BranchFilterableRecyclerViewAdapter.class.getSimpleName();

    private BranchFilter userFilter;

    public abstract void filteredResult(String constraint, Object filteredResult);

    public BranchFilterableRecyclerViewAdapter(List<? extends BranchOutput> branchOutputs) {
        this.userFilter = new BranchFilter(new ArrayList<>(branchOutputs));
        this.userFilter.setBranchFilterableRecyclerViewAdapter(this);
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }
}
