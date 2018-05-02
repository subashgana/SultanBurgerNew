package com.sultanburger.data;

public enum TravelMode {
    DRIVING("driving"),
    WALKING("walking"),
    BICYCLING("bicycling"),
    TRANSIT("transit");

    private String travelMode;

    TravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    @Override
    public String toString() {
        return travelMode;
    }
}
