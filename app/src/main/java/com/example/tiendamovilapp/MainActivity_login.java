package com.example.tiendamovilapp;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class MainActivity_login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.txtlink);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_login.this, MainActivity_register.class));

            }
        });





    }
    private void loginUser() {
        String email = emailEditText != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText != null ? passwordEditText.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity_login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí puedes redirigir al usuario a la actividad principal
                            startActivity(new Intent(MainActivity_login.this, MenuPrincipal.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity_login.this, "Error en el inicio de sesión: " +
                                            (task.getException() != null ? task.getException().getMessage() : "Desconocido"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
           });
    }

}