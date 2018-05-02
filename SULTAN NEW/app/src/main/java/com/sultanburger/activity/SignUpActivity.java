package com.sultanburger.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sultanburger.AppBaseActivity;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;

public class SignUpActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText editTextMobileNumber;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSignUp;
    private TextView textViewSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        if (BuildConfig.DEBUG) {
            editTextMobileNumber.setText("9688220919");
            editTextName.setText("BoopathyRaja");
            editTextEmail.setText("boopathyraja1990@gmail.com");
            editTextPassword.setText("123456789");
            editTextConfirmPassword.setText("123456789");
        }
    }

    @Override
    public void init() {
        editTextMobileNumber = findViewById(R.id.signUp_editText_mobileNumber);
        editTextName = findViewById(R.id.signUp_editText_name);
        editTextEmail = findViewById(R.id.signUp_editText_email);
        editTextPassword = findViewById(R.id.signUp_editText_password);
        editTextConfirmPassword = findViewById(R.id.signUp_editText_confirmPassword);

        buttonSignUp = findViewById(R.id.signUp_button_signUp);
        buttonSignUp.setOnClickListener(this);

        textViewSkip = findViewById(R.id.signUp_textView_skip);
        textViewSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.signUp_button_signUp:
                doSignUp();
                break;

            case R.id.signUp_textView_skip:
                //guestSignIn();
                break;
        }
    }

    private void doSignUp() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String mobileNumber = editTextMobileNumber.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        signUp(name, email, mobileNumber, password, confirmPassword);
    }
}