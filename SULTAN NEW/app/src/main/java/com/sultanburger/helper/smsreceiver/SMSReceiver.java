package com.sultanburger.helper.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;

import com.sultanburger.AppConstants;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Validator;

public class SMSReceiver extends BroadcastReceiver implements AppConstants {

    private static final String TAG = SMSReceiver.class.getSimpleName();

    private String messageContains;

    public SMSReceiver(String messageContains) {
        this.messageContains = messageContains;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (Validator.isValid(bundle)) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

            for (final SmsMessage message : messages) {
                String from = message.getOriginatingAddress();
                long when = message.getTimestampMillis();
                String msg = message.getMessageBody();

                if (msg.contains(messageContains)) {
                    Intent receivedOTPIntent = new Intent(BROADCAST_OTP_RECEIVER);
                    receivedOTPIntent.putExtra(BROADCAST_OTP_RECEIVER_DATA, msg);
                    context.sendBroadcast(receivedOTPIntent);
                }
            }
        }
    }

    public void register(@NonNull Context context, @NonNull SMSReceiver smsReceiver) {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(999);
        context.registerReceiver(smsReceiver, intentFilter);

        Logger.writeLog(TAG, "registered -> SMS_RECEIVER");
    }

    public void unregister(@NonNull Context context, @NonNull SMSReceiver smsReceiver) throws Exception {
        try {
            context.unregisterReceiver(smsReceiver);
            Logger.writeLog(TAG, "unregistered -> SMS_RECEIVER");
        } catch (IllegalArgumentException e) {
            throw new Exception(e);
        }
    }
}