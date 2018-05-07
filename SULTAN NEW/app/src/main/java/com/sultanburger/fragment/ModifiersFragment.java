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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.sultanburger.data.output.AddtoCartOutput;
import com.sultanburger.data.output.Modifiers;
import com.sultanburger.data.output.ModifiersCategory;
import com.sultanburger.data.output.ModifiersOutput;
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

public class ModifiersFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = ModifiersFragment.class.getSimpleName();

    private LinearLayout linearLayoutModifierContainer;
    private Button buttonNext;

    private CartItem cartItem;
    private String actionName;
    private DashBoardHandler dashBoardHandler;
    int orderType = 1;


    List<Integer> modifierslist;
    List<Integer> addons;

    public ModifiersFragment() {

    }

    public static ModifiersFragment newInstance(CartItem cartItem, String actionName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartItem", cartItem);
        bundle.putString("actionName", actionName);

        ModifiersFragment modifiersFragment = new ModifiersFragment();
        modifiersFragment.setArguments(bundle);
        return modifiersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartItem = (CartItem) getArguments().getSerializable("cartItem");
        actionName = getArguments().getString("actionName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modifiers, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        modifierslist = new ArrayList<Integer>();
        addons = new ArrayList<Integer>();
        linearLayoutModifierContainer = view.findViewById(R.id.modifiers_linearLayout_modifierContainer);

        buttonNext = view.findViewById(R.id.modifiers_button_next);
        buttonNext.setText(actionName);
        buttonNext.setOnClickListener(this);

        populateModifiers();
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
            case R.id.modifiers_button_next: {
                Log.d("LOG_VOLLEY_ACTION",actionName);
                if (actionName.equals(getString(R.string.modifiers_done))) {
                    SultanBurgerApplication.getInstance().addCartItem(cartItem);
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
                    //String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();


                    //addons.add(0);

                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        String URL = BuildConfig.URL_BASE+ URI_ADD_MENUS_TO_CART;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("branch_id", branchId);
                        jsonBody.put("order_type", String.valueOf(orderType));
                        jsonBody.put("user_address_id", "1");
                        jsonBody.put("menu_item_id", cartItem.getListMenu().getMenuItemId());
                        jsonBody.put("modifiers",String.valueOf(modifierslist));
                        jsonBody.put("addons", String.valueOf(addons));
                        final String mRequestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                              //  dashBoardHandler.menu(branchId);
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



                   // String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();

                }
                else if (actionName.equalsIgnoreCase(getString(R.string.modifiers_next))) {
                     dashBoardHandler.addons(cartItem);
                }
            }
            break;
        }
    }

    private void populateModifiers() {
        getAppBaseActivity().getModifiers(cartItem.getListMenu().getMenuItemId(), new DataReceiver<ModifiersOutput>() {
            @Override
            public void receiveData(ModifiersOutput result) {
                if (Validator.isValid(result)) {
                    List<ModifiersCategory> modifiersCategories = result.getModifiersDetails();
                    if (Validator.isValid(modifiersCategories) && !modifiersCategories.isEmpty()) {
                        cartItem.setModifiersCategories(modifiersCategories);

                        for (ModifiersCategory modifiersCategory : modifiersCategories) {
                            addModifierContainer(modifiersCategory);
                        }
                    }
                }
            }
        });
    }

    private void addModifierContainer(ModifiersCategory modifiersCategory) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_modifiers_item, null);

        TextView textViewCategoryName = view.findViewById(R.id.modifiersContainer_item_textView_categoryName);
        RadioGroup radioGroupModifiers = view.findViewById(R.id.modifiersContainer_item_radioGroup_modifiers);

        textViewCategoryName.setText(modifiersCategory.getModifierCategoryName());

        for (Modifiers modifiers : modifiersCategory.getModifiers()) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(Integer.parseInt(modifiers.getModifierId()));
            radioButton.setText(modifiers.getModifierName());
            radioButton.setTextColor(DrawableUtil.getColor(getContext(), R.color.light_text_secondary));
            modifierslist.add(Integer.parseInt(modifiers.getModifierId()));
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroupModifiers.addView(radioButton, layoutParams);
        }

        linearLayoutModifierContainer.addView(view);
    }
}
