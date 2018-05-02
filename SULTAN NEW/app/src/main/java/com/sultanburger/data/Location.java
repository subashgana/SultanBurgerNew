package com.sultanburger.data;

import java.io.Serializable;

public class Location implements Serializable {

    private double latitude;
    private double longitude;
    private float accuracy;
    private float bearings;
    private float speed;
    private String provider;
    private Address address;
    private long locationUpdatedAt;

    public Location(double latitude, double longitude, long locationUpdatedAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationUpdatedAt = locationUpdatedAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getBearings() {
        return bearings;
    }

    public void setBearings(float bearings) {
        this.bearings = bearings;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getLocationUpdatedAt() {
        return locationUpdatedAt;
    }

    public void setLocationUpdatedAt(long locationUpdatedAt) {
        this.locationUpdatedAt = locationUpdatedAt;
    }
}