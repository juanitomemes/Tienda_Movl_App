package com.example.tiendamovilapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MenuPrincipal extends AppCompatActivity {

    private static final String PREFS_NAME = "TiendaMovilDoc"; // Nombre del archivo SharedPreferences
    private static final String KEY_PASSWORD = "password"; // Clave para el valor de password
    private ImageView im_products, im_venta, im_corte;
    private AlertDialog.Builder passwordDialogBuilder;
    private AlertDialog passwordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        im_products = findViewById(R.id.im_products);
        im_venta = findViewById(R.id.im_venta);
        im_corte = findViewById(R.id.im_corte);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        EditText input = dialogView.findViewById(R.id.dialogInput);

        passwordDialogBuilder = new AlertDialog.Builder(MenuPrincipal.this)
                .setTitle("Confirmar Identidad")
                .setView(dialogView) // Establecer el diseño personalizado
                .setCancelable(false)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        passwordDialog = passwordDialogBuilder.create();





        im_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this , ListaProductos.class));

            }
        });
        im_venta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, VentaProductos.class));
            }
        });
        im_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordDialog.show();
                passwordDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;

                        input.setError(null);
                        if (Objects.equals(getPassword(), input.getText().toString())) {
                            startActivity(new Intent(MenuPrincipal.this, CorteCaja.class));
                            wantToCloseDialog = true;
                            input.setText("");
                        } else {
                            input.setError("Ingresa una contraseña valida");
                        }

                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if(wantToCloseDialog)
                            passwordDialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passwordDialog.dismiss();
    }

    private String getPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, ""); // Retorna 0cadena vacía si no se encuentra la clave
    }
}