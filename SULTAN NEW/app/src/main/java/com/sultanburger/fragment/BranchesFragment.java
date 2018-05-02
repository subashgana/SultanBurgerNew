package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Validator;

import java.util.List;

public class BranchesFragment extends AppBaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = BranchesFragment.class.getSimpleName();

    private EditText editTextSelectBranch;

    private GoogleMap googleMap;
    private DashBoardHandler dashBoardHandler;

    private Location lastKnownLocation;

    public BranchesFragment() {

    }

    public static BranchesFragment newInstance() {
        BranchesFragment branchesFragment = new BranchesFragment();
        return branchesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branches, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        editTextSelectBranch = view.findViewById(R.id.branch_editText_typeToSelectBranch);
        editTextSelectBranch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.branch_map);
        supportMapFragment.getMapAsync(this);

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
        getLastKnownLocation(new DataReceiver<Location>() {
            @Override
            public void receiveData(Location result) {
                if (Validator.isValid(result)) {
                    lastKnownLocation = result;

                    focusLocation(googleMap, result, false);
                    populateMap();
                }
            }
        });
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
    }

    private void populateMap() {
        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        getAppBaseActivity().getListUserNearByBranches(latLng, "1", new DataReceiver<PagedBranchOutput>() {
            @Override
            public void receiveData(PagedBranchOutput result) {
                if (Validator.isValid(result)) {
                    List<BranchOutput> branchLists = result.getBranchLists();
                    if (Validator.isValid(branchLists) && !branchLists.isEmpty())
                        addBranchMarker(googleMap, branchLists);
                }
            }
        });
    }
}
