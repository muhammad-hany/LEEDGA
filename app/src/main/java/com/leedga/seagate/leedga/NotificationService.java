package com.leedga.seagate.leedga;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Muhammad Workstation on 08/11/2016.
 */


public class NotificationService extends BroadcastReceiver {

    long timeToTrigger;

    @Override
    public void onReceive(Context context, Intent intent) {
        timeToTrigger = intent.getLongExtra(REF.TRIGGER_MILLS_KEY, 0);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("LEED GA");
        builder.setSmallIcon(R.drawable.ic_11_);
        builder.setContentText("You need to study Leed right now");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        Notification notification = builder.build();
        manager.notify(0, notification);
        scheduleNextAlarm(context);

    }

    private void scheduleNextAlarm(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        timeToTrigger = timeToTrigger + AlarmManager.INTERVAL_DAY;
        intent.putExtra(REF.TRIGGER_MILLS_KEY, timeToTrigger);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 3333, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToTrigger, AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeToTrigger, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeToTrigger, pendingIntent);
        }
    }
}

