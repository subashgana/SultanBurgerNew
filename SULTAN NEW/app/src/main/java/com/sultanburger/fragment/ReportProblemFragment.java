package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;

public class ReportProblemFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = ReportProblemFragment.class.getSimpleName();

    private DashBoardHandler dashBoardHandler;

    public ReportProblemFragment() {

    }

    public static ReportProblemFragment newInstance() {
        ReportProblemFragment reportProblemFragment = new ReportProblemFragment();
        return reportProblemFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_problem, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

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
        inflater.inflate(R.menu.menu_empty, menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }
}
