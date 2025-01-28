package com.example.model;

import android.os.Bundle;
import android.util.Log;
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IrakasleActivity extends AppCompatActivity {

    private TableLayout horarioTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irakasle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ordutegia_title);
        }

        // Obtener los datos del usuario enviados desde MainActivity
        Users user = (Users) getIntent().getSerializableExtra("userData");

        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        if (user != null) {
            welcomeTextView.setText("Bienvenido, Profesor " + user.getNombre());
        } else {
            welcomeTextView.setText("Bienvenido, Profesor");
        }

        // Inicializa el TableLayout donde se mostrarán los horarios
        horarioTableLayout = findViewById(R.id.horarioTableLayout);

        // Obtener los horarios del servidor
        String email = user != null ? user.getEmail() : "itziar@elorrieta-errekamari.com";
        getHorarios(email);
    }

    // Método para obtener los horarios desde el servidor
    private void getHorarios(String email) {
        new Thread(() -> {
            try {
                Log.d("DEBUG", "Intentando conectar al servidor...");

                Socket socket = new Socket("10.5.104.21", 54321);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                Log.d("DEBUG", "Conexión con el servidor establecida");


                // Enviar solicitud de obtener horarios al servidor
                outputStream.writeUTF("getHorarios");
                outputStream.writeUTF(email);
                outputStream.flush();
                Log.d("DEBUG", "Solicitud enviada al servidor para el usuario: " + email);

                Log.d("DEBUG", "Esperando la respuesta del servidor...");
                Object response = null;
                try {
                    response = inputStream.readObject();  // Lee el objeto del flujo de entrada
                } catch (IOException e) {
                    Log.e("ERROR", "Error de entrada/salida", e);
                } catch (ClassNotFoundException e) {
                    Log.e("ERROR", "Error al deserializar el objeto", e);
                }

                if (response != null) {
                    Log.d("DEBUG", "Respuesta del servidor: " + response);
                    Log.d("DEBUG", "Clase de la respuesta: " + response.getClass().getName());
                } else {
                    Log.d("DEBUG", "No se recibió ninguna respuesta.");
                }

                if (response instanceof ArrayList) {
                    ArrayList<Horarios> horariosList = (ArrayList<Horarios>) response;
                    Log.d("DEBUG", "Horarios recibidos: " + horariosList.size());

                    runOnUiThread(() -> {
                        if (!horariosList.isEmpty()) {
                            rellenarTabla(horariosList);
                        } else {
                            Toast.makeText(IrakasleActivity.this, "No se encontraron horarios", Toast.LENGTH_SHORT).show();
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

    private void rellenarTabla(ArrayList<Horarios> horarios) {
        horarioTableLayout.removeAllViews(); // Limpiar la tabla antes de llenarla

        // Crear la fila de los días de la semana (encabezado)
        TableRow encabezadoRow = new TableRow(this);
        String[] dias = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};

        for (String dia : dias) {
            TextView diaTextView = new TextView(this);
            diaTextView.setText(dia);
            diaTextView.setPadding(16, 16, 16, 16);
            diaTextView.setTextSize(16);
            diaTextView.setBackgroundResource(R.drawable.table_header_background);
            encabezadoRow.addView(diaTextView);
        }
        horarioTableLayout.addView(encabezadoRow);

        // Crear las filas para las horas del 1 al 6
        for (int bloque = 1; bloque <= 6; bloque++) {
            TableRow row = new TableRow(this);

            // Mostrar el número del bloque en la primera columna
            TextView bloqueTextView = new TextView(this);
            bloqueTextView.setText(String.valueOf(bloque)); // Solo muestra el número
            bloqueTextView.setPadding(16, 16, 16, 16);
            row.addView(bloqueTextView);

            // Añadir columnas para cada día de la semana
            for (int i = 1; i < dias.length; i++) {
                TextView asignaturaTextView = new TextView(this);

                // Buscar la asignatura para el día y el bloque actual
                String diaActual = dias[i];
                String asignatura = "";

                for (Horarios horario : horarios) {

                    Log.d("Syso", horario.getModulos().getNombre());
                    asignatura = horario.getModulos().getNombre();
                    // Si coinciden el día y el bloque, obtenemos la asignatura
//                    if (horario.getId().getHora() == bloque && horario.getId().getDia().equals(diaActual)) {
//                        asignatura = horario.getModulos().getNombre();
//
//                        break;
//                    }
                }

                // Si no se encuentra asignatura, asigna un texto por defecto
                if (asignatura.isEmpty()) {
                    asignatura = "Sin asignatura";
                }

                Log.d("DEBUG", "Bloque: " + bloque + ", Día: " + diaActual + ", Asignatura: " + asignatura); // Verificación adicional

                asignaturaTextView.setText(asignatura);
                asignaturaTextView.setPadding(16, 16, 16, 16);
                row.addView(asignaturaTextView);
            }

            horarioTableLayout.addView(row);
        }
    }
}
