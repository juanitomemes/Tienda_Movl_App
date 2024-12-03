package com.example.tiendamovilapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MainActivity_register extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        nameEditText = findViewById(R.id.register_nombre);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_register.this, MainActivity_login.class));
                finish();

            }
        });

    }

    private void registerUser() {
        String email = emailEditText != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText != null ? passwordEditText.getText().toString().trim() : "";
        String name = nameEditText != null ? nameEditText.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                ((FirebaseUser) user).updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity_register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MainActivity_register.this, MainActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(MainActivity_register.this, "Error en el registro: " +
                                            (task.getException() != null ? task.getException().getMessage() : "Desconocido"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
          });
}

    }