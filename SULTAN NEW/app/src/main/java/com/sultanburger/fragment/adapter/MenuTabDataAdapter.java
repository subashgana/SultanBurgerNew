package com.sultanburger.fragment.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sultanburger.AppBaseActivity;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.data.input.ListMenuInput;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.preference.PreferenceHelper;
import com.sultanburger.helper.recyclerview.RVAdapterCallback;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVViewHolderCallback;
import com.sultanburger.utils.StringUtil;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sultanburger.AppConstants.PREF_AUTH_TOKEN;
import static com.sultanburger.AppConstants.PREF_OPTION_SELECTION_TYPE;
import static com.sultanburger.AppConstants.URI_ADD_MENUS_TO_CART;

public class MenuTabDataAdapter extends RecyclerView.Adapter<MenuTabDataAdapter.ViewHolder> implements RVAdapterCallback {

    private static final String TAG = MenuTabDataAdapter.class.getSimpleName();

    private Context context;
    private List<ListMenu> listMenus;
    private final DashBoardHandler dashBoardHandler;
    private final RVItemDragCallback rvItemDragCallback;
    String tag_json_obj = "json_obj_req";
    int orderType = 1;

    public MenuTabDataAdapter(List<ListMenu> listMenus, DashBoardHandler dashBoardHandler, RVItemDragCallback rvItemDragCallback) {
        this.listMenus = new ArrayList<>(listMenus);
        this.dashBoardHandler = dashBoardHandler;
        this.rvItemDragCallback = rvItemDragCallback;
    }


