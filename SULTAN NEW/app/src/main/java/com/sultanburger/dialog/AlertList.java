package com.sultanburger.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sultanburger.AppConstants;
import com.sultanburger.R;
import com.sultanburger.data.item.ListDataItem;
import com.sultanburger.dialog.adapter.ListAdapter;
import com.sultanburger.dialog.handler.ListListener;
import com.sultanburger.utils.Validator;

import java.util.List;

public class AlertList extends Dialog implements AppConstants {

    private TextView textViewTitle;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    private ListListener listListener;

    public AlertList(Context context) {
        super(context);
        init();
    }

    public AlertList(Context context, int theme) {
        super(context, theme);
        init();
    }

    public AlertList(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public static void showAlert(Context context, String title, List<ListDataItem> listDatumItems, final ListListener listListener) {
        final AlertList alertList = new AlertList(context);
        alertList.setCustomTitle(title);
        alertList.setListListener(new ListListener() {
            @Override
            public void onSelected(ListDataItem data) {
                alertList.dismiss();

                if (Validator.isValid(listListener))
                    listListener.onSelected(data);
            }
        });
        alertList.updateList(listDatumItems);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertList.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertList.show();
        alertList.getWindow().setAttributes(layoutParams);
    }


    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_list);

        textViewTitle = findViewById(R.id.alert_list_textView_title);

        recyclerView = findViewById(R.id.alert_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    public void setCustomTitle(String title) {
        textViewTitle.setText(title);
    }

    public void setListListener(ListListener listListener) {
        this.listListener = listListener;
    }

    public void updateList(List<ListDataItem> data) {
        if (Validator.isValid(data) && !data.isEmpty()) {
            listAdapter = new ListAdapter(getContext(), data, listListener);
            recyclerView.setAdapter(listAdapter);
        }
    }
}