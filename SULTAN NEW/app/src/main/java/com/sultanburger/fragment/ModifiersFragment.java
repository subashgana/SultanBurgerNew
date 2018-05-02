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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.Modifiers;
import com.sultanburger.data.output.ModifiersCategory;
import com.sultanburger.data.output.ModifiersOutput;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Validator;

import java.util.List;

public class ModifiersFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = ModifiersFragment.class.getSimpleName();

    private LinearLayout linearLayoutModifierContainer;
    private Button buttonNext;

    private CartItem cartItem;
    private String actionName;
    private DashBoardHandler dashBoardHandler;

    public ModifiersFragment() {

    }

    public static ModifiersFragment newInstance(CartItem cartItem, String actionName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cartItem", cartItem);
        bundle.putString("actionName", actionName);

        ModifiersFragment modifiersFragment = new ModifiersFragment();
        modifiersFragment.setArguments(bundle);
        return modifiersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartItem = (CartItem) getArguments().getSerializable("cartItem");
        actionName = getArguments().getString("actionName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modifiers, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        linearLayoutModifierContainer = view.findViewById(R.id.modifiers_linearLayout_modifierContainer);

        buttonNext = view.findViewById(R.id.modifiers_button_next);
        buttonNext.setText(actionName);
        buttonNext.setOnClickListener(this);

        populateModifiers();
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
            case R.id.modifiers_button_next: {
                if (actionName.equals(getString(R.string.modifiers_next))) {
                    dashBoardHandler.addons(cartItem);
                } else if (actionName.equals(getString(R.string.modifiers_done))) {
                    SultanBurgerApplication.getInstance().addCartItem(cartItem);

                    String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
                    dashBoardHandler.menu(branchId);
                }
            }
            break;
        }
    }

    private void populateModifiers() {
        getAppBaseActivity().getModifiers(cartItem.getListMenu().getMenuItemId(), new DataReceiver<ModifiersOutput>() {
            @Override
            public void receiveData(ModifiersOutput result) {
                if (Validator.isValid(result)) {
                    List<ModifiersCategory> modifiersCategories = result.getModifiersDetails();
                    if (Validator.isValid(modifiersCategories) && !modifiersCategories.isEmpty()) {
                        cartItem.setModifiersCategories(modifiersCategories);

                        for (ModifiersCategory modifiersCategory : modifiersCategories) {
                            addModifierContainer(modifiersCategory);
                        }
                    }
                }
            }
        });
    }

    private void addModifierContainer(ModifiersCategory modifiersCategory) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_modifiers_item, null);

        TextView textViewCategoryName = view.findViewById(R.id.modifiersContainer_item_textView_categoryName);
        RadioGroup radioGroupModifiers = view.findViewById(R.id.modifiersContainer_item_radioGroup_modifiers);

        textViewCategoryName.setText(modifiersCategory.getModifierCategoryName());

        for (Modifiers modifiers : modifiersCategory.getModifiers()) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(Integer.parseInt(modifiers.getModifierId()));
            radioButton.setText(modifiers.getModifierName());
            radioButton.setTextColor(DrawableUtil.getColor(getContext(), R.color.light_text_secondary));

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroupModifiers.addView(radioButton, layoutParams);
        }

        linearLayoutModifierContainer.addView(view);
    }
}
