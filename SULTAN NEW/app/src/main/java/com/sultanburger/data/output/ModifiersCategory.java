package com.sultanburger.data.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModifiersCategory implements Serializable {

    @Expose
    @SerializedName("modifier_category_id")
    private String modifierCategoryId;

    @Expose
    @SerializedName("modifier_category_en")
    private String modifierCategoryEn;

    @Expose
    @SerializedName("modifier_category_ar")
    private String modifierCategoryAr;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("modifier_category_name")
    private String modifierCategoryName;

    @Expose
    @SerializedName("modifiers")
    private List<Modifiers> modifiers;

    public ModifiersCategory() {

    }

    public String getModifierCategoryId() {
        return modifierCategoryId;
    }

    public void setModifierCategoryId(String modifierCategoryId) {
        this.modifierCategoryId = modifierCategoryId;
    }

    public String getModifierCategoryEn() {
        return modifierCategoryEn;
    }

    public void setModifierCategoryEn(String modifierCategoryEn) {
        this.modifierCategoryEn = modifierCategoryEn;
    }

    public String getModifierCategoryAr() {
        return modifierCategoryAr;
    }

    public void setModifierCategoryAr(String modifierCategoryAr) {
        this.modifierCategoryAr = modifierCategoryAr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifierCategoryName() {
        return modifierCategoryName;
    }

    public void setModifierCategoryName(String modifierCategoryName) {
        this.modifierCategoryName = modifierCategoryName;
    }

    public List<Modifiers> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifiers> modifiers) {
        this.modifiers = modifiers;
    }
}
