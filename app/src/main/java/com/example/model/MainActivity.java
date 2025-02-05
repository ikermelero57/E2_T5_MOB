package com.example.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import model.Users;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView tvPasahitzaAldatu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showLanguageSelectionDialog();
    }

    private void showLanguageSelectionDialog() {
        final String[] languages = {"Euskara", "Español"};
        final String[] languageCodes = {"eu", "es"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.language_selection)
                .setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAppLocale(languageCodes[which]);
                        dialog.dismiss();
                        proceedToMainContent();
                    }
                })
                .setCancelable(false);
        builder.create().show();
    }

    private void setAppLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void proceedToMainContent() {
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        tvPasahitzaAldatu = findViewById(R.id.tvPasahitzaAldatu);

        getSupportActionBar().setTitle(getString(R.string.toolbar_title));

        loginButton.setOnClickListener(view -> loginUser());
        tvPasahitzaAldatu.setOnClickListener(view -> pasahitzaAldatu());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.complete_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        String encryptedPassword = encryptPassword(password);

        ServerConnection.login(email, encryptedPassword, new ServerConnection.ServerResponse<Users>() {
            @Override
            public void onSuccess(Users users) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, getString(R.string.welcome) + ", " + users.getNombre(), Toast.LENGTH_SHORT).show();

                    Intent intent = (users.getTipos().getId() <= 3) ?
                            new Intent(MainActivity.this, IrakasleActivity.class) :
                            new Intent(MainActivity.this, IkasleActivity.class);
                    intent.putExtra("userData", users);
                    startActivity(intent);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, getString(R.string.login_error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void pasahitzaAldatu() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return;
        }

        ServerConnection.requestPasswordReset(email, new ServerConnection.ServerResponse<String>() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, getString(R.string.password_sent), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, getString(R.string.error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] dataBytes = password.getBytes();
            md.update(dataBytes);
            byte[] resumen = md.digest();
            return new String(resumen);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
