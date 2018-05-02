package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;

public class AccountSettingsFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = AccountSettingsFragment.class.getSimpleName();

    private RelativeLayout relativeLayoutAccountInfo;
    private RelativeLayout relativeLayoutChangePassword;
    private RelativeLayout relativeLayoutLogout;
    private RelativeLayout relativeLayoutReportProblem;
    private RelativeLayout relativeLayoutTermsOfUse;
    private RelativeLayout relativeLayoutAbout;
    private RelativeLayout relativeLayoutRateAndReview;

    private DashBoardHandler dashBoardHandler;

    public AccountSettingsFragment() {

    }

    public static AccountSettingsFragment newInstance() {
        AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
        return accountSettingsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        relativeLayoutAccountInfo = view.findViewById(R.id.accountSettings_relativeLayout_accountInfo);
        relativeLayoutAccountInfo.setOnClickListener(this);

        relativeLayoutChangePassword = view.findViewById(R.id.accountSettings_relativeLayout_changePassword);
        relativeLayoutChangePassword.setOnClickListener(this);

        relativeLayoutLogout = view.findViewById(R.id.accountSettings_relativeLayout_logout);
        relativeLayoutLogout.setOnClickListener(this);

        relativeLayoutReportProblem = view.findViewById(R.id.accountSettings_relativeLayout_reportProblem);
        relativeLayoutReportProblem.setOnClickListener(this);

        relativeLayoutTermsOfUse = view.findViewById(R.id.accountSettings_relativeLayout_termsOfUse);
        relativeLayoutTermsOfUse.setOnClickListener(this);

        relativeLayoutAbout = view.findViewById(R.id.accountSettings_relativeLayout_about);
        relativeLayoutAbout.setOnClickListener(this);

        relativeLayoutRateAndReview = view.findViewById(R.id.accountSettings_relativeLayout_rateAndReview);
        relativeLayoutRateAndReview.setOnClickListener(this);

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

        switch (id) {
            case R.id.accountSettings_relativeLayout_accountInfo:
                dashBoardHandler.accountInfo();
                break;

            case R.id.accountSettings_relativeLayout_changePassword:
                dashBoardHandler.changePassword();
                break;

            case R.id.accountSettings_relativeLayout_logout:
                dashBoardHandler.logout();
                break;

            case R.id.accountSettings_relativeLayout_reportProblem:
                dashBoardHandler.reportProblem();
                break;

            case R.id.accountSettings_relativeLayout_termsOfUse:
                dashBoardHandler.termsOfUse();
                break;

            case R.id.accountSettings_relativeLayout_about:
                dashBoardHandler.about();
                break;

            case R.id.accountSettings_relativeLayout_rateAndReview:
                dashBoardHandler.rateAndReview();
                break;
        }
    }
}
