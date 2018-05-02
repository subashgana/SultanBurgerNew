package com.sultanburger.utils;

import android.content.Context;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.sultanburger.data.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationUtil {

    private static final String TAG = LocationUtil.class.getSimpleName();

    public static Address getAddress(@NonNull Context context, @NonNull LatLng latLng) throws Exception {
        Address retVal;

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!Validator.isValid(addresses)) {
                retVal = null;
            } else {
                android.location.Address address = addresses.get(0);
                ArrayList<String> addressList = new ArrayList<>();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressList.add(address.getAddressLine(i));
                }

                String addressLine = StringUtil.collectionToCSV(addressList);
                String line1 = address.getThoroughfare() + ", " + address.getSubThoroughfare();
                String line2 = address.getSubLocality();
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryCode();
                String pinCode = address.getPostalCode();

                retVal = new Address();
                retVal.setAddressLine(addressLine);
                retVal.setLine1(line1);
                retVal.setLine2(line2);
                retVal.setCity(city);
                retVal.setState(state);
                retVal.setCountry(country);
                retVal.setPinCode(pinCode);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return retVal;
    }

    public static LatLng getLatLngFromAddress(@NonNull Context context, @NonNull String address) throws Exception {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!Validator.isValid(addresses)) {
                return null;
            } else {
                android.location.Address tempAddress = addresses.get(0);
                return new LatLng(tempAddress.getLatitude(), tempAddress.getLongitude());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
