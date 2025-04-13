package com.example.eoffapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistActivity extends AppCompatActivity {
    EditText registEmailET;
    EditText registPassET;
    EditText registPass2ET;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registEmailET = findViewById(R.id.registEmailET);
        registPassET  = findViewById(R.id.registPassET);
        registPass2ET  = findViewById(R.id.registPass2ET);

        mAuth = FirebaseAuth.getInstance();
    }

    public void cancel(View view) {
        finish();
    }

    public void registNow(View view) {
        String email = registEmailET.getText().toString();
        String pass  = registPassET.getText().toString();
        String pass2 = registPass2ET.getText().toString();

        if(! pass.equals(pass2)){
            registPassET.setError("Nem egyezik a jelszavak!");
            registPass2ET.setError("Nem egyezik a jelszavak!");

            //TODO: jelszóellenrőrzés javítása
            Log.e(RegistActivity.class.getName(), "Nem eggyező jelszavak!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //we are happy
                    openMain();
                }
                else{
                    //we are not happy
                    Log.e(RegistActivity.class.getName(), "Nem sikerült regisztrálni!");
                    //TODO: részletes kifejtés
                }
            }
        });



    }
    private void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}