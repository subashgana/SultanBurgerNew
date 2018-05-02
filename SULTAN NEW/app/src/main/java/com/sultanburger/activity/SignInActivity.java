package com.sultanburger.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sultanburger.ActivityConfig;
import com.sultanburger.AppBaseActivity;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;

public class SignInActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private EditText editTextMobileNumberEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewResetPassword;
    private TextView textViewNewToAppSignUp;
    private TextView textViewSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();

        if (BuildConfig.DEBUG) {
            editTextMobileNumberEmail.setText("9688220919");
            editTextPassword.setText("1234567890");
        }
    }

    @Override
    public void init() {
        editTextMobileNumberEmail = findViewById(R.id.signIn_editText_mobileNumberEmail);
        editTextPassword = findViewById(R.id.signIn_editText_password);

        buttonLogin = findViewById(R.id.signIn_button_login);
        buttonLogin.setOnClickListener(this);

        textViewResetPassword = findViewById(R.id.signIn_textView_resetPassword);
        textViewResetPassword.setOnClickListener(this);

        textViewNewToAppSignUp = findViewById(R.id.signIn_textView_newToAppSignUp);
        textViewNewToAppSignUp.setOnClickListener(this);

        textViewSkip = findViewById(R.id.signIn_textView_skip);
        textViewSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.signIn_button_login:
                doLogin();
                break;

            case R.id.signIn_textView_resetPassword:
                doResetPassword();
                break;

            case R.id.signIn_textView_newToAppSignUp:
                doSignUp();
                break;

            case R.id.signIn_textView_skip:
                //guestSignIn();
                break;
        }
    }

    private void doLogin() {
        String username = editTextMobileNumberEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        signIn(username, password);
    }

    private void doResetPassword() {
        ActivityConfig.startForgotPasswordActivity(SignInActivity.this);
    }

    private void doSignUp() {
        ActivityConfig.startSignUpActivity(SignInActivity.this);
    }
}
