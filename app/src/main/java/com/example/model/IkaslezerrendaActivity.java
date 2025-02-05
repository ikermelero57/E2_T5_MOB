package com.example.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e2_t5_mob.R;
import model.Users;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IkaslezerrendaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAlumnos;
    private StudentsAdapter adapter;
    private ArrayList<Users> studentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikaslezerrenda);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambiar el ícono de los tres puntos a blanco (opcional)
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_points_white)); // Asegúrate de tener este ícono en tu carpeta drawable

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.ikasle_title);
        }

        // Inicializar el RecyclerView con el ID correcto
        recyclerViewAlumnos = findViewById(R.id.recyclerViewAlumnos);
        recyclerViewAlumnos.setLayoutManager(new LinearLayoutManager(this));

        // Obtener el correo electrónico del profesor
        String userEmail = getIntent().getStringExtra("userEmail");

        // Obtener la lista de alumnos
        getStudents(userEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú desde un archivo XML
        getMenuInflater().inflate(R.menu.menu_ikaslezerrenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemIrakasle) {
            // Volver a IrakasleActivity
            Users user = (Users) getIntent().getSerializableExtra("userData");
            if (user != null) {
                Intent intent = new Intent(this, IrakasleActivity.class);
                intent.putExtra("userData", user);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (item.getItemId() == R.id.itemSalir) {
            // Cerrar sesión
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();  // Mensaje de cierre de sesión
            finish();  // Cierra la actividad actual
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void getStudents(String email) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("10.5.104.21", 54321);
                socket.setSoTimeout(5000); // Evitar bloqueos si el servidor no responde

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeUTF("getStudents");
                outputStream.writeUTF(email);
                outputStream.flush();

                Object response = inputStream.readObject();

                if (response instanceof ArrayList) {
                    Log.d("DEBUG", "Respuesta del servidor: " + response); // Asegúrate de que los datos llegan correctamente
                    studentsList = (ArrayList<Users>) response;

                    runOnUiThread(() -> {
                        if (studentsList != null && !studentsList.isEmpty()) {
                            mostrarListaAlumnos(studentsList);
                        } else {
                            Toast.makeText(this, "No se encontraron alumnos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void mostrarListaAlumnos(ArrayList<Users> studentsList) {
        // Configurar el adaptador para el RecyclerView
        adapter = new StudentsAdapter(studentsList);
        recyclerViewAlumnos.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
        Log.d("DEBUG", "Número de estudiantes: " + studentsList.size()); // Verifica si se reciben alumnos

    }

    // Adaptador del RecyclerView
    private static class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {
        private final ArrayList<Users> studentsList;

        public StudentsAdapter(ArrayList<Users> studentsList) {
            this.studentsList = studentsList;
        }

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
            return new StudentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {
            Users student = studentsList.get(position);
            Log.d("DEBUG", "Nombre del estudiante: " + student.getNombre());  // Verificar que el nombre se obtenga correctamente
            holder.bind(student);
        }

        @Override
        public int getItemCount() {
            return studentsList.size();
        }

        public static class StudentViewHolder extends RecyclerView.ViewHolder {
            private final TextView studentNameTextView;

            public StudentViewHolder(View itemView) {
                super(itemView);
                studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            }

            public void bind(Users student) {
                studentNameTextView.setText(student.getNombre());
            }
        }
    }
}
