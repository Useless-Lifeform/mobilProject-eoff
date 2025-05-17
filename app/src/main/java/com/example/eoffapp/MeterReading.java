package com.example.eoffapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eoffapp.models.Meres;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MeterReading extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore db; CollectionReference meroorak;
    Spinner merokSpinner;
    EditText meroAllasET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meter_reading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser(); if(user == null){finish();}
        db = FirebaseFirestore.getInstance(); meroorak = db.collection("users").document(user.getUid()).collection("meroorak");

        merokSpinner = findViewById(R.id.merokSpinner);
        meroAllasET = findViewById(R.id.meroAllasET);

        List<String> list = new ArrayList<>();
        {
            meroorak.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this ,android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    merokSpinner.setAdapter(adapter);
                } else {//TODO
                   // Log.e("Firestore", "Hiba a lekérdezés során: ", task.getException());
                }

            });
        }


    }///END of onCreate()///
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnectedToInternet()) {
                SharedPreferences prefs = getSharedPreferences("offline_data", MODE_PRIVATE);
                String merosz = prefs.getString("pending_meroszam", null);
                String allas = prefs.getString("pending_allas", null);

                if (merosz != null && allas != null) {
                    int ertek = Integer.parseInt(allas);
                    uploadData(merosz, ertek);
                    Toast.makeText(context, "Automatikus feltöltés újra csatlakozáskor", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



    {/*
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    */}

    public void meresFeltoltes(View view) {
        String meroszam = merokSpinner.getSelectedItem().toString();
        int allas = Integer.parseInt(meroAllasET.getText().toString());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String dateAsID = LocalDateTime.now().format(formatter);


            if (isConnectedToInternet()) {
                uploadData(meroszam, allas);
            } else {
                saveToLocalStorage(dateAsID, meroszam, allas);
                Toast.makeText(this, "Nincs internet. Adat elmentve későbbre.", Toast.LENGTH_SHORT).show();
            }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    private void saveToLocalStorage(String id, String meroszam, int allas) {
        SharedPreferences prefs = getSharedPreferences("offline_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pending_meroszam", meroszam);
        editor.putString("pending_allas", Integer.toString(allas) );
        editor.apply();
    }
    private void clearLocalDataIfAny() {
        SharedPreferences prefs = getSharedPreferences("offline_data", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private void uploadData(String meroszam, int allas) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        meroorak.document(meroszam).collection("meresek").document(formattedDateTime)
                .set(  new Meres(formattedDateTime,allas) )

                .addOnSuccessListener(documentReference -> {
                    Log.d("Upload", "Sikeres feltöltés: ");// + documentReference.getId());
                    clearLocalDataIfAny();
                })
                .addOnFailureListener(e -> Log.e("Upload", "Hiba a feltöltéskor", e));
    }


}