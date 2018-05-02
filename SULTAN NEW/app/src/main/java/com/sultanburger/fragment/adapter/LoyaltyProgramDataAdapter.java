package com.sultanburger.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.utils.Validator;

import java.util.HashMap;
import java.util.List;

public class LoyaltyProgramDataAdapter extends BaseExpandableListAdapter {

    private static final String TAG = LoyaltyProgramDataAdapter.class.getSimpleName();

    private Context context;
    private List<String> headers;
    private HashMap<String, List<String>> childData;
    private final DashBoardHandler dashBoardHandler;

    public LoyaltyProgramDataAdapter(List<String> headers, HashMap<String, List<String>> childData, DashBoardHandler dashBoardHandler) {
        this.headers = headers;
        this.childData = childData;
        this.dashBoardHandler = dashBoardHandler;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        this.context = parent.getContext();

        if (!Validator.isValid(convertView)) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_loyalty_program_child_item, null);
        }

        final String childText = (String) getChild(groupPosition, childPosition);

        TextView textViewChild = convertView.findViewById(R.id.loyaltyProgram_child_item_textView);
        textViewChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        this.context = parent.getContext();

        if (!Validator.isValid(convertView)) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_loyalty_program_header_item, null);
        }

        String headerTitle = (String) getGroup(groupPosition);

        TextView textViewHeader = convertView.findViewById(R.id.loyaltyProgram_header_item_textView);
        textViewHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
