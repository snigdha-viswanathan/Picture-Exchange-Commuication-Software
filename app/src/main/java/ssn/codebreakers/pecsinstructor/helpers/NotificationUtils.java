package ssn.codebreakers.pecsinstructor.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.UUID;

import ssn.codebreakers.pecsinstructor.HomeScreenActivity;
import ssn.codebreakers.pecsinstructor.NewHomeScreen;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils
{
    private static final String PREFERENCE_LAST_NOTIF_ID = "PREFERENCE_LAST_NOTIF_ID";
    private static final String NOTIFICATION_CHANNEL_ID = "pecs_nchannel";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "pecs notification";

    private static int getNextNotifId(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = sharedPreferences.getInt(PREFERENCE_LAST_NOTIF_ID, 0) + 1;
        if (id == Integer.MAX_VALUE) { id = 0; }
        sharedPreferences.edit().putInt(PREFERENCE_LAST_NOTIF_ID, id).apply();
        return id;
    }

    public static int showNotification(Context context, String text, Intent intent)
    {
        PendingIntent pIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder notificationbuilder= new Notification.Builder(context)
                .setContentTitle("PECS")
                .setContentText(text)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentIntent(pIntent)
                .setColor(Color.rgb(18,164,221))
                .setAutoCancel(true)
                .addAction(android.R.mipmap.sym_def_app_icon, "View", pIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);

            if (notificationChannel == null)
            {
                System.out.println("notificationChannel is null");
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_DESCRIPTION, importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationbuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }
        Notification notification = notificationbuilder.build();
        int notificationId = NotificationUtils.getNextNotifId(context);
        notificationManager.notify(notificationId, notification);
        return notificationId;
    }

    public static int showNotification(Context context, String text)
    {
        Intent intent = new Intent(context, NewHomeScreen.class);
        return showNotification(context, text, intent);
    }
}
