package com.example.e2_t5_mob;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.LocaleList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class HizkuntzaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hizkuntza);

        // Aquí defines tu spinner o cualquier otro control
        Spinner languageSpinner = findViewById(R.id.language_spinner);

        // Lógica para cuando el usuario elige el idioma
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el idioma seleccionado
                String selectedLanguage = (String) parentView.getItemAtPosition(position);
                updateLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nada seleccionado
            }
        });
    }

    // Método para actualizar el idioma de la aplicación
    private void updateLanguage(String language) {
        Locale locale;
        if (language.equals("es")) {
            locale = new Locale("es");
        } else {
            locale = new Locale("eu"); // Euskera
        }

        // Para versiones de API 24 o superior, utilizar LocaleList
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            Configuration config = new Configuration();
            config.setLocales(localeList);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } else {
            // Para versiones anteriores de Android, usamos Locale directamente
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        // Guardar el idioma en las preferencias
        // Aquí se pueden guardar preferencias o usar cualquier método para persistir el idioma elegido
        Toast.makeText(this, "Idioma cambiado a " + language, Toast.LENGTH_SHORT).show();
    }
}
