package com.example.model;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e2_t5_mob.R;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Users;

public class IkaslezerrendaActivity extends AppCompatActivity {

    private ListView listViewEstudiantes;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikaslezerrenda);

        // Obtener el correo electrónico del profesor pasado desde la actividad anterior
        userEmail = getIntent().getStringExtra("userEmail");

        listViewEstudiantes = findViewById(R.id.listViewEstudiantes); // Asegúrate de tener el ListView en el XML

        // Obtener la lista de estudiantes del servidor
        if (userEmail != null && !userEmail.isEmpty()) {
            getStudents(userEmail);
        } else {
            Log.e("ERROR", "Email recibido es nulo o vacío");
            Toast.makeText(this, "Error al recibir el correo electrónico", Toast.LENGTH_SHORT).show();
        }
    }

    private void getStudents(String email) {
        new Thread(() -> {
            try {
                Log.d("DEBUG", "Intentando conectar al servidor para obtener estudiantes...");
                Socket socket = new Socket("10.5.104.21", 54321); // Dirección IP y puerto del servidor
                Log.d("DEBUG", "Conexión establecida con el servidor.");

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                // Enviar mensaje al servidor para obtener la lista de estudiantes
                outputStream.writeUTF("getStudents");
                outputStream.writeUTF(email);
                outputStream.flush();
                Log.d("DEBUG", "Mensaje enviado al servidor.");

                // Leer respuesta del servidor
                Object response = inputStream.readObject();
                Log.d("DEBUG", "Respuesta recibida del servidor.");

                if (response instanceof ArrayList) {
                    ArrayList<Users> estudiantesList = (ArrayList<Users>) response;

                    // Log para ver el contenido de la lista
                    for (Users estudiante : estudiantesList) {
                        Log.d("DEBUG", "Estudiante recibido: " + estudiante.getNombre() + " " + estudiante.getApellidos());
                    }

                    // Actualizar la UI con la lista de estudiantes en el hilo principal
                    runOnUiThread(() -> {
                        if (!estudiantesList.isEmpty()) {
                            mostrarEstudiantes(estudiantesList);
                        } else {
                            Toast.makeText(this, "No se encontraron estudiantes", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("ERROR", "La respuesta del servidor no es una lista válida de estudiantes");
                }

                socket.close();
                Log.d("DEBUG", "Conexión cerrada.");
            } catch (Exception e) {
                Log.e("ERROR", "Error al conectar con el servidor", e);
            }
        }).start();
    }

    private void mostrarEstudiantes(ArrayList<Users> estudiantes) {
        // Crear el adaptador personalizado para mostrar los estudiantes
        StudentAdapter adapter = new StudentAdapter(this, estudiantes);
        listViewEstudiantes.setAdapter(adapter);
    }
}
