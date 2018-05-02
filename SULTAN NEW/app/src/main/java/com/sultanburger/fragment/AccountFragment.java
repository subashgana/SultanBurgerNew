package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.output.UserDetail;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

public class AccountFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = AccountFragment.class.getSimpleName();

    private ImageView imageViewAvatar;
    private TextView textViewUserName;
    private LinearLayout linearLayoutAccountSettings;
    private LinearLayout linearLayoutOrderHistory;
    private LinearLayout linearLayoutMyAddress;
    private LinearLayout linearLayoutOrderTracking;

    private DashBoardHandler dashBoardHandler;
    private Dimens avatarImageSize;

    private boolean isGuestUser;

    public AccountFragment() {

    }

    public static AccountFragment newInstance() {
        AccountFragment accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        avatarImageSize = Dimens.from(Utils.getAvatarImageSize(getContext()), Utils.getAvatarImageSize(getContext()));
        isGuestUser = getAppBaseActivity().getPreferenceHelper().getBoolean(PREF_IS_GUEST_USER);

        imageViewAvatar = view.findViewById(R.id.account_imageView_avatar);
        imageViewAvatar.getLayoutParams().width = avatarImageSize.getWidth();
        imageViewAvatar.getLayoutParams().height = avatarImageSize.getHeight();
        imageViewAvatar.setOnClickListener(this);

        textViewUserName = view.findViewById(R.id.account_textView_userName);

        linearLayoutAccountSettings = view.findViewById(R.id.account_linearLayout_accountSettings);
        linearLayoutAccountSettings.setOnClickListener(this);

        linearLayoutOrderHistory = view.findViewById(R.id.account_linearLayout_orderHistory);
        linearLayoutOrderHistory.setOnClickListener(this);

        linearLayoutMyAddress = view.findViewById(R.id.account_linearLayout_myAddress);
        linearLayoutMyAddress.setOnClickListener(this);

        linearLayoutOrderTracking = view.findViewById(R.id.account_linearLayout_orderTracking);
        linearLayoutOrderTracking.setOnClickListener(this);

        populateData();
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
        inflater.inflate(R.menu.menu_empty, menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.account_imageView_avatar:
                break;

            case R.id.account_linearLayout_accountSettings:
                if (!isGuestUser)
                    dashBoardHandler.accountSettings();
                break;

            case R.id.account_linearLayout_orderHistory:
                if (!isGuestUser)
                    dashBoardHandler.orderHistory();
                break;

            case R.id.account_linearLayout_myAddress:
                if (!isGuestUser)
                    dashBoardHandler.myAddress(false);
                break;

            case R.id.account_linearLayout_orderTracking:
                if (!isGuestUser)
                    dashBoardHandler.orderTracking();
                break;
        }
    }

    private void populateData() {
        UserDetail userDetail = SultanBurgerApplication.getInstance().getUserDetail();
        if (Validator.isValid(userDetail)) {
            ImageUtil.setCircularImage(getContext(), imageViewAvatar, userDetail.getProfileImage(), R.drawable.ic_account);
            textViewUserName.setText(userDetail.getName());
        }
    }
}
