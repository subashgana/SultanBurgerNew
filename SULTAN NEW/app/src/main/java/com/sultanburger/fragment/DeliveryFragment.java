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
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Address;
import com.sultanburger.data.Location;
import com.sultanburger.data.input.AddUserAddressInput;
import com.sultanburger.data.output.AddUserAddressOutput;
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.StringUtil;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFragment extends AppBaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = DeliveryFragment.class.getSimpleName();

    private EditText editTextHouseOrOfficeNumber;
    private EditText editTextStreetNumberOrName;
    private EditText editTextFloor;
    private EditText editTextAreaCity;
    private EditText editTextDeliveryInstruction;
    private Button buttonChooseFromExisting;
    private Button buttonUseThisAddress;

    private GoogleMap googleMap;
    private Location location;
    private DashBoardHandler dashBoardHandler;

    public DeliveryFragment() {

    }

    public static DeliveryFragment newInstance() {
        DeliveryFragment deliveryFragment = new DeliveryFragment();
        return deliveryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.delivery_map);
        supportMapFragment.getMapAsync(this);

        editTextHouseOrOfficeNumber = view.findViewById(R.id.delivery_editText_houseOrOfficeNumber);
        editTextStreetNumberOrName = view.findViewById(R.id.delivery_editText_streetNumberName);
        editTextFloor = view.findViewById(R.id.delivery_editText_floor);
        editTextAreaCity = view.findViewById(R.id.delivery_editText_areaCity);
        editTextDeliveryInstruction = view.findViewById(R.id.delivery_editText_deliveryInstruction);

        buttonChooseFromExisting = view.findViewById(R.id.delivery_button_chooseFromExisting);
        buttonChooseFromExisting.setOnClickListener(this);

        buttonUseThisAddress = view.findViewById(R.id.delivery_button_useThisAddress);
        buttonUseThisAddress.setOnClickListener(this);

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
                    location = result;

                    Address address = result.getAddress();
                    if (Validator.isValid(address)) {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(address.getLine1());
                        temp.add(address.getLine2());

                        editTextStreetNumberOrName.setText(StringUtil.collectionToCSV(temp));
                        editTextAreaCity.setText(address.getCity() + ", " + address.getState() + ", " + address.getCountry() + ", " + address.getPinCode());
                    }

                    focusLocation(googleMap, result, false);
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

        switch (id) {
            case R.id.delivery_button_chooseFromExisting:
                dashBoardHandler.myAddress(true);
                break;

            case R.id.delivery_button_useThisAddress:
                doUseThisAddress();
                break;
        }
    }

    private void doUseThisAddress() {
        String city = "";
        String state = "";
        String country = "";
        String pincode = "";

        String areaCity = editTextAreaCity.getText().toString();
        if (Validator.isValid(areaCity)) {
            String[] split = areaCity.split(",");

            if (split.length == 4) {
                city = split[0];
                state = split[1];
                country = split[2];
                pincode = split[3];
            } else if (split.length == 3) {
                city = split[0];
                state = split[1];
                country = split[2];
            } else if (split.length == 2) {
                city = split[0];
                state = split[1];
            } else if (split.length == 1) {
                city = split[0];
            }
        }

        AddUserAddressInput addUserAddressInput = new AddUserAddressInput();
        addUserAddressInput.setAddressLine1(editTextHouseOrOfficeNumber.getText().toString());
        addUserAddressInput.setAddressLine2(editTextStreetNumberOrName.getText().toString());
        addUserAddressInput.setAddressLine3(editTextFloor.getText().toString());
        addUserAddressInput.setAddressLine4("");
        addUserAddressInput.setCity(city);
        addUserAddressInput.setState(state);
        addUserAddressInput.setCountry(country);
        addUserAddressInput.setPincode(pincode);
        addUserAddressInput.setLatitude(Validator.isValid(location) ? String.valueOf(location.getLatitude()) : "");
        addUserAddressInput.setLongitude(Validator.isValid(location) ? String.valueOf(location.getLongitude()) : "");
        addUserAddressInput.setDeliveryInstruction(editTextDeliveryInstruction.getText().toString());

        getAppBaseActivity().addUserAddress(addUserAddressInput, new DataReceiver<AddUserAddressOutput>() {
            @Override
            public void receiveData(AddUserAddressOutput result) {
                if (Validator.isValid(result)) {
                    List<BranchOutput> deliveryBranchDetails = result.getDeliveryBranchDetails();
                    dashBoardHandler.pickUp(deliveryBranchDetails);
                }
            }
        });
    }
}
