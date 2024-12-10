package com.example.tiendamovilapp;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CorteCaja extends AppCompatActivity {

    Button btnCorte;
    private static final String PREFS_NAME = "TiendaMovilDoc"; // Nombre del archivo SharedPreferences
    private static final String KEY_TOTAL = "total"; // Clave para el valor total
    private float totalFromPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_corte_caja);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        totalFromPreferences = getTotal();

        btnCorte = findViewById(R.id.btnCorte);
        btnCorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTotal(0);
                Toast.makeText(CorteCaja.this, "El contador ha sido reiniciado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        TextView totalTextView = findViewById(R.id.textViewTotal);


        float total = getTotal();

        // Mostrar el total en el TextView
        totalTextView.setText(String.valueOf(total)); // Convertir el total a texto

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Log.e("User", firebaseUser.getEmail());

    }


    // Método para leer el valor asociado a "total"
    private float getTotal() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_TOTAL, 0f); // Retorna 0 si no se encuentra la clave
    }

    private void saveTotal(float value) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_TOTAL, value);
        editor.apply(); // Confirmar los cambios


    }
}