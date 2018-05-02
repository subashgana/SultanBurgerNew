package com.sultanburger.fragment.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sultanburger.R;
import com.sultanburger.data.output.MyOrders;
import com.sultanburger.fragment.handler.OrderTrackingHandler;
import com.sultanburger.helper.recyclerview.RVAdapterCallback;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVViewHolderCallback;
import com.sultanburger.utils.DateTimeUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.ViewHolder> implements RVAdapterCallback {

    private static final String TAG = OrderTrackingAdapter.class.getSimpleName();

    private Context context;
    private List<MyOrders> myOrders;
    private final OrderTrackingHandler orderTrackingHandler;
    private final RVItemDragCallback rvItemDragCallback;

    public OrderTrackingAdapter(List<MyOrders> myOrders, OrderTrackingHandler orderTrackingHandler, RVItemDragCallback rvItemDragCallback) {
        this.myOrders = new ArrayList<>(myOrders);
        this.orderTrackingHandler = orderTrackingHandler;
        this.rvItemDragCallback = rvItemDragCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_order_tracking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final MyOrders myOrders = this.myOrders.get(position);
        if (Validator.isValid(myOrders)) {
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderTrackingHandler.onOrderSelected(myOrders);
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

            holder.textViewOrderId.setText("Order ID : " + myOrders.getUserOrderId());
            holder.textViewPlacedAt.setText("Placed at : " + DateTimeUtil.getRelativeTime(Long.parseLong(myOrders.getOrderPlacedTimestamp())));
            holder.textViewItems.setText("Items : " + myOrders.getMenuNames());
        }
    }

    @Override
    public int getItemCount() {
        return this.myOrders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(myOrders, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(myOrders, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, final int position, boolean isSwipedRight) {
        final MyOrders myOrders = removeItem(position);
//        addItem(position, removedPlacedOrderBO);
    }

    public MyOrders removeItem(int position) {
        MyOrders myOrders = this.myOrders.remove(position);
        notifyItemRemoved(position);

        return myOrders;
    }

    public void addItem(int position, MyOrders myOrders) {
        this.myOrders.add(position, myOrders);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final MyOrders myOrders = this.myOrders.remove(fromPosition);
        this.myOrders.add(toPosition, myOrders);
        notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RVViewHolderCallback {

        private LinearLayout linearLayoutContainer;
        private TextView textViewOrderId;
        private TextView textViewPlacedAt;
        private TextView textViewItems;

        public ViewHolder(View view) {
            super(view);

            linearLayoutContainer = view.findViewById(R.id.orderTracking_item_linearLayout_container);
            textViewOrderId = view.findViewById(R.id.orderTracking_item_textView_orderId);
            textViewPlacedAt = view.findViewById(R.id.orderTracking_item_textView_placedAt);
            textViewItems = view.findViewById(R.id.orderTracking_item_textView_items);

        }

        @Override
        public void onItemSelected(Context context) {

        }

        @Override
        public void onItemClear(Context context) {

        }
    }
}