    public AppBaseActivity getAppBaseActivity() {
        return ((AppBaseActivity) context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_menu_tab_data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final ListMenu listMenu = listMenus.get(position);
        if (Validator.isValid(listMenu)) {
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.linearLayoutContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        if (Validator.isValid(rvItemDragCallback))
                            rvItemDragCallback.onDrag(holder);
                    }

                    return false;
                }
            });

            ImageUtil.setBannerImage(context, holder.imageView, listMenu.getMenuImageUrl(), R.drawable.ic_onloading);
            holder.textViewItemName.setText(listMenu.getMenuName());
            holder.textViewAmount.setText(listMenu.getMrp() + " SAR");
            holder.textViewCount.setText(StringUtil.doPadding(0));

            holder.imageViewMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.textViewCount.getText().toString());
                    if (count > 0) {
                        holder.textViewCount.setText(StringUtil.doPadding(--count));

                        CartItem cartItem = new CartItem();
                        cartItem.setListMenu(listMenu);


                        SultanBurgerApplication.getInstance().removeCartItem(cartItem);
                    }
                }
            });

            holder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(holder.textViewCount.getText().toString());
                    holder.textViewCount.setText(StringUtil.doPadding(++count));

                    Utils.makeDelay(500, new Utils.HandlerCallBack() {
                        @Override
                        public void onDelayCompleted() {

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
                            boolean isModifierPresent = listMenu.getModifiersPresent().equals("1");
                            boolean isAddOnsPresent = listMenu.getAddOnsPresent().equals("1");
                            String actionName = isAddOnsPresent ? context.getResources().getString(R.string.modifiers_next) : context.getResources().getString(R.string.modifiers_done);
                            String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
                            CartItem cartItem = new CartItem();
                            cartItem.setListMenu(listMenu);

                            if (isModifierPresent)
                                dashBoardHandler.modifiers(cartItem, actionName);
                            else if (isAddOnsPresent)
                                dashBoardHandler.addons(cartItem);
                            else

                            {


                                try {
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String URL = BuildConfig.URL_BASE+ URI_ADD_MENUS_TO_CART;
                                    JSONObject jsonBody = new JSONObject();
                                    jsonBody.put("branch_id", branchId);
                                    jsonBody.put("order_type", String.valueOf(orderType));
                                    jsonBody.put("user_address_id", "1");
                                    jsonBody.put("menu_item_id", cartItem.getListMenu().getMenuItemId());
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
                                            PreferenceHelper preferenceHelper = PreferenceHelper.init(context, context.getResources().getString(R.string.app_name));

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

// Adding request to request queue
                               // SultanBurgerApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




                              /*  String url = "http://brillmindz.org/sultanburgerMobile/" + URI_ADD_MENUS_TO_CART;

                                ProgressDialog pDialog = new ProgressDialog(getAppBaseActivity());
                                pDialog.setMessage("Loading...");
                                pDialog.show();

*//*

                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("branch_id", branchId);
                                params.put("order_type", String.valueOf(orderType));
                                params.put("user_address_id", "1");
                                params.put("menu_item_id", cartItem.getListMenu().getMenuItemId());
*//*

                                ListMenuInput listMenuInput = new ListMenuInput();
                                listMenuInput.setBranchId(branchId);
                                listMenuInput.setMenuItemid(cartItem.getListMenu().getMenuItemId());
                                listMenuInput.setOrderType(orderType);
                                //listMenuInput.setUserAddressId(UserAddressId);


                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                        url, new JSONObject((Map) listMenuInput),
                                        new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d(TAG, response.toString());
                                                ToastUtil.showToast(context, response.toString());

                                                pDialog.hide();
                                            }
                                        }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                        ToastUtil.showToast(context, error.getMessage());

                                        pDialog.hide();
                                    }
                                }) {

                                    *//**
                                     * Passing some request headers
                                     * *//*
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        PreferenceHelper preferenceHelper = PreferenceHelper.init(context, context.getResources().getString(R.string.app_name));

                                        String authToken = preferenceHelper.getString(PREF_AUTH_TOKEN);
                                        HashMap<String, String> headers = new HashMap<String, String>();
                                        headers.put("Content-Type", "application/json");
                                        headers.put("Authorization", authToken);
                                        return headers;
                                    }

                                };

                                // Adding request to request queue
                                SultanBurgerApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);*/
                                SultanBurgerApplication.getInstance().addCartItem(cartItem);
                            }

                           /* {
                                List<Integer> modifiers = new ArrayList<Integer>();
                                List<Integer> addons = new ArrayList<Integer>();
                                modifiers.add(0);
                                addons.add(0);
                                getAppBaseActivity().AddMenuItemToCart(branchId, orderType, "1", cartItem.getListMenu().getMenuItemId(), modifiers, addons, new DataReceiver<AddtoCartOutput>() {


                                    @Override
                                    public void receiveData(AddtoCartOutput result) {

                                    }
                                });


                                SultanBurgerApplication.getInstance().addCartItem(cartItem);
                            }*/
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.listMenus.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listMenus, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listMenus, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, final int position, boolean isSwipedRight) {
        final ListMenu listMenu = removeItem(position);
//        addItem(position, removedPlacedOrderBO);
    }

    public ListMenu removeItem(int position) {
        ListMenu listMenu = this.listMenus.remove(position);
        notifyItemRemoved(position);

        return listMenu;
    }

    public void addItem(int position, ListMenu listMenu) {
        this.listMenus.add(position, listMenu);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ListMenu listMenu = this.listMenus.remove(fromPosition);
        this.listMenus.add(toPosition, listMenu);
        notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RVViewHolderCallback {

        private LinearLayout linearLayoutContainer;
        private ImageView imageView;
        private TextView textViewItemName;
        private TextView textViewAmount;
        private ImageView imageViewMinus;
        private TextView textViewCount;
        private ImageView imageViewAdd;

        public ViewHolder(View view) {
            super(view);

            Dimens dimens = Utils.getBannerImageSize(context);

            linearLayoutContainer = view.findViewById(R.id.menu_tab_data_item_linearLayout_container);

            imageView = view.findViewById(R.id.menu_tab_data_item_imageView);
            imageView.getLayoutParams().width = dimens.getWidth();
            imageView.getLayoutParams().height = dimens.getHeight();

            textViewItemName = view.findViewById(R.id.menu_tab_data_item_textView_itemName);
            textViewAmount = view.findViewById(R.id.menu_tab_data_item_textView_amount);
            imageViewMinus = view.findViewById(R.id.menu_tab_data_item_imageView_minus);
            textViewCount = view.findViewById(R.id.menu_tab_data_item_textView_count);
            imageViewAdd = view.findViewById(R.id.menu_tab_data_item_imageView_add);
        }

        @Override
        public void onItemSelected(Context context) {

        }

        @Override
        public void onItemClear(Context context) {

        }
    }
}
