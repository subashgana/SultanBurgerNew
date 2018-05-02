package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.output.UserDetail;
import com.sultanburger.data.output.UserDetailsOutput;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

public class AccountInfoFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = AccountInfoFragment.class.getSimpleName();

    private EditText editTextMobileNumber;
    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonSave;

    private DashBoardHandler dashBoardHandler;

    public AccountInfoFragment() {

    }

    public static AccountInfoFragment newInstance() {
        AccountInfoFragment accountInfoFragment = new AccountInfoFragment();
        return accountInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        editTextMobileNumber = view.findViewById(R.id.accountInfo_editText_mobileNumber);
        editTextMobileNumber.setEnabled(false);

        editTextName = view.findViewById(R.id.accountInfo_editText_name);

        editTextEmail = view.findViewById(R.id.accountInfo_editText_email);
        editTextEmail.setEnabled(false);

        buttonSave = view.findViewById(R.id.accountInfo_button_save);
        buttonSave.setOnClickListener(this);

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
        inflater.inflate(R.menu.menu_empty, menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.accountInfo_button_save:
                doChangeUserName();
                break;
        }
    }

    private void doChangeUserName() {
        Utils.hideSoftKeyboard(getContext(), editTextName);
        String userName = editTextName.getText().toString();

        getAppBaseActivity().changeUserName(userName, new DataReceiver<Boolean>() {
            @Override
            public void receiveData(Boolean result) {
                getAppBaseActivity().getUserDetails(new DataReceiver<UserDetailsOutput>() {
                    @Override
                    public void receiveData(UserDetailsOutput result) {
                        SultanBurgerApplication.getInstance().setUserDetail(result.getUserDetails().get(0));
                        getAppBaseActivity().onBackPressed();
                    }
                });
            }
        });
    }

    private void populateData() {
        UserDetail userDetail = SultanBurgerApplication.getInstance().getUserDetail();

        String phoneNumber = userDetail.getPhoneNumber();
        String name = userDetail.getName();
        String emailId = userDetail.getEmailId();

        editTextMobileNumber.setText(Validator.isValid(phoneNumber) ? phoneNumber : "");
        editTextName.setText(Validator.isValid(name) ? name : "");
        editTextEmail.setText(Validator.isValid(emailId) ? emailId : "");
    }
}
