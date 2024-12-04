package com.example.tiendamovilapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;

public class InsertarProducto extends AppCompatActivity {

    ImageView insertarImagen;
    Button guardarButton;
    EditText insertarCodigo, insertarNombre, insertarCantidad, insertarPrecio;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_producto);

        insertarImagen = findViewById(R.id.insertarImagen);
        insertarCodigo = findViewById(R.id.insertarCodigo);
        insertarNombre = findViewById(R.id.insertarTitulo);
        insertarCantidad = findViewById(R.id.insertarCantidad);
        insertarPrecio = findViewById(R.id.insertarPrecio);
        insertarImagen = findViewById(R.id.insertarImagen);
        guardarButton = findViewById(R.id.guardarButton);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            insertarImagen.setImageURI(uri);
                        }else{
                            Toast.makeText(InsertarProducto.this, "Imagen No Seleccionada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        insertarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seleccionarFoto = new Intent(Intent.ACTION_PICK);
                seleccionarFoto.setType("image/*");
                activityResultLauncher.launch(seleccionarFoto);
            }
        });

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("TM Imagenes").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(InsertarProducto.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                insertarDatos();
                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void insertarDatos(){

        String codigoBarras = insertarCodigo.getText().toString();
        String nombre = insertarNombre.getText().toString();
        int cantidad = Integer.parseInt(insertarCantidad.getText().toString());
        int precio = Integer.parseInt(insertarPrecio.getText().toString());

        DataClass dataClass = new DataClass(codigoBarras, nombre, cantidad, precio, imageURL, "");

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Productos");
        String uid = ref.push().getKey(); //Obtenemos un id para la referencia, y generar el id del producto

        ref.child(uid).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(InsertarProducto.this, "Guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertarProducto.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}