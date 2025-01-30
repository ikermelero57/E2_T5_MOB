package com.example.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Users;

public class ProfilaActivity extends AppCompatActivity {

    private TextView textViewNombre, textViewApellido, textViewDni, textViewTelefono, textViewEmail, textViewDireccion;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profila);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambiar el ícono de los tres puntos a blanco (si lo deseas)
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_points_white));

        // Set title (depending on language)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.profila_title); // Utiliza el título adecuado para euskera o español
        }

        // Asignar los datos del usuario
        textViewNombre = findViewById(R.id.textViewNombre);
        textViewApellido = findViewById(R.id.textViewApellido);
        textViewDni = findViewById(R.id.textViewDni);
        textViewTelefono = findViewById(R.id.textViewTelefono);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewDireccion = findViewById(R.id.textViewDireccion);

        Users user = (Users) getIntent().getSerializableExtra("userData");

        if (user != null) {
            textViewNombre.setText("Nombre: " + user.getNombre());
            textViewApellido.setText("Apellido: " + user.getApellidos());
            textViewDni.setText("DNI: " + user.getDni());
            textViewTelefono.setText("Teléfono: " + user.getTelefono1());
            textViewEmail.setText("Email: " + user.getEmail());
            textViewDireccion.setText("Dirección: " + user.getDireccion());
        } else {
            Toast.makeText(this, "Error: Datos de usuario no disponibles.", Toast.LENGTH_SHORT).show();
            finish();  // Cierra la actividad si no hay datos
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profila, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Users user = (Users) getIntent().getSerializableExtra("userData");

        int itemId = item.getItemId();

        if (itemId == R.id.itemIrakasle) {
            if (user != null) {
                Intent intent = new Intent(this, IrakasleActivity.class);
                intent.putExtra("userData", user);
                startActivity(intent);
            }
            return true;

        } else if (itemId == R.id.itemIrten) {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza la actividad actual
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
