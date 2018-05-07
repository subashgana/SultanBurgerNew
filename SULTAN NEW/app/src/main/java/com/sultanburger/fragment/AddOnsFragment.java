package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.AddOns;
import com.sultanburger.data.output.AddOnsCategory;
import com.sultanburger.data.output.AddOnsOutput;
import com.sultanburger.helper.preference.PreferenceHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOnsFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = AddOnsFragment.class.getSimpleName();

    private LinearLayout linearLayoutAddonsContainer;
    private Button buttonDone;

    private CartItem cartItem;
    private DashBoardHandler dashBoardHandler;
    int orderType = 1;

    List<Integer> modifiers;
    List<Integer> addons;

    public AddOnsFragment() {

    }

    public static AddOnsFragment newInstance(CartItem cartItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartItem", cartItem);

        AddOnsFragment addOnsFragment = new AddOnsFragment();
        addOnsFragment.setArguments(bundle);
        return addOnsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartItem = (CartItem) getArguments().getSerializable("cartItem");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addons, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        modifiers = new ArrayList<Integer>();
        addons = new ArrayList<Integer>();
        linearLayoutAddonsContainer = view.findViewById(R.id.addons_linearLayout_addonsContainer);

        buttonDone = view.findViewById(R.id.addons_button_done);
        buttonDone.setOnClickListener(this);

        populateAddOns();
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

        switch (id) {
            case R.id.addons_button_done:
                SultanBurgerApplication.getInstance().addCartItem(cartItem);


                String optionSelectionTypeStr = getAppBaseActivity().getPreferenceHelper().getString(PREF_OPTION_SELECTION_TYPE);
                if (Validator.isValid(optionSelectionTypeStr)) {
                    OptionSelectionType optionSelectionType = OptionSelectionType.valueOf(optionSelectionTypeStr);
                    switch (optionSelectionType) {
                        case PICKUP:
                            orderType = 1;
                            break;

                        case DELIVERY:
                            orderType = 2;
                            break;
                    }
                }
                String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();


                //addons.add(0);

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    String URL = BuildConfig.URL_BASE+ URI_ADD_MENUS_TO_CART;
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("branch_id", branchId);
                    jsonBody.put("order_type", String.valueOf(orderType));
                    jsonBody.put("user_address_id", "1");
                    jsonBody.put("menu_item_id", cartItem.getListMenu().getMenuItemId());
                    jsonBody.put("modifiers",String.valueOf(modifiers));
                    jsonBody.put("addons", String.valueOf(addons));
                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("LOG_VOLLEY", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("LOG_VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            PreferenceHelper preferenceHelper = PreferenceHelper.init(getActivity(), getActivity().getResources().getString(R.string.app_name));

                            String authToken = preferenceHelper.getString(PREF_AUTH_TOKEN);
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", authToken);
                            return headers;
                        }
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {

                                responseString = String.valueOf(response.statusCode);

                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dashBoardHandler.menu(branchId);
                break;
        }
    }

    private void populateAddOns() {
        getAppBaseActivity().getAddOns(cartItem.getListMenu().getMenuItemId(), new DataReceiver<AddOnsOutput>() {
            @Override
            public void receiveData(AddOnsOutput result) {
                if (Validator.isValid(result)) {
                    List<AddOnsCategory> addOnsCategories = result.getAddOnsDetails();
                    if (Validator.isValid(addOnsCategories) && !addOnsCategories.isEmpty()) {
                        cartItem.setAddOnsCategories(addOnsCategories);

                        for (AddOnsCategory addOnsCategory : addOnsCategories) {
                            addAddOnsContainer(addOnsCategory);
                        }
                    }
                }
            }
        });
    }

    private void addAddOnsContainer(AddOnsCategory addOnsCategory) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_addons_item, null);

        TextView textViewCategoryName = view.findViewById(R.id.addonsContainer_item_textView_categoryName);
        LinearLayout linearLayoutSwitchContainer = view.findViewById(R.id.addonsContainer_item_switchContainer);

        textViewCategoryName.setText(addOnsCategory.getAddOnsCategoryName());


        for (AddOns addOns : addOnsCategory.getAddOns()) {
            Switch aSwitch = new Switch(getContext());
            aSwitch.setId(Integer.parseInt(addOns.getAddOnsId()));
            addons.add( Integer.parseInt(addOns.getAddOnsId()));
            aSwitch.setText(addOns.getAddOnsName());
            aSwitch.setTextColor(DrawableUtil.getColor(getContext(), R.color.light_text_secondary));

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutSwitchContainer.addView(aSwitch, layoutParams);
        }

        linearLayoutAddonsContainer.addView(view);
    }
}