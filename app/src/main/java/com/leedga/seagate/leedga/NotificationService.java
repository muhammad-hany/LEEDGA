package com.leedga.seagate.leedga;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Muhammad Workstation on 08/11/2016.
 */


public class NotificationService extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("LEED GA");
        builder.setSmallIcon(R.drawable.ic_11_);
        builder.setContentText("You need to study Leed right now");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        Notification notification = builder.build();
        manager.notify(0, notification);

    }


}

