package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Location;
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.data.output.PagedBranchOutput;
import com.sultanburger.fragment.adapter.BranchInfoAdapter;
import com.sultanburger.fragment.handler.BranchHandler;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class PickUpFragment extends AppBaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener, BranchHandler, RVItemDragCallback {

    private static final String TAG = PickUpFragment.class.getSimpleName();

    private EditText editTextTypePickUpBranch;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private Button buttonContinue;

    private GoogleMap googleMap;
    private DashBoardHandler dashBoardHandler;

    private List<BranchOutput> branchOutputs;
    private BranchOutput selectedBranchOutput;
    private Location lastKnownLocation;

    public PickUpFragment() {

    }

    public static PickUpFragment newInstance() {
        PickUpFragment pickUpFragment = new PickUpFragment();
        return pickUpFragment;
    }

    public static PickUpFragment newInstance(ArrayList<BranchOutput> branchOutputs) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("branchOutputs", branchOutputs);

        PickUpFragment pickUpFragment = new PickUpFragment();
        pickUpFragment.setArguments(bundle);
        return pickUpFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            branchOutputs = getArguments().getParcelableArrayList("branchOutputs");
        } catch (NullPointerException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_up, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        editTextTypePickUpBranch = view.findViewById(R.id.pickUp_editText_typePickUpBranch);
        editTextTypePickUpBranch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BranchInfoAdapter branchInfoAdapter = (BranchInfoAdapter) recyclerView.getAdapter();
                if (Validator.isValid(branchInfoAdapter))
                    branchInfoAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.pickUp_map);
        supportMapFragment.getMapAsync(this);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = view.findViewById(R.id.pickUp_recyclerView_branchInfo);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RVGridSpacingDecoration(1, 20, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

        buttonContinue = view.findViewById(R.id.pickUp_button_continue);
        buttonContinue.setOnClickListener(this);

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
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setMyLocationEnabled(true);

        setMapUiSettings(googleMap);

        if (!Validator.isValid(branchOutputs)) {
            getLastKnownLocation(new DataReceiver<Location>() {
                @Override
                public void receiveData(Location result) {
                    if (Validator.isValid(result)) {
                        lastKnownLocation = result;

                        focusLocation(googleMap, result, false);
                        populateRecyclerView();
                    } else {
                        ToastUtil.showToast(getContext(), UNABLE_TO_GET_CURRENT_LOCATION);
                    }
                }
            });
        } else if (Validator.isValid(branchOutputs) && !branchOutputs.isEmpty()) {
            populateData(branchOutputs);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String snippet = marker.getSnippet();
        return false;
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
            case R.id.pickUp_button_continue: {
                if (Validator.isValid(selectedBranchOutput))
                    dashBoardHandler.menu(selectedBranchOutput.getBranchId());
                else
                    ToastUtil.showToast(getContext(), SELECT_THE_BRANCH);
            }
            break;
        }
    }

    @Override
    public void onBranchSelected(BranchOutput branchOutput) {
        selectedBranchOutput = branchOutput;
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    private void populateRecyclerView() {
        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        getAppBaseActivity().getListUserNearByBranches(latLng, "1", new DataReceiver<PagedBranchOutput>() {
            @Override
            public void receiveData(PagedBranchOutput result) {
                if (Validator.isValid(result))
                    populateData(result.getBranchLists());
            }
        });
    }

    private void populateData(List<BranchOutput> branchLists) {
        if (Validator.isValid(branchLists) && !branchLists.isEmpty()) {
            addBranchMarker(googleMap, branchLists);

            BranchInfoAdapter branchInfoAdapter = new BranchInfoAdapter(branchLists, PickUpFragment.this, PickUpFragment.this);
            recyclerView.setAdapter(branchInfoAdapter);

            RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), branchInfoAdapter);
            rvItemTouchHelper.setSwipeEnabled(false);
            rvItemTouchHelper.setSwipeDirectionRight(false);

            itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }
}
