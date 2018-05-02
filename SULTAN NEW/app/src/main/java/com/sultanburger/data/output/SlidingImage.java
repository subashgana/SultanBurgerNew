package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlidingImage {

    @Expose
    @SerializedName("slider_id")
    private String sliderId;

    @Expose
    @SerializedName("image_url")
    private String imageUrl;

    public SlidingImage() {

    }

    public String getSliderId() {
        return sliderId;
    }

    public void setSliderId(String sliderId) {
        this.sliderId = sliderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
