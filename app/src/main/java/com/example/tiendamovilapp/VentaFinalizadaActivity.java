package com.example.tiendamovilapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class VentaFinalizadaActivity extends AppCompatActivity {

    private TextView txtProductosVendidos, txtTotalVenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_finalizada);


        // Inicializar los TextViews
        txtProductosVendidos = findViewById(R.id.txtProductosVendidos);
        txtTotalVenta = findViewById(R.id.txtTotalVenta);

        // Recibir los datos del Intent
        ArrayList<String> productosOrden = getIntent().getStringArrayListExtra("productosOrden");
        double totalVenta = getIntent().getDoubleExtra("totalVenta", 0.0);

        // Mostrar los productos vendidos
        if (productosOrden != null && !productosOrden.isEmpty()) {
            StringBuilder productosTexto = new StringBuilder();
            for (String producto : productosOrden) {
                productosTexto.append(producto).append("\n");
            }
            txtProductosVendidos.setText(productosTexto.toString());
        }

        // Mostrar el total de la venta
        txtTotalVenta.setText("Total a Pagar: $" + String.format("%.2f", totalVenta));
    }
}


