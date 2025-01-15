package com.example.e2_t5_mob;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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
                }, 3000); // 3 segundos de duración
    }
}