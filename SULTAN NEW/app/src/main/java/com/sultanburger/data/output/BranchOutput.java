package com.sultanburger.data.output;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BranchOutput implements Parcelable {

    @Expose
    @SerializedName("branch_id")
    private String branchId;

    @Expose
    @SerializedName("branch_name")
    private String branchName;

    @Expose
    @SerializedName("latitude")
    private String latitude;

    @Expose
    @SerializedName("longitude")
    private String longitude;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("branch_open")
    private String branchOpen;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    @Expose
    @SerializedName("distance")
    private String distance;

    public BranchOutput() {

    }

    protected BranchOutput(Parcel in) {
        branchId = in.readString();
        branchName = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        address = in.readString();
        branchOpen = in.readString();
        phoneNumber = in.readString();
        distance = in.readString();
    }

    public static final Creator<BranchOutput> CREATOR = new Creator<BranchOutput>() {
        @Override
        public BranchOutput createFromParcel(Parcel in) {
            return new BranchOutput(in);
        }

        @Override
        public BranchOutput[] newArray(int size) {
            return new BranchOutput[size];
        }
    };

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBranchOpen() {
        return branchOpen;
    }

    public void setBranchOpen(String branchOpen) {
        this.branchOpen = branchOpen;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(branchId);
        parcel.writeString(branchName);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(address);
        parcel.writeString(branchOpen);
        parcel.writeString(phoneNumber);
        parcel.writeString(distance);
    }
}
