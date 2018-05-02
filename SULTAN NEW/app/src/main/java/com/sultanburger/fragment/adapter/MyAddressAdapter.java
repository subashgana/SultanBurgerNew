package com.sultanburger.fragment.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sultanburger.R;
import com.sultanburger.data.output.ListUserAddress;
import com.sultanburger.fragment.handler.MyAddressHandler;
import com.sultanburger.helper.recyclerview.RVAdapterCallback;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVViewHolderCallback;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> implements RVAdapterCallback {

    private static final String TAG = MyAddressAdapter.class.getSimpleName();

    private Context context;
    private List<ListUserAddress> listUserAddresses;
    private final MyAddressHandler myAddressFragmentHandler;
    private final RVItemDragCallback rvItemDragCallback;

    private int selectedPosition = -1;

    public MyAddressAdapter(List<ListUserAddress> listUserAddresses, MyAddressHandler myAddressHandler, RVItemDragCallback rvItemDragCallback) {
        this.listUserAddresses = new ArrayList<>(listUserAddresses);
        this.myAddressFragmentHandler = myAddressHandler;
        this.rvItemDragCallback = rvItemDragCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_my_address_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final ListUserAddress listUserAddress = listUserAddresses.get(position);
        if (Validator.isValid(listUserAddress)) {
            if (selectedPosition == position)
                holder.radioButton.setChecked(true);
            else
                holder.radioButton.setChecked(false);

            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();

                    holder.radioButton.setChecked(true);
                    myAddressFragmentHandler.onAddressSelected(listUserAddress);
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

            holder.radioButton.setText(listUserAddress.getAddress().replaceAll(",", "\n"));
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = position;
                    notifyDataSetChanged();

                    holder.radioButton.setChecked(true);
                    myAddressFragmentHandler.onAddressSelected(listUserAddress);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.listUserAddresses.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listUserAddresses, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listUserAddresses, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, final int position, boolean isSwipedRight) {
        final ListUserAddress listUserAddress = removeItem(position);
//        addItem(position, removedPlacedOrderBO);
    }

    public ListUserAddress removeItem(int position) {
        ListUserAddress listUserAddress = this.listUserAddresses.remove(position);
        notifyItemRemoved(position);

        return listUserAddress;
    }

    public void addItem(int position, ListUserAddress listUserAddress) {
        this.listUserAddresses.add(position, listUserAddress);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ListUserAddress listUserAddress = this.listUserAddresses.remove(fromPosition);
        this.listUserAddresses.add(toPosition, listUserAddress);
        notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RVViewHolderCallback {

        private LinearLayout linearLayoutContainer;
        private RadioButton radioButton;

        public ViewHolder(View view) {
            super(view);

            linearLayoutContainer = view.findViewById(R.id.existingAddress_item_linearLayout_container);
            radioButton = view.findViewById(R.id.myAddress_item_radioButton);

        }

        @Override
        public void onItemSelected(Context context) {

        }

        @Override
        public void onItemClear(Context context) {

        }
    }
}
