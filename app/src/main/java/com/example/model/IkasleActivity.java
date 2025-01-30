package com.example.model;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;
import model.Horarios;
import model.Users;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class IkasleActivity extends AppCompatActivity {

    private TableLayout tablaLunes, tablaMartes, tablaMiercoles, tablaJueves, tablaViernes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikasle);

        // Inicializar las tablas
        tablaLunes = findViewById(R.id.tablaLunes);
        tablaMartes = findViewById(R.id.tablaMartes);
        tablaMiercoles = findViewById(R.id.tablaMiercoles);
        tablaJueves = findViewById(R.id.tablaJueves);
        tablaViernes = findViewById(R.id.tablaViernes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ikasle_title);
        }

        // Obtener datos del usuario pasado desde el intent
        Users user = (Users) getIntent().getSerializableExtra("userData");

        // Mostrar datos del usuario en un TextView (ejemplo)
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Bienvenido, " + user.getNombre());

        // Obtener el horario del servidor
        getHorarios(user.getEmail());
    }

    // Método para obtener el horario del servidor
    private void getHorarios(String email) {
        new Thread(() -> {
            try {
                // Log para indicar que la conexión se está intentando
                Log.d("DEBUG", "Intentando conectar al servidor...");
                Socket socket = new Socket("10.5.104.21", 54321);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                // Cambiar la llamada para utilizar el nuevo método getHorariosByStudent
                outputStream.writeUTF("getHorariosByStudent");  // Aquí usas "getHorariosByStudent" ahora
                outputStream.writeUTF(email);
                outputStream.flush();

                // Leer la respuesta del servidor
                Object response = inputStream.readObject();

                // Verificar si la respuesta es un ArrayList de horarios
                if (response instanceof ArrayList) {
                    ArrayList<Horarios> horariosList = (ArrayList<Horarios>) response;
                    runOnUiThread(() -> {
                        // Si se encuentran horarios, llenar las tablas
                        if (!horariosList.isEmpty()) {
                            rellenarTablas(horariosList);
                        } else {
                            // Si no se encuentran horarios, mostrar un mensaje
                            Toast.makeText(this, "No se encontraron horarios", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("ERROR", "La respuesta del servidor no es un ArrayList de Horarios");
                }

                // Cerrar la conexión con el servidor
                socket.close();

            } catch (Exception e) {
                // Manejo de errores y mostrar mensaje de error
                Log.e("ERROR", "Error al conectar con el servidor", e);
            }
        }).start();
    }


    // Método para rellenar las tablas con el horario del alumno
    private void rellenarTablas(ArrayList<Horarios> horarios) {
        // Mapa para almacenar los horarios con clave "bloque-día"
        HashMap<String, String> horarioMap = new HashMap<>();

        for (Horarios horario : horarios) {
            int hora = Character.getNumericValue(horario.getId().getHora()); // Convertir char a int
            String diaNormalizado = horario.getId().getDia().split("/")[0]; // Normalizar el día

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
        String[] dias = {"Hora", dia}; // Solo mostrar el día actual

        // Añadir las cabeceras de la tabla (hora y asignatura)
        for (String item : dias) {
            TextView textView = new TextView(this);
            textView.setText(item);
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);  // Centrar texto horizontal y verticalmente
            textView.setBackgroundColor(Color.parseColor("#f0f0f0")); // Fondo color claro para la cabecera

            // Asignar parámetros de ancho flexible con separación
            TableRow.LayoutParams layoutParams;

            if (item.equals("Hora")) {
                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);  // Columna "Hora" más estrecha
            } else {
                layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);  // Columna "Asignatura" más ancha
            }

            textView.setLayoutParams(layoutParams);

            // Añadir borde a la celda de la cabecera
            setCellBorder(textView);

            encabezadoRow.addView(textView);
        }
        tableLayout.addView(encabezadoRow);

        // Crear las filas para las horas (1 al 6)
        for (int bloque = 1; bloque <= 5; bloque++) {
            TableRow row = new TableRow(this);

            // Primera columna con el número de bloque (hora)
            TextView bloqueTextView = new TextView(this);
            bloqueTextView.setText(String.valueOf(bloque));
            bloqueTextView.setPadding(16, 16, 16, 16);
            bloqueTextView.setGravity(Gravity.CENTER);  // Centrar el número de la hora
            TableRow.LayoutParams horaParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);  // Columna de horas estrecha
            bloqueTextView.setLayoutParams(horaParams);  // Añadir ancho flexible

            // No aplicar gradiente al fondo de la hora
            bloqueTextView.setBackgroundColor(Color.WHITE); // Fondo blanco para la celda de hora

            // Añadir borde a la celda de la hora
            setCellBorder(bloqueTextView);

            row.addView(bloqueTextView);

            // Añadir columna con la asignatura correspondiente
            String clave = bloque + "-" + dia;
            String asignatura = horarioMap.getOrDefault(clave, "");

            TextView asignaturaTextView = new TextView(this);
            asignaturaTextView.setText(asignatura);
            asignaturaTextView.setPadding(16, 16, 16, 16);
            asignaturaTextView.setGravity(Gravity.CENTER);  // Centrar la asignatura
            TableRow.LayoutParams asignaturaParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);  // Columna de asignaturas más ancha
            asignaturaTextView.setLayoutParams(asignaturaParams);  // Añadir ancho flexible

            // No aplicar gradiente a las asignaturas
            asignaturaTextView.setBackgroundColor(Color.WHITE); // Fondo blanco para las celdas de asignaturas

            // Añadir borde a la celda de la asignatura
            setCellBorder(asignaturaTextView);

            row.addView(asignaturaTextView);

            tableLayout.addView(row);
        }
    }

    // Método para establecer borde de color #d4dcdf a las celdas
    private void setCellBorder(TextView textView) {
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.LTGRAY);  // Fondo blanco
        border.setStroke(2, Color.parseColor("#d4dcdf"));  // Establece el borde con el color #d4dcdf y un grosor de 2 píxeles
        textView.setBackground(border);  // Aplica el borde
    }
}
