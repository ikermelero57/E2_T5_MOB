package com.example.model;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Users;

public class ServerConnection {

    private static final String HOST = "10.5.104.21"; // Dirección IP del servidor
    private static final int PORT = 54321;           // Puerto del servidor

    // Interfaz para manejar las respuestas del servidor
    public interface ServerResponse<T> {
        void onSuccess(T response);
        void onError(Exception e);
    }

    // Método para login
    public static void login(String email, String password, ServerResponse<Users> callback) {
        new Thread(() -> {
            try (Socket client = new Socket(HOST, PORT);
                 DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                 ObjectInputStream dis = new ObjectInputStream(client.getInputStream())) {

                // Enviar la acción y las credenciales
                dos.writeUTF("login");
                dos.writeUTF(email);
                dos.writeUTF(password);

                // Leer respuesta del servidor
                Boolean loginCredentialsOk = dis.readBoolean();

                if (loginCredentialsOk) {
                    try {
                        Log.e("nirelog", "aaa");

                        // Deserializar el objeto 'Users'
                        Users registeredUser = (Users) dis.readObject();
                        callback.onSuccess(registeredUser);

                    } catch (ClassNotFoundException e) {
                        Log.e("nirelog", e.getMessage());
                        // Error de deserialización: se captura la excepción
                        callback.onError(new Exception("Error de deserialización: " + e.getMessage()));
                    }
                } else {
                    // Si las credenciales son incorrectas
                    callback.onError(new Exception("Credenciales inválidas."));
                }

            } catch (UnknownHostException e) {
                // Error de conexión: host desconocido
                callback.onError(new Exception("Host desconocido: " + e.getMessage()));
            } catch (IOException e) {
                // Error de conexión general
                callback.onError(new Exception("Error de conexión: " + e.getMessage()));
            }
        }).start();
    }
}
