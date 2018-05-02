package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlidingImagesOutput {

    @Expose
    @SerializedName("image_base_url")
    private String imageBaseUrl;

    @Expose
    @SerializedName("slider_images")
    private List<SlidingImage> sliderImages;

    public SlidingImagesOutput() {

    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public List<SlidingImage> getSliderImages() {
        return sliderImages;
    }

    public void setSliderImages(List<SlidingImage> sliderImages) {
        this.sliderImages = sliderImages;
    }
}
