package com.example.tpfinal.Conexion;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.tpfinal.Entidades.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConexionUsuario {

    private Context context;

    public ConexionUsuario(Context context) {
        this.context = context;
    }

    public void iniciarSesion(String nombreUsuario, String password, IniciarSesionCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean autenticado = false;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);


                String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND password = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, nombreUsuario);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();

                autenticado = rs.next();

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalAutenticado = autenticado;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (finalAutenticado) {
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, /*activity_paginaprincipal.class*/null); // ACOMODAR CUANDO SE CREE EL ANTIVITY
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
                callback.onIniciarSesion(finalAutenticado);
            });
        });
    }

    public void insertUsuario(Usuario usuario) {
        usuarioExiste(usuario.getNombreUsuario(), existe -> {
            if (existe) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Nombre de usuario en uso", Toast.LENGTH_SHORT).show();
                });
            } else {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        Class.forName(DataBD.driver);
                        Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                        // MODIFICAR ESTO PARA USUARIO
                        String sql = "INSERT INTO usuarios (nombre, apellido, genero, mail, cel, nombre_usuario, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, usuario.getNombre());
                        preparedStatement.setString(2, usuario.getApellido());
                        preparedStatement.setString(3, usuario.getGenero());
                        preparedStatement.setString(4, usuario.getMail());
                        preparedStatement.setString(5, usuario.getCel());
                        preparedStatement.setString(6, usuario.getNombreUsuario());
                        preparedStatement.setString(7, usuario.getPassword());

                        int rowsAffected = preparedStatement.executeUpdate();

                        preparedStatement.close();
                        con.close();

                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (rowsAffected > 0) {
                                Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private void usuarioExiste(String nombreUsuario, UsuarioExistenteCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean existe = false;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, nombreUsuario);
                ResultSet rs = preparedStatement.executeQuery();

                existe = rs.next();

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalExiste = existe;
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onUsuarioExistente(finalExiste);
            });
        });
    }

    public interface IniciarSesionCallback {
        void onIniciarSesion(boolean autenticado);
    }

    public interface UsuarioExistenteCallback {
        void onUsuarioExistente(boolean existe);
    }

}