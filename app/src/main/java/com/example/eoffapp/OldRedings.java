package com.example.eoffapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eoffapp.models.Meres;
import com.example.eoffapp.models.MeroOra;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OldRedings extends AppCompatActivity {

    RecyclerView RecyclerBox;
    ImageView nothingIcon;
    ArrayList<Meres> meresekLista;
    OldReadingsAdapter orAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_old_redings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nothingIcon = findViewById(R.id.nothingIcon);
        RecyclerBox = findViewById(R.id.ReadingBox);
        RecyclerBox.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        meresekLista = new ArrayList<>();
        orAdapter = new OldReadingsAdapter(this, meresekLista);
        RecyclerBox.setAdapter(orAdapter);

        lekerData();
    }
    void lekerData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user == null) {
            finish();
            return;
        }

        String uid = user.getUid();

        // Lekérjük az összes olyan mérőórát, ami ehhez az UID-hez tartozik
        db.collection("readings")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(meresekQuery -> {
                    if (meresekQuery.isEmpty()) {
                        Log.d("OldReadings", "a 'readings' kollekcióban nincs semmi (ezzel uidval)");
                        nothingIcon.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (var egyMeres : meresekQuery.getDocuments()) {
                        String serial = egyMeres.getString("serial");
                        Meres meres = new Meres();
                        meres.setId(egyMeres.getId());
                        meres.setMeroszama(serial);
                        meres.setMeres( Integer.parseInt( egyMeres.get("reading").toString()) );

                        // opcionálisan formázhatod dátummá
                        Timestamp ts = egyMeres.getTimestamp("timestamp");
                        if (ts != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            meres.setDate(sdf.format(ts.toDate()));
                        }

                        meresekLista.add(meres);
                        orAdapter.notifyDataSetChanged();
                        nothingIcon.setVisibility(View.GONE);


                    }
                });
        Log.d("OldReadings", "Összesen betöltött mérés: " + meresekLista.size());
    }


    public void cancelOR(View view) {
        finish();
    }
}