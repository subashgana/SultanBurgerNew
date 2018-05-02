package com.sultanburger.data;

import android.graphics.Bitmap;
import android.net.Uri;

public class Notification {

    private int notificationID;
    private String contentTile;
    private String contentText;
    private String contentInfo;
    private int smallIcon;
    private Bitmap largeIcon;
    private Uri soundUri;
    private boolean isAutoCancel;
    private boolean isOnGoing;

    public Notification(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getContentTile() {
        return contentTile;
    }

    public void setContentTile(String contentTile) {
        this.contentTile = contentTile;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(String contentInfo) {
        this.contentInfo = contentInfo;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    public Uri getSoundUri() {
        return soundUri;
    }

    public void setSoundUri(Uri soundUri) {
        this.soundUri = soundUri;
    }

    public boolean isAutoCancel() {
        return isAutoCancel;
    }

    public void setAutoCancel(boolean autoCancel) {
        isAutoCancel = autoCancel;
    }

    public boolean isOnGoing() {
        return isOnGoing;
    }

    public void setOnGoing(boolean onGoing) {
        isOnGoing = onGoing;
    }
}
