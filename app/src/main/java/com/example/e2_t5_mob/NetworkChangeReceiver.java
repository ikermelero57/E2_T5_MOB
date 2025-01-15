package com.example.e2_t5_mob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            // Mostrar notificación de conexión restaurada
            Toast.makeText(context, "Conexión a Internet restaurada", Toast.LENGTH_SHORT).show();
        } else {
            // Mostrar notificación de conexión perdida
            Toast.makeText(context, "Conexión a Internet perdida", Toast.LENGTH_SHORT).show();
        }
    }
}
