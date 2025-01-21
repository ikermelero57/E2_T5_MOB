package com.example.model;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e2_t5_mob.R;

import java.util.Locale;

import model.Users;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

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

        // Inicializar los elementos de la UI
        emailEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        emailEditText.setText("1");
        passwordEditText.setText("1234");

        // Actualizar el título del Toolbar con la cadena traducida
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verificar la conexión a Internet y mostrar mensaje
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No hay conexión a Internet. Por favor, conecta tu dispositivo.", Toast.LENGTH_LONG).show();
            return;
        }

        // Configurar el botón de login
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamada al método login
            ServerConnection.login(email, password, new ServerConnection.ServerResponse<Users>() {
                @Override
                public void onSuccess(Users users) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Bienvenido, " + users.getNombre(), Toast.LENGTH_SHORT).show();
                        // Aquí puedes iniciar una nueva actividad si lo deseas
                        // Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        // startActivity(intent);
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Error de login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}
