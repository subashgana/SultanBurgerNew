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
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.fragment.filter.BranchFilterableRecyclerViewAdapter;
import com.sultanburger.fragment.handler.BranchHandler;
import com.sultanburger.helper.recyclerview.RVAdapterCallback;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVViewHolderCallback;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BranchInfoAdapter extends BranchFilterableRecyclerViewAdapter<BranchInfoAdapter.ViewHolder> implements RVAdapterCallback {

    private static final String TAG = BranchInfoAdapter.class.getSimpleName();

    private Context context;
    private List<BranchOutput> branchOutputs;
    private final BranchHandler branchHandler;
    private final RVItemDragCallback rvItemDragCallback;

    private int selectedPosition = -1;

    public BranchInfoAdapter(List<BranchOutput> branchOutputs, BranchHandler branchHandler, RVItemDragCallback rvItemDragCallback) {
        super(new ArrayList<>(branchOutputs));

        this.branchOutputs = new ArrayList<>(branchOutputs);
        this.branchHandler = branchHandler;
        this.rvItemDragCallback = rvItemDragCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_branch_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final BranchOutput branchOutput = branchOutputs.get(position);
        if (Validator.isValid(branchOutput)) {
            if (selectedPosition == position) {
                holder.textViewShopName.setTextColor(DrawableUtil.getColor(context, R.color.red));
                holder.textViewAddress.setTextColor(DrawableUtil.getColor(context, R.color.red));
                holder.textViewMobileNumber.setTextColor(DrawableUtil.getColor(context, R.color.red));
                holder.textViewDistance.setTextColor(DrawableUtil.getColor(context, R.color.red));
                holder.textViewStatus.setTextColor(DrawableUtil.getColor(context, R.color.red));
            } else {
                holder.textViewShopName.setTextColor(DrawableUtil.getColor(context, R.color.light_text_secondary));
                holder.textViewAddress.setTextColor(DrawableUtil.getColor(context, R.color.light_text_secondary));
                holder.textViewMobileNumber.setTextColor(DrawableUtil.getColor(context, R.color.light_text_secondary));
                holder.textViewDistance.setTextColor(DrawableUtil.getColor(context, R.color.light_text_secondary));
                holder.textViewStatus.setTextColor(DrawableUtil.getColor(context, R.color.light_text_secondary));
            }

            holder.textViewShopName.setText(branchOutput.getBranchName());
            holder.textViewAddress.setText(branchOutput.getAddress());
            holder.textViewMobileNumber.setText("Ph : " + branchOutput.getPhoneNumber());
            holder.textViewDistance.setText(branchOutput.getDistance() + " kms");

            if (branchOutput.getBranchOpen().equals("1")) {
                holder.textViewStatus.setText("Opened");
                holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_circle_green, 0, 0);
            } else {
                holder.textViewStatus.setText("Closed");
                holder.textViewStatus.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_circle_red, 0, 0);
            }

            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();

                    branchHandler.onBranchSelected(branchOutput);
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
        }
    }

    @Override
    public int getItemCount() {
        return this.branchOutputs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void filteredResult(String constraint, Object filteredResult) {
        ToastUtil.clearAllToast();

        branchOutputs = (ArrayList<BranchOutput>) filteredResult;
        notifyDataSetChanged();

        if (branchOutputs.isEmpty())
            ToastUtil.showToast(context, "No results found for '" + constraint + "'");
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(branchOutputs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(branchOutputs, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder, final int position, boolean isSwipedRight) {
        final BranchOutput branchOutput = removeItem(position);
//        addItem(position, removedPlacedOrderBO);
    }

    public BranchOutput removeItem(int position) {
        BranchOutput branchOutput = this.branchOutputs.remove(position);
        notifyItemRemoved(position);

        return branchOutput;
    }

    public void addItem(int position, BranchOutput branchOutput) {
        this.branchOutputs.add(position, branchOutput);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final BranchOutput branchOutput = this.branchOutputs.remove(fromPosition);
        this.branchOutputs.add(toPosition, branchOutput);
        notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RVViewHolderCallback {

        private LinearLayout linearLayoutContainer;
        private TextView textViewShopName;
        private TextView textViewAddress;
        private TextView textViewMobileNumber;
        private TextView textViewDistance;
        private TextView textViewStatus;

        public ViewHolder(View view) {
            super(view);

            linearLayoutContainer = view.findViewById(R.id.branchInfo_item_linearLayout_container);
            textViewShopName = view.findViewById(R.id.branchInfo_item_textView_shopName);
            textViewAddress = view.findViewById(R.id.branchInfo_item_textView_address);
            textViewMobileNumber = view.findViewById(R.id.branchInfo_item_textView_mobileNumber);
            textViewDistance = view.findViewById(R.id.branchInfo_item_textView_distance);
            textViewStatus = view.findViewById(R.id.branchInfo_item_textView_status);
        }

        @Override
        public void onItemSelected(Context context) {

        }

        @Override
        public void onItemClear(Context context) {

        }
    }
}
