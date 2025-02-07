package com.example.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e2_t5_mob.R;

import java.util.ArrayList;

import model.Users;

public class StudentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Users> estudiantesList;

    public StudentAdapter(Context context, ArrayList<Users> estudiantesList) {
        this.context = context;
        this.estudiantesList = estudiantesList;
    }

    @Override
    public int getCount() {
        return estudiantesList.size();
    }

    @Override
    public Object getItem(int position) {
        return estudiantesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Usar el LayoutInflater para inflar el layout de cada elemento
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_student, parent, false);
        }

        // Obtener la referencia a los elementos del layout
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvApellidos = convertView.findViewById(R.id.tvApellidos);
        TextView tvTelefono = convertView.findViewById(R.id.tvTelefono);

        // Obtener el estudiante en la posición actual
        Users estudiante = estudiantesList.get(position);

        // Establecer los valores de los estudiantes en las vistas
        tvNombre.setText("Nombre:" + estudiante.getNombre());
        tvApellidos.setText("Apellido:" + estudiante.getApellidos());
        tvTelefono.setText("Telefono:" + String.valueOf(estudiante.getTelefono1())); // Suponiendo que quieres mostrar el primer teléfono

        return convertView;
    }
}
