package com.sultanburger.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sultanburger.R;
import com.sultanburger.data.item.ListDataItem;
import com.sultanburger.dialog.handler.ListListener;
import com.sultanburger.utils.Validator;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<ListDataItem> data;
    private ListListener listListener;

    private ViewHolder viewHolder;

    public ListAdapter(Context context, List<ListDataItem> data, ListListener listListener) {
        this.context = context;
        this.data = data;
        this.listListener = listListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_list_item, parent, false);

        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListDataItem listDataItem = this.data.get(position);
        String data = listDataItem.getData();

        holder.textView.setText(data);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.isValid(listListener))
                    listListener.onSelected(listDataItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.alert_list_item_textView);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
