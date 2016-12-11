package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        Intent i = new Intent(context, TestActivity.class);
        i.putExtra(REF.TEST_FRAGMENT_TYPE, REF.SINGLE_QUESTION);
        SharedPreferences preferences = context.getSharedPreferences(REF.GENERAL_SETTING_PREF, Context.MODE_PRIVATE);
        boolean istPremium = preferences.getBoolean(REF.PREMIUM_USER_KEY, false);
        long trialTime = AlarmManager.INTERVAL_DAY * 5;
        long firstLogIn = preferences.getLong(REF.FIRST_LOG_IN_KEY, 0);
        boolean stillInTrial = System.currentTimeMillis() < (firstLogIn + trialTime);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("LEED GA");
        builder.setSmallIcon(R.mipmap.ic_launcher2);
        builder.setContentText("Time for Question of the day");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        if (istPremium || stillInTrial) {
            manager.notify(0, notification);
        }



    }


}

