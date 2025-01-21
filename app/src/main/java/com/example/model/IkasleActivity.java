package com.example.model;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;

public class IkasleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikasle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Establecer el título del Toolbar con la cadena traducida
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ordutegia_title);
        }
    }
}
