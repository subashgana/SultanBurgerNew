package com.sultanburger.fragment.filter;

import android.widget.Filter;

import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.List;

class BranchFilter extends Filter {

    private List<? extends BranchOutput> branchOutputs;
    private BranchFilterableRecyclerViewAdapter branchFilterableRecyclerViewAdapter;

    public BranchFilter(List<? extends BranchOutput> branchOutputs) {
        this.branchOutputs = branchOutputs;
    }

    public void setBranchFilterableRecyclerViewAdapter(BranchFilterableRecyclerViewAdapter branchFilterableRecyclerViewAdapter) {
        this.branchFilterableRecyclerViewAdapter = branchFilterableRecyclerViewAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            results.values = branchOutputs;
            results.count = branchOutputs.size();
        } else {
            List<BranchOutput> filteredData = new ArrayList<>();

            for (BranchOutput branchOutput : branchOutputs) {
                String name = branchOutput.getBranchName().toLowerCase();

                String constraintStr = constraint.toString().toLowerCase();
                if (Validator.contains(name, constraintStr))
                    filteredData.add(branchOutput);
            }

            results.values = filteredData;
            results.count = filteredData.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (Validator.isValid(branchFilterableRecyclerViewAdapter))
            branchFilterableRecyclerViewAdapter.filteredResult(constraint.toString(), results.values);
    }
}
