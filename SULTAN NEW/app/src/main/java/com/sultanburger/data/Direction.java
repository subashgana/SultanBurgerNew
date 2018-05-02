package com.sultanburger.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Direction {

    private String totalDistance;
    private String totalDuration;
    private LatLng originLocation;
    private String originAddress;
    private LatLng destinationLocation;
    private String destinationAddress;
    private ArrayList<DirectionStep> directionSteps;
    private String polylineOverview;

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public LatLng getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(LatLng originLocation) {
        this.originLocation = originLocation;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public ArrayList<DirectionStep> getDirectionSteps() {
        return directionSteps;
    }

    public void setDirectionSteps(ArrayList<DirectionStep> directionSteps) {
        this.directionSteps = directionSteps;
    }

    public String getPolylineOverview() {
        return polylineOverview;
    }

    public void setPolylineOverview(String polylineOverview) {
        this.polylineOverview = polylineOverview;
    }
}
