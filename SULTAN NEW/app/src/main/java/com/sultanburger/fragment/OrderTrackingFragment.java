package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.output.MyOrders;
import com.sultanburger.data.output.MyOrdersOutput;
import com.sultanburger.fragment.adapter.OrderTrackingAdapter;
import com.sultanburger.fragment.handler.OrderTrackingHandler;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.List;

public class OrderTrackingFragment extends AppBaseFragment implements View.OnClickListener, RVItemDragCallback, OrderTrackingHandler {

    private static final String TAG = OrderTrackingFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayout linearLayoutBottomButtons;
    private Button buttonFeedback;
    private Button buttonOrderHistory;

    private DashBoardHandler dashBoardHandler;

    public OrderTrackingFragment() {

    }

    public static OrderTrackingFragment newInstance() {
        OrderTrackingFragment homeFragment = new OrderTrackingFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tracking, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = view.findViewById(R.id.orderTracking_recyclerView_orders);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RVGridSpacingDecoration(1, 20, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

        linearLayoutBottomButtons = view.findViewById(R.id.orderTracking_linearLayout_bottomContainer);
        linearLayoutBottomButtons.setVisibility(View.GONE);

        buttonFeedback = view.findViewById(R.id.orderTracking_button_feedback);
        buttonFeedback.setOnClickListener(this);

        buttonOrderHistory = view.findViewById(R.id.orderTracking_button_orderHistory);
        buttonOrderHistory.setOnClickListener(this);

        populateRecyclerView();
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

        if (id == R.id.orderTracking_button_feedback)
            doFeedback();
        else if (id == R.id.orderTracking_button_orderHistory)
            dashBoardHandler.orderHistory();
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onOrderSelected(MyOrders myOrders) {
        recyclerView.setVisibility(View.GONE);
        linearLayoutBottomButtons.setVisibility(View.VISIBLE);
    }

    private void populateRecyclerView() {
        getAppBaseActivity().getMyPendingOrders(new DataReceiver<MyOrdersOutput>() {
            @Override
            public void receiveData(MyOrdersOutput result) {
                if (Validator.isValid(result)) {
                    List<MyOrders> myOrders = result.getOrders();
                    if (Validator.isValid(myOrders) && !myOrders.isEmpty()) {
                        if (myOrders.size() == 1) {
                            recyclerView.setVisibility(View.GONE);
                            linearLayoutBottomButtons.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            linearLayoutBottomButtons.setVisibility(View.GONE);

                            OrderTrackingAdapter orderTrackingAdapter = new OrderTrackingAdapter(myOrders, OrderTrackingFragment.this, OrderTrackingFragment.this);
                            recyclerView.setAdapter(orderTrackingAdapter);

                            RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), orderTrackingAdapter);
                            rvItemTouchHelper.setSwipeEnabled(false);
                            rvItemTouchHelper.setSwipeDirectionRight(false);

                            itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                            itemTouchHelper.attachToRecyclerView(recyclerView);
                        }
                    } else {
                        ToastUtil.showToast(getContext(), "No orders to track");
                    }
                }
            }
        });
    }

    private void doFeedback() {

    }
}
