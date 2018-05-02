package com.sultanburger.fragment.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.recyclerview.RVAdapterCallback;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVViewHolderCallback;
import com.sultanburger.utils.StringUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuTabDataAdapter extends RecyclerView.Adapter<MenuTabDataAdapter.ViewHolder> implements RVAdapterCallback {

    private static final String TAG = MenuTabDataAdapter.class.getSimpleName();

    private Context context;
    private List<ListMenu> listMenus;
    private final DashBoardHandler dashBoardHandler;
    private final RVItemDragCallback rvItemDragCallback;

    public MenuTabDataAdapter(List<ListMenu> listMenus, DashBoardHandler dashBoardHandler, RVItemDragCallback rvItemDragCallback) {
        this.listMenus = new ArrayList<>(listMenus);
        this.dashBoardHandler = dashBoardHandler;
        this.rvItemDragCallback = rvItemDragCallback;
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
                            boolean isModifierPresent = listMenu.getModifiersPresent().equals("1");
                            boolean isAddOnsPresent = listMenu.getAddOnsPresent().equals("1");
                            String actionName = isAddOnsPresent ? context.getResources().getString(R.string.modifiers_next) : context.getResources().getString(R.string.modifiers_done);

                            CartItem cartItem = new CartItem();
                            cartItem.setListMenu(listMenu);

                            if (isModifierPresent)
                                dashBoardHandler.modifiers(cartItem, actionName);
                            else if (isAddOnsPresent)
                                dashBoardHandler.addons(cartItem);
                            else
                                SultanBurgerApplication.getInstance().addCartItem(cartItem);
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
