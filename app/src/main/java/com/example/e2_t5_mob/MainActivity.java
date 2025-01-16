package com.example.e2_t5_mob;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Mostrar el cuadro de diálogo de selección de idioma cada vez que se inicie la aplicación
        showLanguageSelectionDialog();
    }

    // Mostrar el cuadro de diálogo de selección de idioma
    private void showLanguageSelectionDialog() {
        final String[] languages = {"Euskara", "Español"};
        final String[] languageCodes = {"eu", "es"}; // Códigos de idioma correspondientes

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hizkuntza / Idioma")
                .setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aplicar el idioma seleccionado
                        setAppLocale(languageCodes[which]);

                        // Cerrar el cuadro de diálogo y proceder al contenido principal
                        dialog.dismiss();
                        proceedToMainContent();
                    }
                })
                .setCancelable(false); // Evita que el usuario cierre el cuadro sin seleccionar un idioma
        builder.create().show();
    }

    // Cambiar el idioma de la aplicación
    private void setAppLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void proceedToMainContent() {
        // Configurar el diseño principal
        setContentView(R.layout.activity_main);

        // Actualizar el título del Toolbar con la cadena traducida
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));  // Usa el string traducido

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verificar la conexión a Internet y mostrar mensaje
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No hay conexión a Internet. Por favor, conecta tu dispositivo.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Conexión a Internet disponible", Toast.LENGTH_SHORT).show();
        }
    }
}



