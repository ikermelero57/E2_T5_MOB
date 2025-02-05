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
                dos.flush();

                // Leer respuesta del servidor
                boolean loginCredentialsOk = dis.readBoolean();

                if (loginCredentialsOk) {
                    try {
                        // Deserializar el objeto 'Users'
                        Users registeredUser = (Users) dis.readObject();
                        callback.onSuccess(registeredUser);
                    } catch (ClassNotFoundException e) {
                        Log.e("nirelog", e.getMessage());
                        callback.onError(new Exception("Error de deserialización: " + e.getMessage()));
                    }
                } else {
                    callback.onError(new Exception("Credenciales inválidas."));
                }

            } catch (UnknownHostException e) {
                callback.onError(new Exception("Host desconocido: " + e.getMessage()));
            } catch (IOException e) {
                callback.onError(new Exception("Error de conexión: " + e.getMessage()));
            }
        }).start();
    }

    public static void requestPasswordReset(String email, ServerResponse<String> callback) {
        new Thread(() -> {
            try (Socket client = new Socket(HOST, PORT);
                 DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                 ObjectInputStream dis = new ObjectInputStream(client.getInputStream())) {

                dos.writeUTF("pasahitzaAldatu");
                dos.writeUTF(email);
                dos.flush();

                String response = dis.readUTF();

                if ("OK".equals(response)) {
                    callback.onSuccess("Contraseña enviada");
                } else {
                    callback.onError(new Exception("Error en el servidor"));
                }

            } catch (IOException e) {
                callback.onError(new Exception("Error de conexión: " + e.getMessage()));
            }
        }).start();
    }

}
