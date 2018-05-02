package com.sultanburger.rest;

public class HeaderParam extends Param {

    private static final String TAG = HeaderParam.class.getSimpleName();

    public static HeaderParam from(String name, String value) {
        HeaderParam retVal = new HeaderParam();
        retVal.setName(name);
        retVal.setValue(value);
        return retVal;
    }
}
