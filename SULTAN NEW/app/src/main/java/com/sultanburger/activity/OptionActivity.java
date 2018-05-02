package com.sultanburger.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sultanburger.ActivityConfig;
import com.sultanburger.AppBaseActivity;
import com.sultanburger.R;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.utils.ToastUtil;

public class OptionActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = OptionActivity.class.getSimpleName();

    private TextView textViewWelcome;
    private Button buttonPickUp;
    private Button buttonDelivery;
    private LinearLayout linearLayout;
    private TextView textViewOr;
    private Button buttonReorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        init();


        boolean isUserSignedIn = false;

        if (getPreferenceHelper().contains(PREF_IS_USER_SIGNED_IN))
            isUserSignedIn = getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN);

        textViewWelcome.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
        linearLayout.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
        textViewOr.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
        buttonReorder.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void init() {
        textViewWelcome = findViewById(R.id.option_textView_welcome);

        buttonPickUp = findViewById(R.id.option_button_pickUp);
        buttonPickUp.setOnClickListener(this);

        buttonDelivery = findViewById(R.id.option_button_delivery);
        buttonDelivery.setOnClickListener(this);

        linearLayout = findViewById(R.id.option_linearLayout);
        textViewOr = findViewById(R.id.option_textView_or);

        buttonReorder = findViewById(R.id.option_button_reOrder);
        buttonReorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.option_button_pickUp:
                getPreferenceHelper().setString(PREF_OPTION_SELECTION_TYPE, OptionSelectionType.PICKUP.name());
                // ActivityConfig.startDashBoardActivity(OptionActivity.this);


                if (getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN)) {
                    String username = getPreferenceHelper().getString(USER_NAME);
                    String password = getPreferenceHelper().getString(PASSWORD);
                    ToastUtil.showToast(getApplicationContext(),username+password);

                    signIn(username,password);
                } else {
                    guestSignIn();

                }
                break;

            case R.id.option_button_delivery:
                getPreferenceHelper().setString(PREF_OPTION_SELECTION_TYPE, OptionSelectionType.DELIVERY.name());

                if (getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN)) {

                    signIn(getPreferenceHelper().getString(USER_NAME), getPreferenceHelper().getString(PASSWORD));
                } else {
                    guestSignIn();

                }
                //  ActivityConfig.startDashBoardActivity(OptionActivity.this);
                break;

            case R.id.option_button_reOrder:
                // TODO - Check for recent order and re-order (If user SignIn)
//                getPreferenceHelper().setString(PREF_OPTION_SELECTION_TYPE, OptionSelectionType.RE_ORDER.name());
//                ActivityConfig.startDashBoardActivity(OptionActivity.this);
                break;
        }
    }
}
