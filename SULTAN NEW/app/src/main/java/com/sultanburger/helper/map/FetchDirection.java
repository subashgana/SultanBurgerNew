package com.sultanburger.helper.map;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.sultanburger.data.Direction;
import com.sultanburger.data.DirectionStep;
import com.sultanburger.data.TravelMode;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchDirection extends AsyncTask<Void, Void, List<Direction>> {

    private static final String TAG = FetchDirection.class.getSimpleName();

    private LatLng latLngOrigin;
    private LatLng latLngDestination;
    private TravelMode travelMode;

    public FetchDirection(LatLng latLngOrigin, LatLng latLngDestination, TravelMode travelMode) {
        this.latLngOrigin = latLngOrigin;
        this.latLngDestination = latLngDestination;
        this.travelMode = travelMode;
    }

    @Override
    protected List<Direction> doInBackground(Void... data) {
        List<Direction> retVal = null;

        try {
            URL url = new URL(getDirectionsUrl(latLngOrigin, latLngDestination, travelMode));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            int statusCode = httpURLConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                String response = convertInputStreamToString(inputStream);

                if (Validator.isValid(response))
                    retVal = parseResultData(response);
            }

            httpURLConnection.disconnect();
        } catch (Exception e) {
            Logger.writeLog(TAG, "FetchDirection -> " + e.getLocalizedMessage());
        }

        return retVal;
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination, TravelMode travelMode) {
        String _origin = "origin=" + origin.latitude + "," + origin.longitude;
        String _destination = "destination=" + destination.latitude + "," + destination.longitude;
        String _travelMode = "mode=" + travelMode.toString();    // driving, walking, bicycling, transit
        String _avoid = "avoid=";  // tolls, highways, ferries

        String parameters = _origin + "&" + _destination + "&" + _travelMode + "&" + _avoid;
        String outputType = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + outputType + "?" + parameters;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
        StringBuffer stringBuffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append('\r');
        }

        bufferedReader.close();

        if (inputStream != null)
            inputStream.close();

        return String.valueOf(stringBuffer);
    }

    private List<Direction> parseResultData(String data) throws Exception {
        List<Direction> directions = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            String status = jsonObject.has("status") ? jsonObject.getString("status") : "";
            if (status.equalsIgnoreCase("OK")) {
                JSONArray jsonArrayRoutes = jsonObject.has("routes") ? jsonObject.getJSONArray("routes") : null;
                if (Validator.isValid(jsonArrayRoutes)) {
                    for (int r = 0; r < jsonArrayRoutes.length(); r++) {
                        Direction direction = null;

                        JSONObject jsonObjectRouteData = jsonArrayRoutes.getJSONObject(r);

                        JSONArray jsonArrayLegs = jsonObjectRouteData.has("legs") ? jsonObjectRouteData.getJSONArray("legs") : null;
                        if (Validator.isValid(jsonArrayLegs)) {
                            for (int l = 0; l < jsonArrayLegs.length(); l++) {
                                JSONObject jsonObjectLegsData = jsonArrayLegs.getJSONObject(l);

                                String totalDistance = "";
                                String totalDuration = "";
                                LatLng originLocation = null;
                                String originAddress;
                                LatLng destinationLocation = null;
                                String destinationAddress;
                                ArrayList<DirectionStep> directionSteps = new ArrayList<>();

                                JSONObject jsonObjectDistance = jsonObjectLegsData.has("distance") ? jsonObjectLegsData.getJSONObject("distance") : null;
                                if (Validator.isValid(jsonObjectDistance)) {
                                    totalDistance = jsonObjectDistance.has("text") ? jsonObjectDistance.getString("text") : "";
                                }

                                JSONObject jsonObjectDuration = jsonObjectLegsData.has("duration") ? jsonObjectLegsData.getJSONObject("duration") : null;
                                if (Validator.isValid(jsonObjectDuration)) {
                                    totalDuration = jsonObjectDuration.has("text") ? jsonObjectDuration.getString("text") : "";
                                }

                                JSONObject jsonObjectOriginLocation = jsonObjectLegsData.has("start_location") ? jsonObjectLegsData.getJSONObject("start_location") : null;
                                if (Validator.isValid(jsonObjectOriginLocation)) {
                                    String latitude = jsonObjectOriginLocation.has("lat") ? jsonObjectOriginLocation.getString("lat") : "";
                                    String longitude = jsonObjectOriginLocation.has("lng") ? jsonObjectOriginLocation.getString("lng") : "";

                                    originLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                }

                                originAddress = jsonObjectLegsData.has("start_address") ? jsonObjectLegsData.getString("start_address") : "";

                                JSONObject jsonObjectDestinationLocation = jsonObjectLegsData.has("end_location") ? jsonObjectLegsData.getJSONObject("end_location") : null;
                                if (Validator.isValid(jsonObjectDestinationLocation)) {
                                    String latitude = jsonObjectDestinationLocation.has("lat") ? jsonObjectDestinationLocation.getString("lat") : "";
                                    String longitude = jsonObjectDestinationLocation.has("lng") ? jsonObjectDestinationLocation.getString("lng") : "";

                                    destinationLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                }

                                destinationAddress = jsonObjectLegsData.has("end_address") ? jsonObjectLegsData.getString("end_address") : "";

                                JSONArray jsonArraySteps = jsonObjectLegsData.has("steps") ? jsonObjectLegsData.getJSONArray("steps") : null;
                                if (Validator.isValid(jsonArraySteps)) {
                                    for (int s = 0; s < jsonArraySteps.length(); s++) {
                                        JSONObject jsonObjectStepsData = jsonArraySteps.getJSONObject(s);

                                        String _distance = "";
                                        String _duration = "";
                                        LatLng _destinationLocation = null;
                                        String _instruction;
                                        String _maneuver;
                                        String _travelMode;
                                        String _polylinePoints = "";

                                        JSONObject _jsonObjectDistance = jsonObjectStepsData.has("distance") ? jsonObjectStepsData.getJSONObject("distance") : null;
                                        if (Validator.isValid(_jsonObjectDistance)) {
                                            _distance = _jsonObjectDistance.has("text") ? _jsonObjectDistance.getString("text") : "";
                                        }

                                        JSONObject _jsonObjectDuration = jsonObjectStepsData.has("duration") ? jsonObjectStepsData.getJSONObject("duration") : null;
                                        if (Validator.isValid(_jsonObjectDuration)) {
                                            _duration = _jsonObjectDuration.has("text") ? _jsonObjectDuration.getString("text") : "";
                                        }

                                        JSONObject _jsonObjectDestinationLocation = jsonObjectStepsData.has("end_location") ? jsonObjectStepsData.getJSONObject("end_location") : null;
                                        if (Validator.isValid(_jsonObjectDestinationLocation)) {
                                            String latitude = _jsonObjectDestinationLocation.has("lat") ? _jsonObjectDestinationLocation.getString("lat") : "";
                                            String longitude = _jsonObjectDestinationLocation.has("lng") ? _jsonObjectDestinationLocation.getString("lng") : "";

                                            _destinationLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                        }

                                        _instruction = jsonObjectStepsData.has("html_instructions") ? jsonObjectStepsData.getString("html_instructions") : "";

                                        _maneuver = jsonObjectStepsData.has("maneuver") ? jsonObjectStepsData.getString("maneuver") : "";

                                        _travelMode = jsonObjectStepsData.has("travel_mode") ? jsonObjectStepsData.getString("travel_mode") : "";

                                        JSONObject _jsonObjectPolylinePoints = jsonObjectStepsData.has("polyline") ? jsonObjectStepsData.getJSONObject("polyline") : null;
                                        if (Validator.isValid(_jsonObjectPolylinePoints)) {
                                            _polylinePoints = _jsonObjectPolylinePoints.has("points") ? _jsonObjectPolylinePoints.getString("points") : "";
                                        }

                                        DirectionStep directionStep = new DirectionStep();
                                        directionStep.setDistance(_distance);
                                        directionStep.setDuration(_duration);
                                        directionStep.setDestinationLocation(_destinationLocation);
                                        directionStep.setInstruction(_instruction);
                                        directionStep.setManeuver(_maneuver);
                                        directionStep.setTravelMode(_travelMode);
                                        directionStep.setPolylinePoints(decodePolyline(_polylinePoints));
                                        directionSteps.add(directionStep);
                                    }
                                }

                                direction = new Direction();
                                direction.setTotalDistance(totalDistance);
                                direction.setTotalDuration(totalDuration);
                                direction.setOriginLocation(originLocation);
                                direction.setOriginAddress(originAddress);
                                direction.setDestinationLocation(destinationLocation);
                                direction.setDestinationAddress(destinationAddress);
                                direction.setDirectionSteps(directionSteps);
                            }
                        }

                        String polylineOverview = jsonObjectRouteData.has("overview_polyline") ? jsonObjectRouteData.getString("overview_polyline") : "";

                        direction.setPolylineOverview(polylineOverview);
                        directions.add(direction);
                    }
                }
            }
        } catch (JSONException e) {
            throw new Exception(e);
        }

        return directions;
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> latLngs = new ArrayList<>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            latLngs.add(p);
        }

        return latLngs;
    }
}
