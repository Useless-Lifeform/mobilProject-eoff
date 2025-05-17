package com.example.eoffapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eoffapp.models.Meres;
import com.example.eoffapp.models.MeroOra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {
    private LinearLayout ujJelszoBox, ujMeroBox, myMerosBox;
    private EditText ujMeroET;
    private TextView textViewName, meroorakListTW;
    private String UID;
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }
        );

        ujJelszoBox = findViewById(R.id.ujJelszoBox);
        ujMeroBox=findViewById(R.id.ujMeroBox);
        myMerosBox=findViewById(R.id.myMerosBox);

        ujMeroET=findViewById(R.id.ujMeroET);
        textViewName=findViewById(R.id.textViewName);
        meroorakListTW=findViewById(R.id.meroorakListTW);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID=user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String name = task.getResult().getString("name");
                    textViewName.setText(name);
                }
            });
        }
        else {
            finish();
        }
        db = FirebaseFirestore.getInstance();

        populateMeroorak();
    }

    void populateMeroorak(){///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        db.collection("users").document(UID).collection("meroorak").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String tmp ="";
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //Log.d(TAG, document.getId() + " => " + document.getData());
                    tmp+=document.getId()+"\n";
                }
                meroorakListTW.setText(tmp);
            } else {
                Log.d("ProfilActivity", "Error getting documents: ", task.getException());
            }


        });
    }
    public void openCloseJelszoBox(View view) {
        if (ujJelszoBox.getVisibility() == View.GONE) {
            ujJelszoBox.setVisibility(View.VISIBLE);
        } else {
            ujJelszoBox.setVisibility(View.GONE);
        }
    }
    public void openCloseMeroBox(View view) {
        if (ujMeroBox.getVisibility() == View.GONE) {
            ujMeroBox.setVisibility(View.VISIBLE);
        } else {
            ujMeroBox.setVisibility(View.GONE);
        }
    }

    public void openCloseMyMerosBox(View view) {
        if (myMerosBox.getVisibility() == View.GONE) {
            myMerosBox.setVisibility(View.VISIBLE);
        } else {
            myMerosBox.setVisibility(View.GONE);
        }
    }

    public void close(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void addMero(View view) {
        String meroszam=ujMeroET.getText().toString();
        if(meroszam.isBlank())
            return;
        //TODO: formátum ellenőrzés egyedi igények szerint
        Log.d("addMero","Mero hozzaadasa");

        db.collection("users").document(UID).collection("meroorak")
                .document(meroszam).set(new HashMap<>())
                   .addOnSuccessListener(aVoid -> {Log.d("addMero","SIKERES meroóra hozzáadva");
                        showPopUp("Sikerers Mérőóra hozzáadás!");});
        ujMeroET.setText("");
    }
    private void showPopUp( String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                //.setTitle(title)
                .setMessage(message)//TOdo: message from resource, (forever WIP)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}