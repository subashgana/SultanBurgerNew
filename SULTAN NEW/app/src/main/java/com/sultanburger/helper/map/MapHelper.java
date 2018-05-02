package com.sultanburger.helper.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sultanburger.R;
import com.sultanburger.data.Direction;
import com.sultanburger.data.DirectionStep;
import com.sultanburger.data.Location;
import com.sultanburger.data.MapMarker;
import com.sultanburger.data.TravelMode;
import com.sultanburger.utils.DisplayUtil;
import com.sultanburger.utils.DrawableUtil;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapHelper {

    private static final String TAG = MapHelper.class.getSimpleName();

    private static MapHelper mapHelper;

    private Context context;

    private MapHelper(Context context) {
        this.context = context;
    }

    public static MapHelper init(@NonNull Context context) {
        if (!Validator.isValid(mapHelper))
            mapHelper = new MapHelper(context);

        return mapHelper;
    }

    public MarkerOptions getSelfMarkerDetails(@NonNull Context context, @NonNull MapMarker mapMarker, boolean canMarkerDraggable) throws Exception {
        Bitmap markerBitmapFromView = getBitmapFromView(context, R.drawable.ic_navigator);
        if (Validator.isValid(markerBitmapFromView)) {
            Location location = mapMarker.getLocation();
            return new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mapMarker.getTitle()).snippet(mapMarker.getSnippet()).icon(BitmapDescriptorFactory.fromBitmap(markerBitmapFromView)).draggable(canMarkerDraggable).anchor(0.5f, 0.5f).rotation(location.getBearings()).flat(true);
        } else {
            Logger.writeLog(TAG, "getSelfMarkerDetails -> Invalid marker icon");
            return null;
        }
    }

    public MarkerOptions getMarkerDetails(@NonNull Context context, @NonNull MapMarker mapMarker, boolean canMarkerDraggable) {
        Location location = mapMarker.getLocation();
        return new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mapMarker.getTitle()).snippet(mapMarker.getSnippet()).draggable(canMarkerDraggable).anchor(0.5f, 0.5f).rotation(location.getBearings()).flat(true);
    }

    public MarkerOptions getMarkerDetails(@NonNull Context context, @NonNull MapMarker mapMarker, boolean canMarkerDraggable, @NonNull Bitmap bitmap) {
        Location location = mapMarker.getLocation();
        return new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mapMarker.getTitle()).snippet(mapMarker.getSnippet()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).draggable(canMarkerDraggable).anchor(0.5f, 0.5f).rotation(location.getBearings()).flat(true);
    }

    public MarkerOptions getMarkerDetails(@NonNull Context context, @NonNull MapMarker mapMarker, boolean canMarkerDraggable, @DrawableRes int markerIcon) throws Exception {
        Bitmap markerBitmapFromView = getBitmapFromView(context, markerIcon);
        if (Validator.isValid(markerBitmapFromView)) {
            Location location = mapMarker.getLocation();
            return new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(mapMarker.getTitle()).snippet(mapMarker.getSnippet()).icon(BitmapDescriptorFactory.fromBitmap(markerBitmapFromView)).draggable(canMarkerDraggable).anchor(0.5f, 0.5f).rotation(location.getBearings()).flat(true);
        } else {
            Logger.writeLog(TAG, "getMarkerDetails -> Invalid marker icon");
            return getMarkerDetails(context, mapMarker, canMarkerDraggable);
        }
    }

    public void clearMarker(@NonNull Marker marker) {
        if (Validator.isValid(marker))
            marker.remove();
    }

    public void animateCamera(@NonNull GoogleMap googleMap, @NonNull Location location, int zoomLevel, int tiltAngle) {
        if (Validator.isValid(googleMap) && Validator.isValid(location)) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).tilt(tiltAngle).zoom(zoomLevel).bearing(location.getBearings()).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
        }
    }

    public void moveMarker(@NonNull final GoogleMap googleMap, @NonNull final Marker marker, @NonNull final Location location) {
        if (Validator.isValid(googleMap) && Validator.isValid(marker) && Validator.isValid(location)) {
            final boolean hideMarker = false;
            final long start = SystemClock.uptimeMillis();
            final long duration = 1000;

            Projection projection = googleMap.getProjection();
            Point startPoint = projection.toScreenLocation(marker.getPosition());
            final LatLng startLatLng = projection.fromScreenLocation(startPoint);

            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        long elapsed = SystemClock.uptimeMillis() - start;
                        float t = new LinearInterpolator().getInterpolation((float) elapsed / duration);
                        double longitude = t * location.getLongitude() + (1 - t) * startLatLng.longitude;
                        double latitude = t * location.getLatitude() + (1 - t) * startLatLng.latitude;

                        marker.setPosition(new LatLng(latitude, longitude));

                        if (t < 1.0) {
                            // Post again 16ms later.
                            handler.postDelayed(this, 16);
                        } else {
                            if (hideMarker)
                                marker.setVisible(false);
                            else
                                marker.setVisible(true);
                        }
                    } catch (NullPointerException e) {
                        Logger.writeLog(TAG, "moveMarker -> NullPointerException : " + e.getLocalizedMessage());
                    }
                }
            });
        }
    }

    public void moveMarkerAndAnimateCamera(@NonNull GoogleMap googleMap, @NonNull Marker marker, @NonNull Location location, int zoomLevel, int tiltAngle) {
        moveMarker(googleMap, marker, location);
        animateCamera(googleMap, location, zoomLevel, tiltAngle);
    }

    public void setZoomLevel(@NonNull GoogleMap googleMap, @NonNull List<Marker> markerList, int padding) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < markerList.size(); i++) {
            builder.include(markerList.get(i).getPosition());
        }

        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    public Bitmap getBitmapFromView(@NonNull Context context, @DrawableRes int resId) throws Exception {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_map_marker, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.custom_map_marker_imageView);
            imageView.setImageResource(resId);

            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();

            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

            Drawable drawable = view.getBackground();
            if (Validator.isValid(drawable))
                drawable.draw(canvas);

            view.draw(canvas);

            return bitmap;
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    public Bitmap getBitmapFromView(@NonNull Context context, @NonNull Bitmap imageBitmap) throws Exception {
        try {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_map_marker, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.custom_map_marker_imageView);
            imageView.setImageBitmap(imageBitmap);

            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();

            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

            Drawable drawable = view.getBackground();
            if (Validator.isValid(drawable))
                drawable.draw(canvas);

            view.draw(canvas);

            return bitmap;
        } catch (NullPointerException e) {
            throw new Exception(e);
        }
    }

    public List<Direction> getDirection(@NonNull LatLng origin, @NonNull LatLng destination, @NonNull TravelMode travelMode) throws Exception {
        List<Direction> directions;

        try {
            directions = new FetchDirection(origin, destination, travelMode).execute().get();
        } catch (InterruptedException e) {
            throw new Exception(e);
        } catch (ExecutionException e) {
            throw new Exception(e);
        }

        return directions;
    }

    public List<Polyline> drawDirection(@NonNull Context context, @NonNull GoogleMap googleMap, @NonNull List<Direction> directions) {
        List<Polyline> polylineList = new ArrayList<>();

        for (int i = 0; i < directions.size(); i++) {
            Direction direction = directions.get(i);
            if (Validator.isValid(direction)) {
                List<DirectionStep> directionSteps = direction.getDirectionSteps();

                for (int s = 0; s < directionSteps.size(); s++) {
                    DirectionStep directionStep = directionSteps.get(s);
                    if (Validator.isValid(directionStep)) {
                        List<LatLng> polylinePoints = directionStep.getPolylinePoints();

                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.width(DisplayUtil.dpToPx(5));
                        polylineOptions.color(DrawableUtil.getColor(context, R.color.dark_blue));
                        polylineOptions.geodesic(true);
                        polylineOptions.clickable(false);
                        polylineOptions.addAll(polylinePoints);

                        Polyline polyline = googleMap.addPolyline(polylineOptions);
                        polylineList.add(polyline);
                    }
                }
            }
        }

        return polylineList;
    }
}
