package com.sultanburger.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.AddtoCartOutput;
import com.sultanburger.data.output.ListCartAllList;
import com.sultanburger.data.output.ListCartOutput;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.data.output.ListMenuOutput;
import com.sultanburger.fragment.adapter.MenuTabDataAdapter;
import com.sultanburger.helper.preference.PreferenceHelper;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sultanburger.helper.permission.PermissionHelper.init;

public class CartFragment extends AppBaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = CartFragment.class.getSimpleName();

    private DashBoardHandler dashBoardHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView  tvtotalitems, tvsubtotal, tvdeliveryfee, tvdiscountsr, tv_total;
    String[] font = new String[]{"6:00 PM", "7:00 PM", "8:00 PM"};
    Spinner spinnertime;
    LinearLayout linearLayout;
    Button addproduct, placeorder;
    private MediaDescriptionCompat.Builder supportActionBar;
    int minteger = 0;
    int orderType = 1;
    LinearLayout parentLayout;

    public CartFragment() {

    }

    public static CartFragment newInstance() {
        CartFragment cartFragment = new CartFragment();
        return cartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        parentLayout = (LinearLayout) view.findViewById(R.id.ll_ordersummary);
        tvsubtotal = (TextView) view.findViewById(R.id.tv_subtotal);
        tvdeliveryfee = (TextView) view.findViewById(R.id.tv_deliveryfee);
        tvdiscountsr = (TextView) view.findViewById(R.id.tv_discountsr);
        tv_total = (TextView) view.findViewById(R.id.tv_total);

        tvtotalitems = (TextView) view.findViewById(R.id.tv_totalitems);
       // customizetwo = (TextView) view.findViewById(R.id.customizetwo);
        addproduct = (Button) view.findViewById(R.id.addproduct);
        spinnertime = (Spinner) view.findViewById(R.id.spinnertime);
        spinnertime.setOnItemSelectedListener(CartFragment.this);

        initdata();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinnertime, font);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinnertime);
        spinnertime.setAdapter(spinnerArrayAdapter);


        placeorder = (Button) view.findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent placeord = new Intent(getActivity(), LoginTwoActivity.class);
                startActivity(placeord);*/
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    private void initdata() {
        String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
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

        List<Integer> modifiers = new ArrayList<Integer>();
        List<Integer> addons = new ArrayList<Integer>();
        modifiers.add(0);
        addons.add(0);

        SultanBurgerApplication.getInstance().getCartItems();


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = BuildConfig.URL_BASE + URI_CART_PRODUCT_LIST;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("branch_id", branchId);
            jsonBody.put("order_type", orderType);
            jsonBody.put("user_address_id", "1");
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  Log.d("LOG_VOLLEY", response.toString());

                    try {
                        JSONObject obj = new JSONObject(response);

                        JSONObject newObj = obj.optJSONObject("data");

                        JSONArray jarray = new JSONArray();
                        jarray = newObj.getJSONArray("cart_product_details");
                        if (jarray != null) {
                            parentLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject cartdetailsobj = new JSONObject();
                                cartdetailsobj = jarray.getJSONObject(i);
                                LayoutInflater layoutInflater = getLayoutInflater();
                                View view;

                                // Add the text layout to the parent layout
                                view = layoutInflater.inflate(R.layout.text_layout, parentLayout, false);

                                // In order to get the view we have to use the new view with text_layout in it
                                TextView tv_consolidatedmenuname = (TextView) view.findViewById(R.id.tv_consolidated_menu_name);
                                TextView tvconsolidatedorder = (TextView) view.findViewById(R.id.tv_consolidatedorder);
                                TextView tvnumberone = (TextView) view.findViewById(R.id.numberone);
                                tv_consolidatedmenuname.setText(cartdetailsobj.optString("item_name"));
                                tvconsolidatedorder.setText(cartdetailsobj.optString("overall_menu_cost"));
                                tvnumberone.setText(cartdetailsobj.optString("quantity"));
                                Log.d("LOG_VOLLEY_COST", cartdetailsobj.optString("overall_menu_cost"));
                                parentLayout.addView(view);
                                //tvnumberone.setText();
                                // Add the text view to the parent layout
                            }


                        }


                        JSONObject carttotalobj = newObj.getJSONObject("cart_total_details");
                        tvsubtotal.setText(carttotalobj.optString("cart_total"));
                        tvdeliveryfee.setText(carttotalobj.optString("delivery_cost"));
                        tvdiscountsr.setText(carttotalobj.optString("discount_cost"));
                        tv_total.setText(carttotalobj.optString("total"));


                        tvtotalitems.setText(newObj.optString("cart_total_products") + "  items in total");




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

// add the request object to the queue to be executed
        // SultanBurgerApplication.getInstance().addToRequestQueue(request_json);

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
