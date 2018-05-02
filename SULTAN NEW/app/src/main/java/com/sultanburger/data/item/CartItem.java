package com.sultanburger.data.item;

import com.sultanburger.data.output.AddOnsCategory;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.data.output.ModifiersCategory;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {

    private ListMenu listMenu;
    private List<ModifiersCategory> modifiersCategories;
    private List<AddOnsCategory> addOnsCategories;

    public ListMenu getListMenu() {
        return listMenu;
    }

    public void setListMenu(ListMenu listMenu) {
        this.listMenu = listMenu;
    }

    public List<ModifiersCategory> getModifiersCategories() {
        return modifiersCategories;
    }

    public void setModifiersCategories(List<ModifiersCategory> modifiersCategories) {
        this.modifiersCategories = modifiersCategories;
    }

    public List<AddOnsCategory> getAddOnsCategories() {
        return addOnsCategories;
    }

    public void setAddOnsCategories(List<AddOnsCategory> addOnsCategories) {
        this.addOnsCategories = addOnsCategories;
    }
}
