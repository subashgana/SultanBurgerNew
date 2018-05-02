package com.sultanburger.rest;

public class QueryParam extends Param {

    private static final String TAG = QueryParam.class.getSimpleName();

    public static QueryParam from(String name, String value) {
        QueryParam retVal = new QueryParam();
        retVal.setName(name);
        retVal.setValue(value);
        return retVal;
    }
}
