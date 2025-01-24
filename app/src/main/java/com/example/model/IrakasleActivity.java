package com.example.model;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;

import model.Users;

public class IrakasleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irakasle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Establecer el título del Toolbar con la cadena traducida
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ordutegia_title);
        }

        // Obtener los datos del usuario enviados desde MainActivity
        Users user = (Users) getIntent().getSerializableExtra("userData");

        // Mostrar información del profesor, como su nombre
        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        if (user != null) {
            welcomeTextView.setText("Bienvenido, Profesor " + user.getNombre());
        } else {
            welcomeTextView.setText("Bienvenido, Profesor");
        }
    }
}
