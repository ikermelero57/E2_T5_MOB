package com.example.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.e2_t5_mob.R;
import model.Reuniones;

import java.util.ArrayList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReunionesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Reuniones> reunionesList;

    // Método para convertir Timestamp a String
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "Fecha no disponible";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(timestamp);
    }

    public ReunionesAdapter(Context context, ArrayList<Reuniones> reunionesList) {
        this.context = context;
        this.reunionesList = reunionesList;
    }

    @Override
    public int getCount() {
        return reunionesList.size();
    }

    @Override
    public Object getItem(int position) {
        return reunionesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reunion, parent, false);
        }

        Reuniones reunion = reunionesList.get(position);

        TextView fechaTextView = convertView.findViewById(R.id.fechaTextView);
        TextView horaTextView = convertView.findViewById(R.id.horaTextView);
        TextView asuntoTextView = convertView.findViewById(R.id.asuntoTextView);

        fechaTextView.setText(formatTimestamp(reunion.getFecha()));
        horaTextView.setText(reunion.getHora());
        asuntoTextView.setText(reunion.getAsunto());

        return convertView;
    }
}
