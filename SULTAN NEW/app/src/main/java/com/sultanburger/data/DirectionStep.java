package com.sultanburger.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DirectionStep {

    private String distance;
    private String duration;
    private LatLng destinationLocation;
    private String instruction;
    private String maneuver;
    private String travelMode;
    private List<LatLng> polylinePoints;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public List<LatLng> getPolylinePoints() {
        return polylinePoints;
    }

    public void setPolylinePoints(List<LatLng> polylinePoints) {
        this.polylinePoints = polylinePoints;
    }
}
