package com.example.model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.e2_t5_mob.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        // Encuentra la ImageView donde mostrarás el GIF
        ImageView splashImage = findViewById(R.id.splashImage);

        // Usar Glide para cargar el GIF
        Glide.with(this)
                .asGif()
                .load(R.drawable.logo) // Nombre del archivo GIF en drawable
                .into(splashImage);

        // Opcional: Puedes agregar un retraso para que el splash screen se muestre durante unos segundos
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        // Aquí puedes hacer la transición a la MainActivity, por ejemplo
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }, 4000); // 3 segundos de duración
    }
}