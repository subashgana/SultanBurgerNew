package com.sultanburger.helper.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.sultanburger.data.Notification;
import com.sultanburger.utils.Validator;

public class NotificationHelper {

    private static final String TAG = NotificationHelper.class.getSimpleName();

    private static NotificationHelper notificationHelper;

    private Context context;

    private NotificationHelper(Context context) {
        this.context = context;
    }

    public static NotificationHelper init(@NonNull Context context) {
        if (!Validator.isValid(notificationHelper))
            notificationHelper = new NotificationHelper(context);

        return notificationHelper;
    }

    public void pushNotification(Notification notification) {
        if (Validator.isValid(notification)) {
            int notificationID = notification.getNotificationID();

            NotificationCompat.Builder notificationBuilder = getNormalNotificationBuilder(notification);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, notificationBuilder.build());
        }
    }

    public void clearNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private NotificationCompat.Builder getNormalNotificationBuilder(Notification notification) {
        String contentTile = notification.getContentTile();
        String contentText = notification.getContentText();
        String contentInfo = notification.getContentInfo();
        int smallIcon = notification.getSmallIcon();
        Bitmap largeIcon = notification.getLargeIcon();
        Uri defaultSoundUri = notification.getSoundUri();
        boolean isAutoCancel = notification.isAutoCancel();
        boolean isOnGoing = notification.isOnGoing();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(isAutoCancel)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setSound(defaultSoundUri)
                .setContentTitle(contentTile)
                .setContentText(contentText)
                .setContentInfo(contentInfo)
                .setOngoing(isOnGoing);

        return notificationBuilder;
    }
}
