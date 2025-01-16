package com.example.e2_t5_mob;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerConnection {

    private static final String HOST = "10.5.104.21"; // IP del servidor
    private static final int PORT = 3307; // Puerto del servidor

    public interface ServerResponseListener {
        void onResponse(String[][] response);
        void onError(String error);
    }

    // Método para conectarse al servidor y obtener los datos
    public static void connectToServer(int requestCode, ServerResponseListener listener) {
        new Thread(() -> {
            String[][] matriz = null;
            try (Socket socket = new Socket(HOST, PORT);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 ObjectInputStream dis = new ObjectInputStream(socket.getInputStream())) {

                // Enviar el código de solicitud
                dos.writeInt(requestCode);

                // Leer la respuesta del servidor
                matriz = (String[][]) dis.readObject();

                // Enviar la respuesta al hilo principal
                if (listener != null) {
                    listener.onResponse(matriz);
                }

            } catch (IOException | ClassNotFoundException e) {
                Log.e("ServerConnection", "Error: " + e.getMessage());
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        }).start();
    }
}
