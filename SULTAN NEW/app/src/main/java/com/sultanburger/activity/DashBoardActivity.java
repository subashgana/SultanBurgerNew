package com.sultanburger.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sultanburger.ActivityConfig;
import com.sultanburger.AppBaseActivity;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.fragment.AccountFragment;
import com.sultanburger.fragment.AccountInfoFragment;
import com.sultanburger.fragment.AccountSettingsFragment;
import com.sultanburger.fragment.AddOnsFragment;
import com.sultanburger.fragment.BranchesFragment;
import com.sultanburger.fragment.CartFragment;
import com.sultanburger.fragment.DeliveryFragment;
import com.sultanburger.fragment.LoyaltyProgramFragment;
import com.sultanburger.fragment.MenuFragment;
import com.sultanburger.fragment.ModifiersFragment;
import com.sultanburger.fragment.MyAddress;
import com.sultanburger.fragment.OrderHistoryFragment;
import com.sultanburger.fragment.OrderTrackingFragment;
import com.sultanburger.fragment.OrderTrackingMobileNumberFragment;
import com.sultanburger.fragment.PickUpFragment;
import com.sultanburger.fragment.PromosFragment;
import com.sultanburger.fragment.ReportProblemFragment;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardActivity extends AppBaseActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, DashBoardHandler {

    private static final String TAG = DashBoardActivity.class.getSimpleName();

    private static final String SIGN_IN = "SignIn";
    private static final String PICKUP = "PickUp";
    private static final String DELIVERY = "Select Address";
    private static final String HOME = "Home";
    private static final String MENU = "Menu";
    private static final String MODIFIERS = "Modifiers";
    private static final String ADDONS = "AddOns";
    private static final String ORDER_TRACKING = "Order Tracking";
    private static final String ORDER_TRACKING_MOBILE_NUMBER = "Order Tracking";
    private static final String PROMOS = "Promos";
    private static final String BRANCHES = "Branches";
    private static final String ACCOUNT = "Account";
    private static final String ORDER_HISTORY = "Order History";
    private static final String MY_ADDRESS = "My Address";
    private static final String EXISTING_ADDRESS = "Existing Address";
    private static final String ACCOUNT_SETTINGS = "Account Settings";
    private static final String ACCOUNT_INFO = "Account Info";
    private static final String REPORT_A_PROBLEM = "Report a Problem";
    private static final String LOYALTY_PROGRAM = "Loyalty Program";
    private static final String CART = "Cart";
    private static final String LANGUAGE = "English / Arabic";

    private static final int BACK_PRESSED_TIME_INTERVAL = 2000;

    private Toolbar toolbar;
    private TextView textViewToolbarTitle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private Map<String, AppBaseFragment> fragments = new HashMap<>();
    private Fragment currentFragment = null;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
        populateNavigationViewMenu();
        populateBottomNavigationViewMenu();

        String optionSelectionTypeStr = getPreferenceHelper().getString(PREF_OPTION_SELECTION_TYPE);
        if (Validator.isValid(optionSelectionTypeStr)) {
            OptionSelectionType optionSelectionType = OptionSelectionType.valueOf(optionSelectionTypeStr);
            switch (optionSelectionType) {
                case PICKUP:
                    pickUp();
                    break;

                case DELIVERY:
                    delivery();
                    break;

                case RE_ORDER:
                    // TODO - Check for recent order and re-order
                    break;
            }
        } else {
            account();
        }
    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.dashboard_toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewToolbarTitle = toolbar.findViewById(R.id.dashboard_toolbar_title);

        drawerLayout = findViewById(R.id.dashboard_drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.dashboard_navigation_drawer_open, R.string.dashboard_navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigationView = findViewById(R.id.dashboard_navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.dashboard_bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case HOME:
                option();
                break;

            case ORDER_TRACKING:
                if (isUserSignedIn())
                    orderTracking();
                else
                    orderTrackingMobileNumber();
                break;

            case PROMOS:
                promos();
                break;

            case BRANCHES:
                branches();
                break;

            case ACCOUNT:
                if (getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN)) {
                    account();
                }
                else if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER)) {
                    signIn();
                   // account();
                }

                break;

            case LOYALTY_PROGRAM:
                loyaltyProgram();
                break;

            case SIGN_IN:
                if (getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN)) {
                    account();
                }
                else if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER)) {
                    signIn();
                    // account();
                }
                break;

            case MENU:
                String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
                menu(branchId);
                break;

            case CART:
                cart();
                break;
        }

        return true;
    }

    @Override
    public void option() {
        ActivityConfig.startOptionActivity(DashBoardActivity.this);
    }

    @Override
    public void signIn() {
        getPreferenceHelper().remove(PREF_OPTION_SELECTION_TYPE);
        ActivityConfig.startSignInActivity(DashBoardActivity.this);
    }

    @Override
    public PickUpFragment pickUp() {
        bottomNavigationView.setVisibility(View.GONE);

        PickUpFragment pickUpFragment = PickUpFragment.newInstance();
        fragments.put(PICKUP, pickUpFragment);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(pickUpFragment, PICKUP);

        return pickUpFragment;
    }

    @Override
    public PickUpFragment pickUp(List<BranchOutput> branchOutputs) {
        bottomNavigationView.setVisibility(View.GONE);

        PickUpFragment pickUpFragment = PickUpFragment.newInstance(new ArrayList<>(branchOutputs));
        fragments.put(PICKUP, pickUpFragment);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(pickUpFragment, PICKUP);

        return pickUpFragment;
    }

    @Override
    public DeliveryFragment delivery() {
        bottomNavigationView.setVisibility(View.GONE);

        DeliveryFragment deliveryFragment = (DeliveryFragment) fragments.get(DELIVERY);
        if (!Validator.isValid(deliveryFragment)) {
            deliveryFragment = DeliveryFragment.newInstance();
            fragments.put(DELIVERY, deliveryFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(deliveryFragment, DELIVERY);

        return deliveryFragment;
    }

    @Override
    public MenuFragment menu(String branchId) {
        bottomNavigationView.setVisibility(View.VISIBLE);
        populateBottomNavigationViewMenuIcon(1);

        MenuFragment menuFragment = (MenuFragment) fragments.get(MENU);
        if (!Validator.isValid(menuFragment)) {
            menuFragment = MenuFragment.newInstance(branchId);
            fragments.put(MENU, menuFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(menuFragment, MENU);

        return menuFragment;
    }

    @Override
    public ModifiersFragment modifiers(CartItem cartItem, String actionName) {
        bottomNavigationView.setVisibility(View.GONE);

        ModifiersFragment modifiersFragment = (ModifiersFragment) fragments.get(MODIFIERS);
        if (!Validator.isValid(modifiersFragment)) {
            modifiersFragment = ModifiersFragment.newInstance(cartItem, actionName);
            fragments.put(MODIFIERS, modifiersFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(modifiersFragment, MODIFIERS);

        return modifiersFragment;
    }

    @Override
    public AddOnsFragment addons(CartItem cartItem) {
        bottomNavigationView.setVisibility(View.GONE);

        AddOnsFragment addOnsFragment = (AddOnsFragment) fragments.get(ADDONS);
        if (!Validator.isValid(addOnsFragment)) {
            addOnsFragment = AddOnsFragment.newInstance(cartItem);
            fragments.put(ADDONS, addOnsFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(addOnsFragment, ADDONS);

        return addOnsFragment;
    }

    @Override
    public OrderTrackingFragment orderTracking() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        populateBottomNavigationViewMenuIcon(2);

        OrderTrackingFragment orderTrackingFragment = (OrderTrackingFragment) fragments.get(ORDER_TRACKING);
        if (!Validator.isValid(orderTrackingFragment)) {
            orderTrackingFragment = OrderTrackingFragment.newInstance();
            fragments.put(ORDER_TRACKING, orderTrackingFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(orderTrackingFragment, ORDER_TRACKING);

        return orderTrackingFragment;
    }

    @Override
    public OrderTrackingMobileNumberFragment orderTrackingMobileNumber() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        populateBottomNavigationViewMenuIcon(2);

        OrderTrackingMobileNumberFragment orderTrackingMobileNumberFragment = (OrderTrackingMobileNumberFragment) fragments.get(ORDER_TRACKING_MOBILE_NUMBER);
        if (!Validator.isValid(orderTrackingMobileNumberFragment)) {
            orderTrackingMobileNumberFragment = OrderTrackingMobileNumberFragment.newInstance();
            fragments.put(ORDER_TRACKING_MOBILE_NUMBER, orderTrackingMobileNumberFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(orderTrackingMobileNumberFragment, ORDER_TRACKING_MOBILE_NUMBER);

        return orderTrackingMobileNumberFragment;
    }

    @Override
    public PromosFragment promos() {
        bottomNavigationView.setVisibility(View.GONE);

        PromosFragment promosFragment = (PromosFragment) fragments.get(PROMOS);
        if (!Validator.isValid(promosFragment)) {
            promosFragment = PromosFragment.newInstance();
            fragments.put(PROMOS, promosFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(promosFragment, PROMOS);

        return promosFragment;
    }

    @Override
    public BranchesFragment branches() {
        bottomNavigationView.setVisibility(View.GONE);

        BranchesFragment branchesFragment = (BranchesFragment) fragments.get(BRANCHES);
        if (!Validator.isValid(branchesFragment)) {
            branchesFragment = BranchesFragment.newInstance();
            fragments.put(BRANCHES, branchesFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(branchesFragment, BRANCHES);

        return branchesFragment;
    }

    @Override
    public AccountFragment account() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        populateBottomNavigationViewMenuIcon(0);

        AccountFragment accountFragment = (AccountFragment) fragments.get(ACCOUNT);
        if (!Validator.isValid(accountFragment)) {
            accountFragment = AccountFragment.newInstance();
            fragments.put(ACCOUNT, accountFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(accountFragment, ACCOUNT);

        return accountFragment;
    }

    @Override
    public AccountSettingsFragment accountSettings() {
        bottomNavigationView.setVisibility(View.GONE);

        AccountSettingsFragment accountSettingsFragment = (AccountSettingsFragment) fragments.get(ACCOUNT_SETTINGS);
        if (!Validator.isValid(accountSettingsFragment)) {
            accountSettingsFragment = AccountSettingsFragment.newInstance();
            fragments.put(ACCOUNT_SETTINGS, accountSettingsFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(accountSettingsFragment, ACCOUNT_SETTINGS);

        return accountSettingsFragment;
    }

    @Override
    public AccountInfoFragment accountInfo() {
        bottomNavigationView.setVisibility(View.GONE);

        AccountInfoFragment accountInfoFragment = (AccountInfoFragment) fragments.get(ACCOUNT_INFO);
        if (!Validator.isValid(accountInfoFragment)) {
            accountInfoFragment = AccountInfoFragment.newInstance();
            fragments.put(ACCOUNT_INFO, accountInfoFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(accountInfoFragment, ACCOUNT_INFO);

        return accountInfoFragment;
    }

    @Override
    public void changePassword() {
        ActivityConfig.startChangePasswordActivity(DashBoardActivity.this);
    }

    @Override
    public void logout() {
        super.logout();
    }

    @Override
    public ReportProblemFragment reportProblem() {
        bottomNavigationView.setVisibility(View.GONE);

        ReportProblemFragment reportProblemFragment = (ReportProblemFragment) fragments.get(REPORT_A_PROBLEM);
        if (!Validator.isValid(reportProblemFragment)) {
            reportProblemFragment = ReportProblemFragment.newInstance();
            fragments.put(REPORT_A_PROBLEM, reportProblemFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(reportProblemFragment, REPORT_A_PROBLEM);

        return reportProblemFragment;
    }

    @Override
    public void termsOfUse() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void about() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void rateAndReview() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public OrderHistoryFragment orderHistory() {
        bottomNavigationView.setVisibility(View.GONE);

        OrderHistoryFragment orderHistoryFragment = (OrderHistoryFragment) fragments.get(ORDER_HISTORY);
        if (!Validator.isValid(orderHistoryFragment)) {
            orderHistoryFragment = OrderHistoryFragment.newInstance();
            fragments.put(ORDER_HISTORY, orderHistoryFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(orderHistoryFragment, ORDER_HISTORY);

        return orderHistoryFragment;
    }

    @Override
    public MyAddress myAddress(boolean showButtons) {
        bottomNavigationView.setVisibility(View.GONE);

        MyAddress myAddressFragment = (MyAddress) fragments.get(showButtons ? EXISTING_ADDRESS : MY_ADDRESS);
        if (!Validator.isValid(myAddressFragment)) {
            myAddressFragment = MyAddress.newInstance(showButtons);
            fragments.put(showButtons ? EXISTING_ADDRESS : MY_ADDRESS, myAddressFragment);
        }

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        updateView(myAddressFragment, showButtons ? EXISTING_ADDRESS : MY_ADDRESS);

        return myAddressFragment;
    }

    @Override
    public LoyaltyProgramFragment loyaltyProgram() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        LoyaltyProgramFragment loyaltyProgramFragment = LoyaltyProgramFragment.newInstance();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(loyaltyProgramFragment, LOYALTY_PROGRAM);
        return loyaltyProgramFragment;
    }

    @Override
    public CartFragment cart() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        populateBottomNavigationViewMenuIcon(3);

        CartFragment cartFragment = CartFragment.newInstance();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        updateView(cartFragment, CART);
        return cartFragment;
    }

    @Override
    public void onBackPressed() {
        if (Validator.isValid(currentFragment)) {
            if (currentFragment instanceof MenuFragment) {
                if (backPressedTime + BACK_PRESSED_TIME_INTERVAL > System.currentTimeMillis())
                    finish();
                else
                    ToastUtil.showToast(getApplicationContext(), "Tap again to exit");

                backPressedTime = System.currentTimeMillis();
            } else if (currentFragment instanceof AccountSettingsFragment
                    || currentFragment instanceof OrderHistoryFragment
                    || currentFragment instanceof MyAddress) {
                account();
            } else if (currentFragment instanceof AccountInfoFragment
                    || currentFragment instanceof ReportProblemFragment) {
                accountSettings();
            } else if (currentFragment instanceof MyAddress) {
                delivery();
            } else if (currentFragment instanceof PickUpFragment
                    || currentFragment instanceof DeliveryFragment) {
                ActivityConfig.startOptionActivity(DashBoardActivity.this);
            } else {
                String branchId = SultanBurgerApplication.getInstance().getSelectedBranchId();
                menu(branchId);
            }
        }
    }

    private void populateNavigationViewMenu() {
        Menu menu = navigationView.getMenu();
        menu.clear();

        menu.add(HOME).setIcon(R.drawable.ic_home);
        menu.add(ORDER_TRACKING).setIcon(R.drawable.ic_order_tracking);
        menu.add(PROMOS).setIcon(R.drawable.ic_promo);
        menu.add(BRANCHES).setIcon(R.drawable.ic_branches);
        menu.add(ACCOUNT).setIcon(R.drawable.ic_account);
        menu.add(LOYALTY_PROGRAM).setIcon(R.drawable.ic_loyalty_program);
        menu.add(LANGUAGE).setIcon(R.drawable.ic_language);

        // Select first
//        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
    }

    private void populateBottomNavigationViewMenu() {
        Menu menu = bottomNavigationView.getMenu();
        menu.clear();

        if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER)) {
            menu.add(ACCOUNT).setIcon(R.drawable.ic_account);
        }
        else {
            menu.add(SIGN_IN).setIcon(R.drawable.ic_login);
        }

        menu.add(MENU).setIcon(R.drawable.ic_menu);
        menu.add(ORDER_TRACKING).setIcon(R.drawable.ic_order_tracking);
        menu.add(CART).setIcon(R.drawable.ic_cart);
    }

    private void populateBottomNavigationViewMenuIcon(int position) {
        switch (position) {
            case 0:
                MenuItem menuItemAccount = bottomNavigationView.getMenu().getItem(0);
                menuItemAccount.setChecked(true);

                if (getPreferenceHelper().getBoolean(PREF_IS_USER_SIGNED_IN)) {
                    menuItemAccount.setIcon(R.drawable.ic_account_selected);
                }

                else
                    menuItemAccount.setIcon(R.drawable.ic_login);

                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_menu);
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_order_tracking);
                bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_cart);
                break;

            case 1:
                if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER))
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_login);
                else
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_account);

                MenuItem menuItemMenu = bottomNavigationView.getMenu().getItem(1);
                menuItemMenu.setChecked(true);
                menuItemMenu.setIcon(R.drawable.ic_menu);

                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_order_tracking);
                bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_cart);
                break;

            case 2:
                if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER))
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_login);
                else
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_account);

                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_menu);

                MenuItem menuItemOrderTracking = bottomNavigationView.getMenu().getItem(2);
                menuItemOrderTracking.setChecked(true);
                menuItemOrderTracking.setIcon(R.drawable.ic_order_tracking);

                bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_cart);
                break;

            case 3:
                if (getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER))
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_login);
                else
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_account);

                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_menu);
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_order_tracking);

                MenuItem menuItemOrderCart = bottomNavigationView.getMenu().getItem(3);
                menuItemOrderCart.setChecked(true);
                menuItemOrderCart.setIcon(R.drawable.ic_cart);
                break;
        }
    }

    private void updateView(Fragment fragment, String tag) {
        currentFragment = fragment;
        drawerLayout.closeDrawer(GravityCompat.START);
        textViewToolbarTitle.setText(tag.toUpperCase());

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragmentPreview = fragmentManager.findFragmentByTag(tag);
            if (Validator.isValid(fragmentPreview)) {
                try {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragmentPreview);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    Logger.writeLog(TAG, "updateView(fragmentPreview) - Exception : " + e.toString());
                }

                fragment = fragmentPreview;
            }

            try {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_relativeLayout_container, fragment, tag);
//                fragmentTransaction.add(fragment, tag);
                fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            } catch (Exception e) {
                Logger.writeLog(TAG, "updateView - Exception : " + e.toString());
            }

            fragmentManager.executePendingTransactions();
        } catch (IllegalStateException e) {
            Logger.writeLog(TAG, "updateView - IllegalStateException : " + e.toString());
        }
    }
}
