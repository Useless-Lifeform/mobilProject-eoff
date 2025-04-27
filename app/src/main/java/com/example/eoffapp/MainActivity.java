package com.example.eoffapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.NavigableMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            finish();
        }
    }
//--navigació---
    public void openProfil(View view) {
        Intent csinald = new Intent(this, ProfilActivity.class);
        startActivity(csinald);
    }
    public void openMeterReading(View view) {
        Intent mert = new Intent(this, MeterReading.class);
        startActivity(mert);
    }
    public void openMeterRecords(View view) {
        /*Intent megverlek = new Intent(this, OldReadings.class);
        startActivity(megverlek);//TODO: itt mást kell hívni!!!*/
    }
}