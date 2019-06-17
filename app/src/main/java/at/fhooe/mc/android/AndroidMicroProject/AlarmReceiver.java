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
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        for (int i = 1; i < 999; i++) {
            PendingIntent pendingI = PendingIntent.getActivity(context, i, notificationIntent, 0);
            NotificationChannel channel = new NotificationChannel("default", "Birthday Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Birthday Notification");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
            NotificationCompat.Builder b = new NotificationCompat.Builder(context, "default");
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.birthdaycake)
                    .setContentTitle("Birthday")
                    .setContentText("A friend of yours has birthday today!")
                    .setContentIntent(pendingI);
            if (nm != null) {
                nm.notify(0, b.build());
            }
        }
    }
}