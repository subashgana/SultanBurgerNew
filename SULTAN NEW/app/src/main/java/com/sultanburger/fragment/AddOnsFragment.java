package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.AddOns;
import com.sultanburger.data.output.AddOnsCategory;
import com.sultanburger.data.output.AddOnsOutput;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Validator;

import java.util.List;

public class AddOnsFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = AddOnsFragment.class.getSimpleName();

    private LinearLayout linearLayoutAddonsContainer;
    private Button buttonDone;

    private CartItem cartItem;
    private DashBoardHandler dashBoardHandler;

    public AddOnsFragment() {

    }

    public static AddOnsFragment newInstance(CartItem cartItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartItem", cartItem);

        AddOnsFragment addOnsFragment = new AddOnsFragment();
        addOnsFragment.setArguments(bundle);
        return addOnsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartItem = (CartItem) getArguments().getSerializable("cartItem");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addons, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        linearLayoutAddonsContainer = view.findViewById(R.id.addons_linearLayout_addonsContainer);

        buttonDone = view.findViewById(R.id.addons_button_done);
        buttonDone.setOnClickListener(this);

        populateAddOns();
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

        switch (id) {
            case R.id.addons_button_done:
                SultanBurgerApplication.getInstance().addCartItem(cartItem);

                String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
                dashBoardHandler.menu(branchId);
                break;
        }
    }

    private void populateAddOns() {
        getAppBaseActivity().getAddOns(cartItem.getListMenu().getMenuItemId(), new DataReceiver<AddOnsOutput>() {
            @Override
            public void receiveData(AddOnsOutput result) {
                if (Validator.isValid(result)) {
                    List<AddOnsCategory> addOnsCategories = result.getAddOnsDetails();
                    if (Validator.isValid(addOnsCategories) && !addOnsCategories.isEmpty()) {
                        cartItem.setAddOnsCategories(addOnsCategories);

                        for (AddOnsCategory addOnsCategory : addOnsCategories) {
                            addAddOnsContainer(addOnsCategory);
                        }
                    }
                }
            }
        });
    }

    private void addAddOnsContainer(AddOnsCategory addOnsCategory) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_addons_item, null);

        TextView textViewCategoryName = view.findViewById(R.id.addonsContainer_item_textView_categoryName);
        LinearLayout linearLayoutSwitchContainer = view.findViewById(R.id.addonsContainer_item_switchContainer);

        textViewCategoryName.setText(addOnsCategory.getAddOnsCategoryName());

        for (AddOns addOns : addOnsCategory.getAddOns()) {
            Switch aSwitch = new Switch(getContext());
            aSwitch.setId(Integer.parseInt(addOns.getAddOnsId()));
            aSwitch.setText(addOns.getAddOnsName());
            aSwitch.setTextColor(DrawableUtil.getColor(getContext(), R.color.light_text_secondary));

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutSwitchContainer.addView(aSwitch, layoutParams);
        }

        linearLayoutAddonsContainer.addView(view);
    }
}