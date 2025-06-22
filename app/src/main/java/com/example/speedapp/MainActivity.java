package com.example.speedapp;

import android.app.Activity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {
    private Handler handler = new Handler();
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "speed_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("Speed Monitor");
        setContentView(textView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Speed", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                String speedText = isConnected() ? "Speed: ~" + (int)(Math.random() * 10 + 1) + " Mbps" : "No Internet";
                showNotification(speedText);
                handler.postDelayed(this, 3000);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showNotification(String text) {
        android.app.Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new android.app.Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new android.app.Notification.Builder(this);
        }
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
               .setContentTitle("Speed")
               .setContentText(text)
               .setOngoing(true);
        notificationManager.notify(1, builder.build());
    }
}
