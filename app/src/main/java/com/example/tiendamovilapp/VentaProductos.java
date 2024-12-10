package com.example.tiendamovilapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VentaProductos extends AppCompatActivity implements ProductoSeleccionadoCallback {
    FloatingActionButton fab;
    RecyclerView recyclerViewVenta;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    AdaptadorVenta adaptadorVenta;
    private ArrayList<DataClass> productosOrden;
    private TextView txtProductoInfo, txtPrecioTotal;
    private float precioTotal = 0.0f;
    private Button btnFinalizarVenta;

    private float totalFromPreferences;

    private static final String PREFS_NAME = "TiendaMovilDoc"; // Nombre del archivo SharedPreferences
    private static final String KEY_TOTAL = "total"; // Clave para el valor total


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_venta_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productosOrden = new ArrayList<>();
        txtProductoInfo = findViewById(R.id.txtProductoInfo);
        txtPrecioTotal = findViewById(R.id.txtPrecioTotal);
        txtProductoInfo = findViewById(R.id.txtProductoInfo);
        recyclerViewVenta = findViewById(R.id.recyclerViewVenta);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        btnFinalizarVenta = findViewById(R.id.btnFinalizarVenta);


        totalFromPreferences = getTotal();

        btnFinalizarVenta.setOnClickListener(view -> finalizarVenta());


        GridLayoutManager gridLayoutManager = new GridLayoutManager(VentaProductos.this, 1);
        recyclerViewVenta.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(VentaProductos.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();



        databaseReference = FirebaseDatabase.getInstance().getReference("Productos");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());

                    dataList.add(dataClass);
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });


    }

    @SuppressLint("DefaultLocale")
    @Override
    public void productoSeleccionado(DataClass producto) {
        if (producto != null) {
            productosOrden.add(producto); // Agregar producto a la lista

            double precioProducto = 0.0;
            try {
                precioProducto = Double.parseDouble(producto.getDataPrec()); // Si getPrecio() es String
            } catch (NumberFormatException e) {
                Log.e("Error", "El precio no tiene un formato válido: " + producto.getDataPrec());

            }
            precioTotal += precioProducto;
            // Crear texto para el TextView
            StringBuilder textoLista = new StringBuilder();

            for (int i = 0; i < productosOrden.size(); i++) {
                textoLista.append(i + 1) // Número de producto
                        .append(". ") // Punto
                        .append(productosOrden.get(i).getDataNom())
                        .append(" - $")// Nombre del producto
                        .append(productosOrden.get(i).getDataPrec())
                        .append("\n"); // Salto de línea
            }

            // Actualizar el TextView
            txtProductoInfo.setText(textoLista.toString());

            txtPrecioTotal.setText("Total: $" + String.format("%.2f", precioTotal));
 }

}
    private void finalizarVenta() {
        // Crear un Intent para enviar los datos al siguiente Activity
        Intent intent = new Intent(VentaProductos.this, VentaFinalizadaActivity.class);

        // Convertir los datos de productos a un formato que puedas pasar, como un ArrayList de Strings
        ArrayList<String> productosInfo = new ArrayList<>();
        for (DataClass producto : productosOrden) {
            productosInfo.add(producto.getDataNom() + " - $" + producto.getDataPrec());
        }

        saveTotal(precioTotal);


        // Pasar los datos al siguiente Activity
        intent.putStringArrayListExtra("productosOrden", productosInfo);
        intent.putExtra("totalVenta", precioTotal);

        // Iniciar el nuevo Activity
        startActivity(intent);
    }

    public void searchList(String text) {

        ArrayList<DataClass> searchList = new ArrayList<>();

        if (!text.isEmpty()) {
            for (DataClass dataClass : dataList) {
                if (dataClass.getDataNom().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(dataClass);
                }
            }


            adaptadorVenta = new AdaptadorVenta(VentaProductos.this, searchList, this);
            recyclerViewVenta.setAdapter(adaptadorVenta);
            adaptadorVenta.searchDataList(searchList);
        } else {
            adaptadorVenta.searchDataList(searchList);
        }
    }

    private void saveTotal(float value) {
        float suma = totalFromPreferences + value;
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_TOTAL, suma);
        editor.apply(); // Confirmar los cambios


    }

    // Método para leer el valor asociado a "total"
    private float getTotal() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_TOTAL, 0f); // Retorna 0 si no se encuentra la clave
    }

}
