package com.example.eoffapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
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


public class LoginActivity extends AppCompatActivity {
    EditText usernameET;
    EditText passwordET;
    Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.userPassET);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();


        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameET.addTextChangedListener(loginTextWatcher);
        passwordET.addTextChangedListener(loginTextWatcher);
    }

/***/public void login2(View view) {///MAJD VEDD KI!
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }
    public void login(View view) {//ET EditText      TV TextView
        loginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));




        String userName = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if(userName.isBlank() || password.isBlank()){
            usernameET.setError("Nem lehet üres!");
            passwordET.setError("Nem lehet üres!");
            return;
        }


        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //we are happy
                    openMain();

                }
                else{
                    //we are not happy
                    showErrorDialog("Hibás email vagy jelszó", "Kérlek ellenőrizd az adataid és próbáld újra.");
                }
            }
        });

    }//-----

    private void validateInputs() {
        String emailInput = usernameET.getText().toString().trim();
        String passwordInput = passwordET.getText().toString();

        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
        boolean isPasswordNotEmpty = !passwordInput.isEmpty();

        loginBtn.setEnabled(isEmailValid && isPasswordNotEmpty);
    }

    private void showErrorDialog(String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void regist(View view) {
        Intent registIntent = new Intent(this, RegistActivity.class);
        startActivity(registIntent);
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}