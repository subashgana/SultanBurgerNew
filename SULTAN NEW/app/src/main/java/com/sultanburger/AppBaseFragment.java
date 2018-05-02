package com.sultanburger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sultanburger.data.Location;
import com.sultanburger.data.MapMarker;
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.map.MapHelper;
import com.sultanburger.helper.permission.PermissionFragment;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class AppBaseFragment extends PermissionFragment implements AppConstants {

    private static final String TAG = AppBaseFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public AppBaseActivity getAppBaseActivity() {
        return ((AppBaseActivity) getActivity());
    }

    public void showProgressBar(String message) {
        getAppBaseActivity().showProgressBar(message);
    }

    public void dismissProgressBar() {
        getAppBaseActivity().dismissProgressBar();
    }

    public MapHelper getMapHelper() {
        return getAppBaseActivity().getMapHelper();
    }

    public void getLastKnownLocation(final DataReceiver<Location> dataReceiver) {
        getAppBaseActivity().getLastKnownLocation(dataReceiver);
    }

    protected void setMapUiSettings(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    protected void focusLocation(final GoogleMap googleMap, final Location location, boolean markLocation) {
        if (Validator.isValid(location)) {
            final MapHelper mapHelper = getMapHelper();

            if (markLocation) {
                ImageLoader imageLoader = ImageUtil.getImageLoader(getContext());
                imageLoader.loadImage(AVATAR, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        addMarkerWithAvatar(googleMap, location, null);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        addMarkerWithAvatar(googleMap, location, loadedImage);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        addMarkerWithAvatar(googleMap, location, null);
                    }
                });
            } else {
                mapHelper.animateCamera(googleMap, location, ZOOM_LEVEL_NORMAL, TILT_ANGLE);
            }
        }
    }

    protected void addMarkerWithAvatar(GoogleMap googleMap, Location location, Bitmap loadedBitmap) {
        try {
            MapHelper mapHelper = getMapHelper();

            if (!Validator.isValid(loadedBitmap))
                loadedBitmap = DrawableUtil.getBitmap(DrawableUtil.getDrawable(getContext(), R.drawable.ic_user));

            loadedBitmap = Bitmap.createScaledBitmap(loadedBitmap, 72, 72, false);
            loadedBitmap = DrawableUtil.getCircularBitmap(loadedBitmap);
            loadedBitmap = mapHelper.getBitmapFromView(getContext(), loadedBitmap);

//            if (Validator.isValid(marker))
//                mapHelper.clearMarker(marker);

            MapMarker mapMarker = new MapMarker();
            mapMarker.setTitle("");
            mapMarker.setSnippet("");
            mapMarker.setLocation(location);

            MarkerOptions markerOptions = mapHelper.getMarkerDetails(getContext(), mapMarker, false, loadedBitmap);
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setRotation(location.getBearings() - 8);

            mapHelper.moveMarkerAndAnimateCamera(googleMap, marker, location, ZOOM_LEVEL_NORMAL, TILT_ANGLE);
        } catch (Exception e) {
            Logger.writeLog(TAG, "addMarkerWithAvatar : " + e.getLocalizedMessage());
        }
    }

    protected List<Marker> addBranchMarker(GoogleMap googleMap, List<BranchOutput> branchOutputs) {
        List<Marker> retVal = new ArrayList<>();
        MapHelper mapHelper = getMapHelper();

        try {
            Bitmap loadedBitmap = DrawableUtil.getBitmap(DrawableUtil.getDrawable(getContext(), R.mipmap.ic_launcher));
            loadedBitmap = Bitmap.createScaledBitmap(loadedBitmap, 72, 72, false);
            loadedBitmap = DrawableUtil.getCircularBitmap(loadedBitmap);
            loadedBitmap = mapHelper.getBitmapFromView(getContext(), loadedBitmap);

            for (BranchOutput branchOutput : branchOutputs) {
                double latitude = Double.parseDouble(branchOutput.getLatitude());
                double longitude = Double.parseDouble(branchOutput.getLongitude());
                Location location = new Location(latitude, longitude, System.currentTimeMillis());

                MapMarker mapMarker = new MapMarker();
                mapMarker.setTitle(branchOutput.getBranchName());
//                mapMarker.setSnippet(branchOutput.getPhoneNumber());
                mapMarker.setLocation(location);

                MarkerOptions markerOptions = mapHelper.getMarkerDetails(getContext(), mapMarker, false, loadedBitmap);
                Marker marker = googleMap.addMarker(markerOptions);
                marker.setRotation(location.getBearings() - 8);

                retVal.add(marker);
            }
        } catch (Exception e) {
            Logger.writeLog(TAG, "addBranchMarker : " + e.getLocalizedMessage());
        }

        mapHelper.setZoomLevel(googleMap, retVal, 15);
        return retVal;
    }
}
