package com.example.eoffapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    NotificationManager manager;
    Context contx; String CHANEL_ID = "eoff_channel";
    public NotificationHandler(Context context){
        this.contx = context;
        manager = (NotificationManager) contx.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }
/*különböző channeleket lehet létrehozni, többet is akár
* beállítások, vibráció, led, leirás a beállításokban.....*/
    private void createChannel(){
        Log.d("DEBUG", " NotificationHandler  createChannel() running");
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;// cak adottt verzió felett

        NotificationChannel channel = new NotificationChannel(CHANEL_ID, "eOff Notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("eOff notification description");
        this.manager.createNotificationChannel(channel);
    }
    public void send(String message){
/*////*/Log.d("DEBUG", " NotificationHandler  send() running");
        Intent intent =new Intent(contx, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(contx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contx, CHANEL_ID)
                .setContentTitle("eOff")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentIntent(pendingIntent);
        this.manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
