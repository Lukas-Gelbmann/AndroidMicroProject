package at.fhooe.mc.android.AndroidMicroProject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //create a notificationmanager
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //create a intent for pending intent
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        for (int i = 1; i < 999; i++) {
            //create pendingintent for notification
            PendingIntent pendingI = PendingIntent.getActivity(context, i, notificationIntent, 0);

            //create notificationchannel in notificationmanager
            NotificationChannel channel = new NotificationChannel("default", "Birthday Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Birthday Notification");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }

            //build notification
            NotificationCompat.Builder b = new NotificationCompat.Builder(context, "default");
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.birthdaycake)
                    .setContentTitle(context.getString(R.string.alarmReceiver_Bday))
                    .setContentText(context.getString(R.string.alarmReceiver_birthday))
                    .setContentIntent(pendingI);
            //get notified
            if (nm != null) {
                nm.notify(0, b.build());
            }
        }
    }
}