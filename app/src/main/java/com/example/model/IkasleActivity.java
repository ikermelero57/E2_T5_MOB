package com.example.model;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;

import model.Users;

public class IkasleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikasle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Establecer el título del Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ikasle_title);
        }

        // Obtener datos del usuario pasado desde el intent
        Users user = (Users) getIntent().getSerializableExtra("userData");

        // Mostrar datos del usuario en un TextView (ejemplo)
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Bienvenido, " + user.getNombre());
    }
}
