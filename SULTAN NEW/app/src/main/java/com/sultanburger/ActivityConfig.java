package com.sultanburger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sultanburger.activity.ChangePasswordActivity;
import com.sultanburger.activity.DashBoardActivity;
import com.sultanburger.activity.ForgotPasswordActivity;
import com.sultanburger.activity.OTPVerifyActivity;
import com.sultanburger.activity.OptionActivity;
import com.sultanburger.activity.SignInActivity;
import com.sultanburger.activity.SignUpActivity;

public class ActivityConfig implements AppConstants {

    public static void startOptionActivity(Activity activity) {
        Intent intent = new Intent(activity, OptionActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startSignInActivity(Activity activity) {
        Intent intent = new Intent(activity, SignInActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
    }

    public static void startSignUpActivity(Activity activity) {
        Intent intent = new Intent(activity, SignUpActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
    }

    public static void startOTPVerifyActivity(Activity activity, long otp, String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_OTP, otp);
        bundle.putString(EXTRA_MOBILE_NUMBER, mobileNumber);

        Intent intent = new Intent(activity, OTPVerifyActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startForgotPasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, ForgotPasswordActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
    }

    public static void startChangePasswordActivity(Activity activity, long otp, String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_OTP, otp);
        bundle.putString(EXTRA_MOBILE_NUMBER, mobileNumber);

        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startChangePasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
    }

    public static void startDashBoardActivity(Activity activity) {
        Intent intent = new Intent(activity, DashBoardActivity.class);
        intent.putExtra(AppBaseActivity.EXTRA_OVERRIDE_TRANSITION, false);
        activity.startActivity(intent);
        activity.finish();
    }
}
