package com.example.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.e2_t5_mob.R;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import model.Reuniones;
import model.Users;

public class BilerakActivity extends AppCompatActivity {

    private ListView reunionesListView;
    private ReunionesAdapter reunionesAdapter;
    private ArrayList<Reuniones> reunionesList;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilerak);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_three_points_white));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.bilerak_title);
        }

        reunionesListView = findViewById(R.id.reunionesListView);
        reunionesList = new ArrayList<>();
        reunionesAdapter = new ReunionesAdapter(this, reunionesList);
        reunionesListView.setAdapter(reunionesAdapter);

        user = (Users) getIntent().getSerializableExtra("userData");

        if (user != null) {
            String role = user.getTipos().getName();  // Obtiene el rol del usuario
            getReuniones(user.getEmail(), role);
        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void getReuniones(String email, String role) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("10.5.104.21", 54321);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                if (role.equalsIgnoreCase("profesor")) {
                    outputStream.writeUTF("getReunionesByTeachersEmail");
                } else {
                    outputStream.writeUTF("getReunionesByAlumnoEmail");
                }

                outputStream.writeUTF(email);
                outputStream.flush();

                Object response = inputStream.readObject();

                if (response instanceof ArrayList) {
                    ArrayList<Reuniones> reunionesServidor = (ArrayList<Reuniones>) response;
                    runOnUiThread(() -> {
                        reunionesList.clear();
                        reunionesList.addAll(reunionesServidor);
                        reunionesAdapter.notifyDataSetChanged();
                    });
                } else {
                    Log.e("ERROR", "La respuesta del servidor no es válida.");
                }

                socket.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error al obtener reuniones", e);
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bilerak, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemCerrarSesion) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.itemIkasleOrdutegia) {
            if (user != null) {
                Intent intent = new Intent(this, IkasleActivity.class);
                intent.putExtra("userData", user);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
