package com.example.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("description"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Log.w("NotificationBroadcast", intent.getStringExtra("title"));
        Log.w("NotificationBroadcast", intent.getStringExtra("description"));
        int tempKey = intent.getIntExtra("key", 1);
        Log.w("NotificationBroadcast", Integer.toString(tempKey));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(tempKey, builder.build());
    }
}
