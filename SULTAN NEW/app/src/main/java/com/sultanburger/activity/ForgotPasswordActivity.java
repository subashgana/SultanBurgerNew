package com.sultanburger.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sultanburger.AppBaseActivity;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

public class ForgotPasswordActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    private EditText editTextMobileNumber;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();

        if (BuildConfig.DEBUG) {
            editTextMobileNumber.setText("9688220919");
        }
    }

    @Override
    public void init() {
        editTextMobileNumber = findViewById(R.id.forgotPassword_editText_mobileNumber);

        buttonNext = findViewById(R.id.forgotPassword_button_next);
        buttonNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.forgotPassword_button_next:
                doForgotPassword();
                break;
        }
    }

    private void doForgotPassword() {
        String mobileNumber = editTextMobileNumber.getText().toString();

        if (!Validator.isValid(mobileNumber)) {
            ToastUtil.showToast(getApplicationContext(), "Enter mobile number");
            return;
        }

        forgotPassword(mobileNumber);
    }
}
