package com.sultanburger.data.item;

public class ListDataItem {

    private String id;
    private String data;

    public static ListDataItem from(String id, String data) {
        ListDataItem retVal = new ListDataItem();
        retVal.id = id;
        retVal.data = data;

        return retVal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
