package com.example.tpfinal.Conexion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tpfinal.ActivityRutinaSeleccionada;
import com.example.tpfinal.ActivityRutinasPropias;
import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Adapters.RutinasAdapter;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Ejercicio;
import com.example.tpfinal.Entidades.Rutina;
import com.example.tpfinal.Entidades.RutinaCargaDatos;
import com.example.tpfinal.Entidades.Usuario;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConexionRutinas {

    private ListView lvRutinas;
    private Context context;
    private ListView lvEntrenamiento;

    public ConexionRutinas(Context context) {
        this.context = context;
    }

    public ConexionRutinas(ListView lvRutinas, Context context) {
        this.lvRutinas = lvRutinas;
        this.context = context;
    }

    public ConexionRutinas(Context context, ListView lvEntrenamiento) {
        this.context = context;
        this.lvEntrenamiento = lvEntrenamiento;
    }

    public void insertRutina(RutinaCargaDatos rutina) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                // Insertar la rutina en la tabla Rutinas
                String sqlRutina = "INSERT INTO rutinas (nombre, descripcion, tipo) VALUES (?, ?, ?)";
                PreparedStatement psRutina = con.prepareStatement(sqlRutina, Statement.RETURN_GENERATED_KEYS);
                psRutina.setString(1, rutina.getNombre());
                psRutina.setString(2, rutina.getDescripcion());
                psRutina.setString(3, rutina.getFrecuencia());

                int rowsAffectedRutina = psRutina.executeUpdate();

                if (rowsAffectedRutina > 0) {
                    // Obtener el ID autogenerado de la rutina insertada
                    ResultSet generatedKeys = psRutina.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int rutinaId = generatedKeys.getInt(1);

                        // Insertar cada ConfiguracionEjercicio en la tabla ConfiguracionEjercicio y en la relación RutinaXEjercicio
                        for (ConfiguracionEjercicio ejercicio : rutina.getEjercicios()) {
                            // Insertar configuración de ejercicio en ConfiguracionEjercicio
                            String sqlConfigEjercicio = "INSERT INTO configuracionejercicio (id_ejercicio, series, repeticiones, tiempo) VALUES (?, ?, ?, ?)";
                            PreparedStatement psConfigEjercicio = con.prepareStatement(sqlConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                            psConfigEjercicio.setString(1, ejercicio.getEjercicio());
                            psConfigEjercicio.setInt(2, ejercicio.getSeries());
                            psConfigEjercicio.setInt(3, ejercicio.getRepeticiones());


                            int rowsAffectedConfigEjercicio = psConfigEjercicio.executeUpdate();

                            if (rowsAffectedConfigEjercicio > 0) {
                                ResultSet generatedKeysConfig = psConfigEjercicio.getGeneratedKeys();
                                if (generatedKeysConfig.next()) {
                                    int configEjercicioId = generatedKeysConfig.getInt(1);

                                    // Insertar la relación entre la rutina y la configuración de ejercicio en RutinaXEjercicio
                                    String sqlRutinaXEjercicio = "INSERT INTO rutinaxejercicio (id_rutina, id_configejercicio) VALUES (?, ?)";
                                    PreparedStatement psRutinaXEjercicio = con.prepareStatement(sqlRutinaXEjercicio);
                                    psRutinaXEjercicio.setInt(1, rutinaId);
                                    psRutinaXEjercicio.setInt(2, configEjercicioId);

                                    psRutinaXEjercicio.executeUpdate();
                                    psRutinaXEjercicio.close();
                                }
                                generatedKeysConfig.close();
                            }
                            psConfigEjercicio.close();
                        }
                    }
                    generatedKeys.close();
                }

                psRutina.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedRutina > 0) {
                        Toast.makeText(context, "Rutina creada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al crear la rutina", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




    // ESTE ES PARA CARGAR LA LISTA DE RUTINAS, UNICAMENTE CON NOMBRE DE LA RUTINA
    public void getRutinasPropias(int idUsuario) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Rutina> rutinaList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = "SELECT r.* FROM Rutinas r JOIN RutinaXUsuario rxu ON r.ID = rxu.ID_Rutina WHERE rxu.ID_Usuario = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idUsuario);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    Rutina rutina = new Rutina();

                    rutina.setTipo(rs.getString("tipo"));
                    rutina.setDescripcion(rs.getString("descripcion"));
                    rutina.setId(rs.getInt("id"));
                    rutina.setNombre(rs.getString("nombre"));

                    rutinaList.add(rutina);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // CREAR ADAPTER DE RUTINAS
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                ((ActivityRutinasPropias) context).rutinasList.clear(); // Limpiar lista existente
                ((ActivityRutinasPropias) context).rutinasList.addAll(rutinaList); // Actualizar lista global

                // Crear y configurar el adaptador
                RutinasAdapter adapter = new RutinasAdapter(context, rutinaList);
                ((ActivityRutinasPropias) context).lvRutinas.setAdapter(adapter);
            });
        });

    }

    public void getEjerciciosConfiguracionPropios(int idRutina) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT ce.*, e.Nombre AS NombreEjercicio FROM ConfiguracionEjercicio ce " +
                        "JOIN Ejercicios e ON ce.ID_Ejercicio = e.ID " +
                        "JOIN RutinaXEjercicio rxe ON ce.ID = rxe.ID_ConfigEjercicio " +
                        "JOIN Rutinas r ON rxe.ID_Rutina = r.ID " +
                        "WHERE r.ID = ?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idRutina);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    ConfiguracionEjercicio configuracionEjercicio = new ConfiguracionEjercicio();

                    configuracionEjercicio.setId(rs.getInt("id"));
                    configuracionEjercicio.setRepeticiones(rs.getInt("repeticiones"));
                    configuracionEjercicio.setSeries(rs.getInt("series"));
                    configuracionEjercicio.setEjercicio(rs.getString("NombreEjercicio"));

                    configuracionEjercicioList.add(configuracionEjercicio);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                ((ActivityRutinaSeleccionada) context).configuracionEjercicioList.clear(); // Limpiar lista existente
                ((ActivityRutinaSeleccionada) context).configuracionEjercicioList.addAll(configuracionEjercicioList); // Actualizar lista global

                // Crear y configurar el adaptador
                ConfiguracionEjercicioAdapter adapter = new ConfiguracionEjercicioAdapter(context, configuracionEjercicioList);
                ((ActivityRutinaSeleccionada) context).lvEjerciciosRutina.setAdapter(adapter);
            });



        });
    }

    public interface RutinaCallback<T> {
        void onComplete(T result);
    }

    public void getEjerciciosConfiguracionPredefinidos(String tipo) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT ce.*, e.Nombre AS NombreEjercicio FROM ConfiguracionEjercicio ce " +
                        "JOIN Ejercicios e ON ce.ID_Ejercicio = e.ID " +
                        "JOIN RutinaXEjercicio rxe ON ce.ID = rxe.ID_ConfigEjercicio " +
                        "JOIN Rutinas r ON rxe.ID_Rutina = r.ID WHERE r.Tipo = ?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, tipo);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    ConfiguracionEjercicio configuracionEjercicio = new ConfiguracionEjercicio();

                    configuracionEjercicio.setId(rs.getInt("id"));
                    configuracionEjercicio.setRepeticiones(rs.getInt("repeticiones"));
                    configuracionEjercicio.setSeries(rs.getInt("series"));
                    configuracionEjercicio.setEjercicio(rs.getString("NombreEjercicio"));

                    configuracionEjercicioList.add(configuracionEjercicio);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                ConfiguracionEjercicioAdapter adapter = new ConfiguracionEjercicioAdapter(context, configuracionEjercicioList);
                lvEntrenamiento.setAdapter(adapter);
            });
        });

    }


    public void eliminarRutina(int idRutina) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean eliminado = false;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);


                String sql = "DELETE FROM Rutinas WHERE ID = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idRutina);
                int filasAfectadas = preparedStatement.executeUpdate();

                // Verificar si se eliminó alguna fila
                eliminado = filasAfectadas > 0;

                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalEliminado = eliminado;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                if (finalEliminado) {
                    Toast.makeText(context, "Rutina eliminada correctamente", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Error al eliminar la rutina", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



}
