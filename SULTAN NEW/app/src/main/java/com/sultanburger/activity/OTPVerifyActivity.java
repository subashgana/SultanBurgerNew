package com.sultanburger.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sultanburger.AppBaseActivity;
import com.sultanburger.R;
import com.sultanburger.helper.smsreceiver.SMSReceiver;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

public class OTPVerifyActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = OTPVerifyActivity.class.getSimpleName();
    private static final String messageStartWith = "Code is";

    private EditText editTextOTP;
    private Button buttonVerify;

    private String mobileNumber;
    private long otp;

    private OTPReceiver otpReceiver;
    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        init();
        processBundleData();

        editTextOTP.setText(String.valueOf(otp));
    }

    @Override
    public void init() {
        editTextOTP = findViewById(R.id.optVerify_editText_otp);

        buttonVerify = findViewById(R.id.otpVerify_button_verify);
        buttonVerify.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerSMSReceiver();
        registerOTPReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterSMSReceiver();
        unregisterOTPReceiver();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.otpVerify_button_verify:
                doVerify();
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

    private void doVerify() {
        String otp = editTextOTP.getText().toString();

        if (!Validator.isValid(otp)) {
            ToastUtil.showToast(getApplicationContext(), "Enter OTPOutput");
            return;
        }

        signUpOTPVerify(otp);
    }

    private void registerSMSReceiver() {
        smsReceiver = new SMSReceiver(messageStartWith);
        smsReceiver.register(getApplicationContext(), smsReceiver);
    }

    private void unregisterSMSReceiver() {
        try {
            smsReceiver.unregister(getApplicationContext(), smsReceiver);
        } catch (Exception e) {
            Logger.writeLog(TAG, "unregisterSMSReceiver -> " + e.getLocalizedMessage());
        }
    }

    private void registerOTPReceiver() {
        otpReceiver = new OTPReceiver();

        IntentFilter intentFilter = new IntentFilter(BROADCAST_OTP_RECEIVER);
        intentFilter.setPriority(999);
        registerReceiver(otpReceiver, intentFilter);
    }

    private void unregisterOTPReceiver() {
        try {
            unregisterReceiver(otpReceiver);
        } catch (IllegalArgumentException e) {
            Logger.writeLog(TAG, "unregisterSMSReceiver -> " + e.getLocalizedMessage());
        }
    }

    class OTPReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (Validator.isValid(bundle)) {
                String otpMessage = bundle.getString(BROADCAST_OTP_RECEIVER_DATA, "");

                String otp = otpMessage.startsWith(messageStartWith) ? otpMessage.replace(messageStartWith, "").trim() : "";
                if (Validator.isValid(otp))
                    editTextOTP.setText(otp);
                else
                    Logger.writeLog(TAG, "OTPReceiver : onReceive -> Invalid OTPOutput");
            }
        }
    }
}
