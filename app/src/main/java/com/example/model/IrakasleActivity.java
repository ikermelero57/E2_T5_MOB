package com.example.model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.e2_t5_mob.R;
import model.Horarios;
import model.Users;
import android.content.Intent;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class IrakasleActivity extends AppCompatActivity {

    private TableLayout tablaLunes, tablaMartes, tablaMiercoles, tablaJueves, tablaViernes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irakasle);

        // Inicializar las tablas
        tablaLunes = findViewById(R.id.tablaLunes);
        tablaMartes = findViewById(R.id.tablaMartes);
        tablaMiercoles = findViewById(R.id.tablaMiercoles);
        tablaJueves = findViewById(R.id.tablaJueves);
        tablaViernes = findViewById(R.id.tablaViernes);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambiar el ícono de los tres puntos a blanco
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_points_white)); // Asegúrate de tener este ícono en tu carpeta drawable

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ordutegia_title);
        }

        // Obtener datos del usuario
        Users user = (Users) getIntent().getSerializableExtra("userData");
        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        welcomeTextView.setText(user != null ? "Bienvenido, Profesor " + user.getNombre() : "Bienvenido, Profesor");

        // Obtener horarios
        String email = user != null ? user.getEmail() : "itziar@elorrieta-errekamari.com";
        getHorarios(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_irakasle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemPerfil) {
            Users user = (Users) getIntent().getSerializableExtra("userData");

            if (user != null) {
                Log.d("DEBUG", "Usuario encontrado: " + user.getNombre());  //  LOG PARA DEPURAR
                Intent intent = new Intent(this, ProfilaActivity.class);
                intent.putExtra("userData", user);
                startActivity(intent);
            } else {
                Log.e("ERROR", "Usuario es NULL. No se puede abrir el perfil.");
                Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (item.getItemId() == R.id.itemIkasleZerrenda) {
            Users user = (Users) getIntent().getSerializableExtra("userData");
            if (user != null) {
                Intent intent = new Intent(this, IkaslezerrendaActivity.class);
                intent.putExtra("userEmail", user.getEmail());  // Pasar el correo electrónico del profesor
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (item.getItemId() == R.id.itemSalir) {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();  // Mensaje de cierre de sesión
            finish();  // Cierra la actividad actual
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void getHorarios(String email) {
        new Thread(() -> {
            try {
                Log.d("DEBUG", "Intentando conectar al servidor...");
                Socket socket = new Socket("10.5.104.21", 54321);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeUTF("getHorarios");
                outputStream.writeUTF(email);
                outputStream.flush();

                Object response = inputStream.readObject();

                if (response instanceof ArrayList) {
                    ArrayList<Horarios> horariosList = (ArrayList<Horarios>) response;
                    runOnUiThread(() -> {
                        if (!horariosList.isEmpty()) {
                            rellenarTablas(horariosList);
                        } else {
                            Toast.makeText(this, "No se encontraron horarios", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("ERROR", "La respuesta del servidor no es un ArrayList de Horarios");
                }

                socket.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error al conectar con el servidor", e);
            }
        }).start();
    }

    private void rellenarTablas(ArrayList<Horarios> horarios) {
        HashMap<String, String> horarioMap = new HashMap<>();

        for (Horarios horario : horarios) {
            int hora = Character.getNumericValue(horario.getId().getHora());
            String diaNormalizado = horario.getId().getDia().split("/")[0];

            String clave = hora + "-" + diaNormalizado;
            String asignatura = horario.getModulos().getNombre();
            horarioMap.put(clave, asignatura);

            Log.d("DEBUG", "Guardado en mapa -> " + clave + ": " + asignatura);
        }

        // Crear las filas para los días de la semana
        crearTablaDia(tablaLunes, horarioMap, "L");
        crearTablaDia(tablaMartes, horarioMap, "M");
        crearTablaDia(tablaMiercoles, horarioMap, "X");
        crearTablaDia(tablaJueves, horarioMap, "J");
        crearTablaDia(tablaViernes, horarioMap, "V");
    }

    private void crearTablaDia(TableLayout tableLayout, HashMap<String, String> horarioMap, String dia) {
        // Crear encabezado con la hora y el día
        TableRow encabezadoRow = new TableRow(this);
        String[] dias = {"Hora", dia};

        for (String item : dias) {
            TextView textView = new TextView(this);
            textView.setText(item);
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.parseColor("#f0f0f0"));

            TableRow.LayoutParams layoutParams;
            if (item.equals("Hora")) {
                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            } else {
                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
            }

            textView.setLayoutParams(layoutParams);

            if (item.equals("Hora") || item.equals(dia)) {
                textView.setBackgroundResource(R.drawable.gradient_background); // Fondo con gradiente
            }

            setCellBorder(textView);
            encabezadoRow.addView(textView);
        }
        tableLayout.addView(encabezadoRow);

        for (int bloque = 1; bloque <= 5; bloque++) {
            TableRow row = new TableRow(this);

            TextView bloqueTextView = new TextView(this);
            bloqueTextView.setText(String.valueOf(bloque));
            bloqueTextView.setPadding(16, 16, 16, 16);
            bloqueTextView.setGravity(Gravity.CENTER);
            TableRow.LayoutParams horaParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            bloqueTextView.setLayoutParams(horaParams);

            bloqueTextView.setBackgroundColor(Color.WHITE);
            setCellBorder(bloqueTextView);

            row.addView(bloqueTextView);

            String clave = bloque + "-" + dia;
            String asignatura = horarioMap.getOrDefault(clave, "");

            TextView asignaturaTextView = new TextView(this);
            asignaturaTextView.setText(asignatura);
            asignaturaTextView.setPadding(16, 16, 16, 16);
            asignaturaTextView.setGravity(Gravity.CENTER);
            TableRow.LayoutParams asignaturaParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
            asignaturaTextView.setLayoutParams(asignaturaParams);

            asignaturaTextView.setBackgroundColor(Color.WHITE);
            setCellBorder(asignaturaTextView);

            row.addView(asignaturaTextView);

            tableLayout.addView(row);
        }
    }

    private void setCellBorder(TextView textView) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.LTGRAY);
        border.setStroke(2, Color.parseColor("#d4dcdf"));
        textView.setBackground(border);
    }
}
