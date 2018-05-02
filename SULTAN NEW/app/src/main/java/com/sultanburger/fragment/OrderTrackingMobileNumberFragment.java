package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;

public class OrderTrackingMobileNumberFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = OrderTrackingMobileNumberFragment.class.getSimpleName();

    private EditText editTextMobileNumber;
    private Button buttonOrderTracking;

    private DashBoardHandler dashBoardHandler;

    public OrderTrackingMobileNumberFragment() {

    }

    public static OrderTrackingMobileNumberFragment newInstance() {
        OrderTrackingMobileNumberFragment homeFragment = new OrderTrackingMobileNumberFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tracking_mobile_number, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        editTextMobileNumber = view.findViewById(R.id.orderTrackingMobileNumber_editText_mobileNumber);

        buttonOrderTracking= view.findViewById(R.id.orderTrackingMobileNumber_button_orderTracking);
        buttonOrderTracking.setOnClickListener(this);

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
        inflater.inflate(R.menu.menu_notification_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.orderTrackingMobileNumber_button_orderTracking)
            doOrderTracking();
    }

    private void doOrderTracking() {
        
    }
}
