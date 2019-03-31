package com.example.antonio.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Antonio on 2/20/2018.
 */

public class ProximityAlertReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifId = intent.getIntExtra("id", 0);
        if (intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false)) {
            String message = intent.getStringExtra("message" );
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Proxmity Alarm")
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                    .build();

            notificationManager.notify(notifId, notification);
        } else {
            //exited geofence
            notificationManager.cancel(notifId);
        }
    }
}

