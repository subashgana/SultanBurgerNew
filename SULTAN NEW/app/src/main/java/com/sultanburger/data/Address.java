package com.sultanburger.data;

import java.io.Serializable;

public class Address implements Serializable {

    private String addressLine;
    private String line1;       // ThroughFare, SubThroughFare
    private String line2;       // SubLocality
    private String city;        // Locality
    private String state;       // AdminArea
    private String country;     // CountryName
    private String pinCode;     // PostCode

    public String getAddressLine() {
        return addressLine.replace("\n", ", ");
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
