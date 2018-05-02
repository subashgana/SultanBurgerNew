package com.sultanburger.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sultanburger.AppBaseActivity;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.utils.Validator;

public class ChangePasswordActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = ChangePasswordActivity.class.getSimpleName();

    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSave;

    private String mobileNumber;
    private long otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
        processBundleData();

        if (otp != 0)
            editTextOldPassword.setVisibility(View.GONE);

        if (BuildConfig.DEBUG) {
            editTextNewPassword.setText("1234567890");
            editTextConfirmPassword.setText("1234567890");
        }
    }

    @Override
    public void init() {
        editTextOldPassword = findViewById(R.id.changePassword_editText_oldPassword);
        editTextNewPassword = findViewById(R.id.changePassword_editText_newPassword);
        editTextConfirmPassword = findViewById(R.id.changePassword_editText_confirmPassword);

        buttonSave = findViewById(R.id.changePassword_button_save);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.changePassword_button_save:
                if (otp == 0)
                    doChangePassword();
                else
                    doForgotPassword();
                break;
        }
    }

    private void processBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (Validator.isValid(bundle)) {
            otp = bundle.getLong(EXTRA_OTP);
            mobileNumber = bundle.getString(EXTRA_MOBILE_NUMBER);
        }
    }

    private void doChangePassword() {
        String oldPassword = editTextOldPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        changePassword(oldPassword, newPassword, confirmPassword);
    }

    private void doForgotPassword() {
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        forgotPasswordChange(newPassword, confirmPassword, String.valueOf(otp));
    }
}
