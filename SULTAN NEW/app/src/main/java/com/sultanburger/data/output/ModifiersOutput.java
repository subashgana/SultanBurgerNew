package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModifiersOutput {

    @Expose
    @SerializedName("modifiers_details")
    private List<ModifiersCategory> modifiersDetails;

    public ModifiersOutput() {

    }

    public List<ModifiersCategory> getModifiersDetails() {
        return modifiersDetails;
    }

    public void setModifiersDetails(List<ModifiersCategory> modifiersDetails) {
        this.modifiersDetails = modifiersDetails;
    }
}
