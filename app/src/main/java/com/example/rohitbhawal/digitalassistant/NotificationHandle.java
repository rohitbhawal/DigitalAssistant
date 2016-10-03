package com.example.rohitbhawal.digitalassistant;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Rohit Bhawal on 9/10/2016.
 */
public class NotificationHandle {

    String LOG_TAG = "Notification_Tryout";
    final Context context;
    NotificationManager notificationManager;
    int notifID;
    String numMessages="Alarm";
    boolean isNotificActive=false;
    final String SETGROUP="set_group";


    public NotificationHandle(Context context, int id, String taskName, String taskDesc){
        this.context=context;
        startNotify(id, taskName, taskDesc);
    }
    public void startNotify(int id, String taskName, String taskDesc){
        NotificationCompat.Builder notificBuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(this.context)
                .setContentTitle(taskName).setContentText(taskDesc)
                .setTicker("Alert New Message").setSmallIcon(R.drawable.alarm);
        //Intent moreInfoIntent = new Intent(this.context,GPS_Check.class);
        //TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this.context);
        //PendingIntent pendingIntent =TaskStackBuilder.create(this.context).getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
        //notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.v(LOG_TAG, "Notification id: "+ id);
        notificationManager.notify(numMessages,id,notificBuilder.build());
        isNotificActive =true;
    }
}
