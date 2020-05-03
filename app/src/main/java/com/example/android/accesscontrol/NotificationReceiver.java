package com.example.android.accesscontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String TOAST_MESSAGE = "toastMessage";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(TOAST_MESSAGE);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
