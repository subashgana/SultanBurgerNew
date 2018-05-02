package com.sultanburger.helper.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface RVAdapterCallback {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(RecyclerView.ViewHolder viewHolder, int position, boolean isSwipedRight);
}
