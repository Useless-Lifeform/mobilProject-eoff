package com.example.eoffapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends AppCompatActivity {
    EditText registEmailET;
    EditText registPassET;
    EditText registPass2ET;
    EditText nameET;
    Button regBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore myDB;

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
        nameET  = findViewById(R.id.nameET);
        regBtn = findViewById(R.id.regBtn);
        regBtn.setBackgroundColor(Color.LTGRAY);

        mAuth = FirebaseAuth.getInstance();
        myDB = FirebaseFirestore.getInstance();

        TextWatcher inputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        registEmailET.addTextChangedListener(inputTextWatcher);
        registPassET.addTextChangedListener(inputTextWatcher);
        nameET.addTextChangedListener(inputTextWatcher);
    }
    private void validateInputs() {
        String emailInput = registEmailET.getText().toString().trim();
        String pass  = registPassET.getText().toString();
        String name = nameET.getText().toString().trim();

        boolean isEmailValid = ( !emailInput.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() );
        boolean isPasswordNotEmpty = !pass.isEmpty();
        boolean isNameNotEmpty = !name.isEmpty();

        Log.d("ValidE", " we are in the validating methode! ");

        if(isEmailValid && isPasswordNotEmpty && isNameNotEmpty){
            regBtn.setEnabled(true);
            regBtn.setBackgroundColor(Color.GREEN);
        }else {
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(Color.LTGRAY);
        }
        //android:backgroundTint="#4CAF50"
    }
    public void cancel(View view) {
        finish();
    }

    public void registNow(View view) {
        String email = registEmailET.getText().toString();
        String pass  = registPassET.getText().toString();
        String pass2 = registPass2ET.getText().toString();
        String name = nameET.getText().toString();

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
                    // Regisztráció sikeres
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();

                        // Új user adat objektum
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);

                        // Firestore-ba mentés
                        myDB.collection("users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    // Sikeres mentés
                                    Log.d("Firestore", "User added successfully!");
                                })
                                .addOnFailureListener(e -> {
                                    // Hiba a Firestore mentéskor
                                    Log.e("Firestore", "Error adding user");
                                });
                    }
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