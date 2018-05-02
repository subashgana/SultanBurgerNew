package com.sultanburger.helper.recyclerview;

import android.content.Context;

public interface RVViewHolderCallback {
    void onItemSelected(Context context);

    void onItemClear(Context context);
}
