package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.output.ListUserAddress;
import com.sultanburger.data.output.ListUserAddressOutput;
import com.sultanburger.fragment.adapter.MyAddressAdapter;
import com.sultanburger.fragment.handler.MyAddressHandler;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

import java.util.List;

public class MyAddress extends AppBaseFragment implements View.OnClickListener, MyAddressHandler, RVItemDragCallback {

    private static final String TAG = MyAddress.class.getSimpleName();

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayout linearLayoutBottomButtons;
    private Button buttonCancel;
    private Button buttonUseThisAddress;

    private DashBoardHandler dashBoardHandler;
    private boolean showButtons;
    private ListUserAddress selectedListUserAddress;

    public MyAddress() {

    }

    public static MyAddress newInstance(boolean showButtons) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showButtons", showButtons);

        MyAddress myAddressFragment = new MyAddress();
        myAddressFragment.setArguments(bundle);
        return myAddressFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showButtons = getArguments().getBoolean("showButtons");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_address, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = view.findViewById(R.id.myAddress_recyclerView_address);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RVGridSpacingDecoration(1, 20, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

        linearLayoutBottomButtons = view.findViewById(R.id.myAddress_linearLayout_bottomContainer);
        linearLayoutBottomButtons.setVisibility(showButtons ? View.VISIBLE : View.GONE);

        buttonCancel = view.findViewById(R.id.myAddress_button_cancel);
        buttonCancel.setOnClickListener(this);

        buttonUseThisAddress = view.findViewById(R.id.myAddress_button_useThisAddress);
        buttonUseThisAddress.setOnClickListener(this);

        populateRecyclerView();
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
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.myAddress_button_cancel:
                dashBoardHandler.delivery();
                break;

            case R.id.myAddress_button_useThisAddress:
                if (Validator.isValid(selectedListUserAddress))
                    dashBoardHandler.pickUp(selectedListUserAddress.getDeliveryBranchDetails());
                else
                    ToastUtil.showToast(getContext(), "Select address");
                break;
        }
    }

    @Override
    public void onAddressSelected(ListUserAddress listUserAddress) {
        selectedListUserAddress = listUserAddress;
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    private void populateRecyclerView() {
        getAppBaseActivity().getUserAddress(new DataReceiver<ListUserAddressOutput>() {
            @Override
            public void receiveData(ListUserAddressOutput result) {
                if (Validator.isValid(result)) {
                    List<ListUserAddress> userAddressDetails = result.getUserAddressDetails();
                    if (Validator.isValid(userAddressDetails) && !userAddressDetails.isEmpty()) {
                        MyAddressAdapter myAddressAdapter = new MyAddressAdapter(userAddressDetails, MyAddress.this, MyAddress.this);
                        recyclerView.setAdapter(myAddressAdapter);

                        RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), myAddressAdapter);
                        rvItemTouchHelper.setSwipeEnabled(false);
                        rvItemTouchHelper.setSwipeDirectionRight(false);

                        itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                    } else {
                        ToastUtil.showToast(getContext(), "No existing address details found");

                        Utils.makeDelay(1000, new Utils.HandlerCallBack() {
                            @Override
                            public void onDelayCompleted() {
                                dashBoardHandler.delivery();
                            }
                        });
                    }
                }
            }
        });
    }
}
