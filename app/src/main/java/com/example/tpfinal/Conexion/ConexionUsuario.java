package com.example.tpfinal.Conexion;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.tpfinal.Entidades.Usuario;
import com.example.tpfinal.MainActivity;

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
            int idUsuario = -1;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);


                String sql = ("SELECT * FROM Usuarios WHERE NombreUsuario = ? AND Contrasena = ?");
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, nombreUsuario);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    autenticado = true;
                    idUsuario = rs.getInt("ID");
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalAutenticado = autenticado;
            int finalId= idUsuario;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (finalAutenticado) {
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("idUsuario", finalId);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
                callback.onIniciarSesion(finalAutenticado,finalId);
            });
        });
    }

    public void deleteUsuario(int idUsuario) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "DELETE FROM Usuarios WHERE ID = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idUsuario);

                int rowsAffected = preparedStatement.executeUpdate();

                preparedStatement.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffected > 0) {
                        Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Error en la base de datos", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }


    public void updateUsuario(Usuario usuario) {
        usuarioExiste(usuario.getNombreUsuario(), existe -> {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    Class.forName(DataBD.driver);
                    Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                    // MODIFICAR ESTO PARA USUARIO
                    String sql = ("UPDATE Usuarios SET Nombre = ?, Apellido = ?, Genero = ?, Email = ? WHERE ID = ?");
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, usuario.getNombre());
                    preparedStatement.setString(2, usuario.getApellido());
                    preparedStatement.setString(3, usuario.getGenero());
                    preparedStatement.setString(4, usuario.getMail());
                    preparedStatement.setInt(5, usuario.getId());

                    int rowsAffected = preparedStatement.executeUpdate();

                    preparedStatement.close();
                    con.close();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (rowsAffected > 0) {
                            Toast.makeText(context, "Usuario editado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al editar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        String sql = ("INSERT INTO Usuarios (Nombre, Apellido, Genero, Email, Celular, NombreUsuario, Contrasena) VALUES (?, ?, ?, ?, ?, ?, ?)");
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
                        Log.e("ActivityRegistrarse", "Error al insertar usuario", e);
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void fetchRecuperarPass(String mail, UsuarioCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Usuario usuario = null;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String query = ("Select * FROM Usuarios WHERE Email = ?");
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, mail);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setPassword(rs.getString("Contrasena"));

                }

                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Usuario usuarioFinal = usuario;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onUsuarioReceived(usuarioFinal);
            });
        });
    }


    private void usuarioExiste(String nombreUsuario, UsuarioExistenteCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean existe = false;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = ("SELECT * FROM Usuarios WHERE NombreUsuario = ?");
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

    public void traerUsuario (int idUsuario, UsuarioCallback usuarioCallback){
        ExecutorService executor = Executors.newSingleThreadExecutor();


        executor.execute(() -> {
            boolean existe = false;
            Usuario usuario = new Usuario();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = ("SELECT * FROM Usuarios WHERE ID = ?");
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idUsuario);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {

                    usuario.setId(rs.getInt("ID"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setMail(rs.getString("Email"));
                    usuario.setGenero(rs.getString("Genero"));
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Usuario usuarioFinal = usuario;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                usuarioCallback.onUsuarioReceived(usuarioFinal);
            });
        });
    }

    public interface UsuarioCallback {
        void onUsuarioReceived(Usuario usuario);
    }

    public interface IniciarSesionCallback {
        void onIniciarSesion(boolean autenticado, int idUsuario);
    }

    public interface UsuarioExistenteCallback {
        void onUsuarioExistente(boolean existe);
    }

}
