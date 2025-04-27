package com.example.eoffapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfilActivity extends AppCompatActivity {
    private LinearLayout ujJelszoBox;
    private LinearLayout ujMeroBox;

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
}