package com.example.eoffapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyBroadcast extends BroadcastReceiver {//aka AlarmReceiver

    @Override
    public void onReceive(Context context, Intent intent) {
        // ébresztőóra indítás, adatok lekéérése, > amiket különböző időnként akarjuk végrehajtani
        NotificationHandler notiHandler = new NotificationHandler(context);
        notiHandler.send("Itt az idő!!!");

        //ez történik meg mikor Alarm aktiválódik
    }
}