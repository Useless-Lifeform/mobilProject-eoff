package com.example.eoffapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OldRedings extends AppCompatActivity {

    RecyclerView RecyclerBox;
    ImageView nothingIcon;
    ArrayList<Meres> morakLista;
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
        morakLista = new ArrayList<>();
        orAdapter = new OldReadingsAdapter(this, morakLista);
        RecyclerBox.setAdapter(orAdapter);
        lekerData();
    }
    void lekerData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user == null) {
                finish();
        }
        String uid =  user.getUid();
        db.collection("users").document(uid).collection("meroorak").get().addOnSuccessListener(task ->{
            if(task.isEmpty()){
                return;
            }
            for(var doc : task.getDocuments()){
                String meroszam = doc.getId();
                db.collection("users").document(uid).collection("meroorak").document(meroszam).collection("meresek")
                        .orderBy("datum").limit(10).get().addOnSuccessListener(task2 ->{
                            if(task2.isEmpty()){
                                return;
                            }
                            for(var doc2 : task2.getDocuments()){
                                Meres tmpItem = doc2.toObject(Meres.class);
                                tmpItem.setMeroszama(meroszam);
                                morakLista.add(tmpItem);
                            }
                            orAdapter.notifyDataSetChanged();

                            nothingIcon.setVisibility(View.GONE);
                        });
            }
        });

    }

    public void cancelOR(View view) {
        finish();
    }
}