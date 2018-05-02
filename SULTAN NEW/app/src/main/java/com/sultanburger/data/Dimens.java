package com.sultanburger.data;

public class Dimens {

    private int width;
    private int height;

    public static Dimens from(int width, int height) {
        Dimens retVal = new Dimens();
        retVal.width = width;
        retVal.height = height;

        return retVal;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
