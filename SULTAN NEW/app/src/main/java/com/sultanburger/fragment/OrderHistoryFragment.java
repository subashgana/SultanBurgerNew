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

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.output.MyOrders;
import com.sultanburger.data.output.MyOrdersOutput;
import com.sultanburger.fragment.adapter.OrderHistoryAdapter;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Validator;

import java.util.List;

public class OrderHistoryFragment extends AppBaseFragment implements View.OnClickListener, RVItemDragCallback {

    private static final String TAG = OrderHistoryFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    private DashBoardHandler dashBoardHandler;

    public OrderHistoryFragment() {

    }

    public static OrderHistoryFragment newInstance() {
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        return orderHistoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = view.findViewById(R.id.orderHistory_recyclerView_orders);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RVGridSpacingDecoration(1, 20, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

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
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    private void populateRecyclerView() {
        getAppBaseActivity().getMyOrders(new DataReceiver<MyOrdersOutput>() {
            @Override
            public void receiveData(MyOrdersOutput result) {
                if (Validator.isValid(result)) {
                    List<MyOrders> orders = result.getOrders();
                    if (Validator.isValid(orders) && !orders.isEmpty()) {
                        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orders, dashBoardHandler, OrderHistoryFragment.this);
                        recyclerView.setAdapter(orderHistoryAdapter);

                        RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), orderHistoryAdapter);
                        rvItemTouchHelper.setSwipeEnabled(false);
                        rvItemTouchHelper.setSwipeDirectionRight(false);

                        itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                    }
                }
            }
        });
    }
}
