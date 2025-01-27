package com.example.model;

import android.os.Bundle;
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
        String email = "profesor@ejemplo.com";  // Cambia esto con el email del profesor
        obtenerHorarios(email);
    }

    // Método para obtener los horarios desde el servidor
    private void obtenerHorarios(String email) {
        new Thread(() -> {
            try {
                // Establecer conexión con el servidor
                Socket socket = new Socket("ip_del_servidor", 12345); // Cambia la IP y el puerto
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                // Enviar solicitud para obtener los horarios
                outputStream.writeUTF("getHorarios");
                outputStream.writeUTF(email);
                outputStream.flush();

                // Leer la respuesta con los horarios
                ArrayList<Horarios> horariosList = (ArrayList<Horarios>) inputStream.readObject();

                // Procesar los horarios recibidos en la UI principal
                runOnUiThread(() -> {
                    if (horariosList != null) {
                        // Llenar la tabla con los horarios
                        rellenarTabla(horariosList);
                    } else {
                        Toast.makeText(IrakasleActivity.this, "No se encontraron horarios", Toast.LENGTH_SHORT).show();
                    }
                });

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para rellenar la tabla con los horarios
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

        // Crear las filas para cada hora del día
        for (Horarios horario : horarios) {
            TableRow row = new TableRow(this);

            // Mostrar la hora en la primera columna
            TextView horaTextView = new TextView(this);
            String hora = String.valueOf(horario.getId().getHora()); // Usamos la hora del HorariosId
            horaTextView.setText(hora);
            horaTextView.setPadding(16, 16, 16, 16);
            row.addView(horaTextView);

            // Añadir las asignaturas a la fila según el día
            for (String dia : dias) {
                TextView asignaturaTextView = new TextView(this);

                // Si el día coincide, mostramos la asignatura
                if (dia.equals(horario.getId().getDia())) {
                    asignaturaTextView.setText(horario.getModulos().getNombre());  // Suponemos que Modulos tiene un campo "nombre"
                } else {
                    asignaturaTextView.setText(""); // Dejar vacío si no es el día correspondiente
                }

                asignaturaTextView.setPadding(16, 16, 16, 16);
                row.addView(asignaturaTextView);
            }

            horarioTableLayout.addView(row);
        }
    }
}
