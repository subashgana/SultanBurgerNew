package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.fragment.adapter.LoyaltyProgramDataAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoyaltyProgramFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = LoyaltyProgramFragment.class.getSimpleName();

    private ExpandableListView expandableListViewData;

    private DashBoardHandler dashBoardHandler;
    private LoyaltyProgramDataAdapter loyaltyProgramDataAdapter;

    public LoyaltyProgramFragment() {

    }

    public static LoyaltyProgramFragment newInstance() {
        LoyaltyProgramFragment loyaltyProgramFragment = new LoyaltyProgramFragment();
        return loyaltyProgramFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loyalty_program, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        expandableListViewData = view.findViewById(R.id.loyaltyProgram_expandableListView_data);
        expandableListViewData.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    expandableListViewData.collapseGroup(previousItem);

                previousItem = groupPosition;
            }
        });

        populateData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dashBoardHandler = (DashBoardHandler) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        dashBoardHandler = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_notification_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    private void populateData() {
        List<String> headerData = new ArrayList<>();
        headerData.add("Header 1");
        headerData.add("Header 2");
        headerData.add("Header 3");
        headerData.add("Header 4");
        headerData.add("Header 5");

        List<String> childData = new ArrayList<>();
        childData.add("Data............................................................................................................................");

        HashMap<String, List<String>> childMapData = new HashMap<>();
        childMapData.put(headerData.get(0), childData);
        childMapData.put(headerData.get(1), childData);
        childMapData.put(headerData.get(2), childData);
        childMapData.put(headerData.get(3), childData);
        childMapData.put(headerData.get(4), childData);

        loyaltyProgramDataAdapter = new LoyaltyProgramDataAdapter(headerData, childMapData, dashBoardHandler);
        expandableListViewData.setAdapter(loyaltyProgramDataAdapter);
    }

    private List<String> prepareChildData() {
        List<String> retVal = new ArrayList<>();

        return retVal;
    }
}
