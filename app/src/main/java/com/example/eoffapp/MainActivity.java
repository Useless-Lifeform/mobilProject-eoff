package com.example.eoffapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.NavigableMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    NotificationHandler notiHandler;
    AlarmManager aManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //createNotificationChannel(); scheduleReminder();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Log.d("MainActivity", "User is null - finish");
            finish();
        }

        notiHandler = new NotificationHandler(this);
        aManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setAlarmM();
    }//end of onCreate
//--navigació---
    public void openProfil(View view) {
        Intent csinald = new Intent(this, ProfilActivity.class);
        notiHandler.send("ezd figyeld!");
        startActivity(csinald);
    }
    public void openMeterReading(View view) {
        Intent mert = new Intent(this, MeterReading.class);
        startActivity(mert);
    }
    public void openMeterRecords(View view) {
        Intent megverlek = new Intent(this, OldRedings.class);
        startActivity(megverlek);//TODO:
    }


    public void setAlarmM(){
        long repeateTime = 1000*60*4 ;// ms-ben számol, x1000 =1s x60 =1m    kb 4perc
        long triggerTime = SystemClock.elapsedRealtime() + repeateTime;
        Intent intent = new Intent(this, NotifyBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        aManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,repeateTime,pendingIntent);
    }/// 8vid  kb 53


}
/*
    {//NOT IN USE
private void scheduleReminder() {
    Calendar calendar = Calendar.getInstance();
    //calendar.set(Calendar.DAY_OF_MONTH, 6);
    //calendar.set(Calendar.HOUR_OF_DAY, 20);
    calendar.set(Calendar.MINUTE, 43);
    calendar.set(Calendar.SECOND, 0);

    // Ha a dátum már elmúlt ebben a hónapban, a következő hónapra állítjuk
    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
        calendar.add(Calendar.MONTH, 1);
    }

    Intent intent = new Intent(this, AlarmReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    notiHandler.send("ezaz üzenet");
}

private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence name = "Villanyóra emlékeztető";
        String description = "Havi bejelentési emlékeztető";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("villanyora_channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
    }*/