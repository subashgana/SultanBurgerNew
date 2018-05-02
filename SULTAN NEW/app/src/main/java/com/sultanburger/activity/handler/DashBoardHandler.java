package com.sultanburger.activity.handler;

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

import java.util.List;

public interface DashBoardHandler {

    void option();

    void signIn();

    PickUpFragment pickUp();

    PickUpFragment pickUp(List<BranchOutput> branchOutputs);

    DeliveryFragment delivery();

    MenuFragment menu(String branchId);

    ModifiersFragment modifiers(CartItem cartItem, String actionName);

    AddOnsFragment addons(CartItem cartItem);

    OrderTrackingFragment orderTracking();

    OrderTrackingMobileNumberFragment orderTrackingMobileNumber();

    PromosFragment promos();

    BranchesFragment branches();

    AccountFragment account();

    AccountSettingsFragment accountSettings();

    AccountInfoFragment accountInfo();

    void changePassword();

    void logout();

    ReportProblemFragment reportProblem();

    void termsOfUse();

    void about();

    void rateAndReview();

    OrderHistoryFragment orderHistory();

    MyAddress myAddress(boolean showButtons);

    LoyaltyProgramFragment loyaltyProgram();

    CartFragment cart();
}
