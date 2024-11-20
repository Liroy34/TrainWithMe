package com.example.tpfinal.Conexion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tpfinal.ActivityEntrenamientos;
import com.example.tpfinal.ActivityRutinaSeleccionada;
import com.example.tpfinal.ActivityRutinasPropias;
import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Adapters.EntrenamientoAdapter;
import com.example.tpfinal.Adapters.RutinasAdapter;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.Entidades.Rutina;
import com.example.tpfinal.Entidades.RutinaCargaDatos;
import com.example.tpfinal.VerEntrenamientosActivity;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConexionEntrenamientos {

    private Context context;
    private ListView lvEntrenamiento, lvEjercicios;

    public ConexionEntrenamientos(Context context) {
        this.context = context;
    }

    public ConexionEntrenamientos(Context context, ListView lvEntrenamiento) {
        this.context = context;
        this.lvEntrenamiento = lvEntrenamiento;
    }

    public void getEntrenamientosPropios(int idUsuario) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Entrenamiento> entrenamientosList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = "SELECT * FROM Entrenamientos WHERE ID_Usuario = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idUsuario);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    Entrenamiento entrenamiento = new Entrenamiento();

                    entrenamiento.setDuracion(rs.getString("Duracion"));
                    entrenamiento.setFecha(rs.getString("Fecha"));
                    entrenamiento.setNombre(rs.getString("Nombre"));
                    entrenamiento.setId(rs.getInt("ID"));
                    entrenamiento.setIdUsuario(rs.getInt("ID_Usuario"));

                    entrenamientosList.add(entrenamiento);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                ((ActivityEntrenamientos) context).entrenamientosList.clear();
                ((ActivityEntrenamientos) context).entrenamientosList.addAll(entrenamientosList);

                EntrenamientoAdapter adapter = new EntrenamientoAdapter(context, entrenamientosList);
                ((ActivityEntrenamientos) context).lvEntrenamientos.setAdapter(adapter);
            });
        });

    }

    public void getEjerciciosConfiguracionPropios(int idEntrenamiento) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT c.ID, c.NombreEjercicio, c.Series, c.Repeticiones " +
                                "FROM ConfiguracionEjercicio c " +
                                "INNER JOIN EntrenamientoXEjercicio ex " +
                                "ON c.ID = ex.ID_ConfigEjercicio WHERE ex.ID_Entrenamiento = ?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, idEntrenamiento);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    ConfiguracionEjercicio configuracionEjercicio = new ConfiguracionEjercicio();

                    configuracionEjercicio.setId(rs.getInt("ID"));
                    configuracionEjercicio.setRepeticiones(rs.getInt("Repeticiones"));
                    configuracionEjercicio.setSeries(rs.getInt("Series"));
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
                ((VerEntrenamientosActivity) context).configEjerciciosList.clear(); // Limpiar lista existente
                ((VerEntrenamientosActivity) context).configEjerciciosList.addAll(configuracionEjercicioList); // Actualizar lista global

                ConfiguracionEjercicioAdapter adapter = new ConfiguracionEjercicioAdapter(context, configuracionEjercicioList);
                ((VerEntrenamientosActivity) context).lvEntrenamientoSeleccionado.setAdapter(adapter);
            });



        });
    }

    public void insertEntrenamiento(Entrenamiento entrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sqlEntrenamiento = "INSERT INTO Entrenamientos (ID_Usuario, Duracion, Fecha, Nombre) VALUES (?, ?, ?, ?)";
                PreparedStatement psEntrenamiento = con.prepareStatement(sqlEntrenamiento, Statement.RETURN_GENERATED_KEYS);
                psEntrenamiento.setInt(1, entrenamiento.getIdUsuario());
                psEntrenamiento.setString(2, entrenamiento.getDuracion());
                psEntrenamiento.setString(3, entrenamiento.getFecha());
                psEntrenamiento.setString(4, entrenamiento.getNombre());

                int rowsAffectedRutina = psEntrenamiento.executeUpdate();

                if (rowsAffectedRutina > 0) {
                    ResultSet generatedKeys = psEntrenamiento.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int entrenamientoId = generatedKeys.getInt(1);

                        for (ConfiguracionEjercicio ejercicio : entrenamiento.getConfiguracionesEjercicio()) {

                            String sqlConfigEjercicio = "INSERT INTO ConfiguracionEjercicio (NombreEjercicio, Series, Repeticiones, Tiempo) VALUES (?, ?, ?, ?)";
                            PreparedStatement psConfigEjercicio = con.prepareStatement(sqlConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                            psConfigEjercicio.setString(1, ejercicio.getEjercicio());
                            psConfigEjercicio.setInt(2, ejercicio.getSeries());
                            psConfigEjercicio.setInt(3, ejercicio.getRepeticiones());
                            psConfigEjercicio.setString(4, "0");

                            int rowsAffectedConfigEjercicio = psConfigEjercicio.executeUpdate();

                            if (rowsAffectedConfigEjercicio > 0) {
                                ResultSet generatedKeysConfig = psConfigEjercicio.getGeneratedKeys();
                                if (generatedKeysConfig.next()) {
                                    int configEjercicioId = generatedKeysConfig.getInt(1);

                                    String sqlEntrenamientoXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                                    PreparedStatement psRutinaXEjercicio = con.prepareStatement(sqlEntrenamientoXEjercicio);
                                    psRutinaXEjercicio.setInt(1, entrenamientoId);
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

                psEntrenamiento.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedRutina > 0) {
                        Toast.makeText(context, "Entrenamiento registrado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al registrar entrenamiento", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateEntrenamiento4(Entrenamiento entrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                // Actualizar la información de la tabla Entrenamientos
                String sqlUpdateEntrenamiento = "UPDATE Entrenamientos SET Nombre = ?, Duracion = ?, Fecha = ? WHERE ID = ?";
                PreparedStatement psUpdateEntrenamiento = con.prepareStatement(sqlUpdateEntrenamiento);
                psUpdateEntrenamiento.setString(1, entrenamiento.getNombre());
                psUpdateEntrenamiento.setString(2, entrenamiento.getDuracion());
                psUpdateEntrenamiento.setString(3, entrenamiento.getFecha());
                psUpdateEntrenamiento.setInt(4, entrenamiento.getId());
                int rowsAffectedEntrenamiento = psUpdateEntrenamiento.executeUpdate();

                // Eliminar las configuraciones relacionadas en EntrenamientoXEjercicio y ConfiguracionEjercicio
                String sqlDeleteEntrenamientoXEjercicio = "DELETE FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement psDeleteEntrenamientoXEjercicio = con.prepareStatement(sqlDeleteEntrenamientoXEjercicio);
                psDeleteEntrenamientoXEjercicio.setInt(1, entrenamiento.getId());
                psDeleteEntrenamientoXEjercicio.executeUpdate();

                String sqlDeleteConfigEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID IN (SELECT ID_ConfigEjercicio FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?)";
                PreparedStatement psDeleteConfigEjercicio = con.prepareStatement(sqlDeleteConfigEjercicio);
                psDeleteConfigEjercicio.setInt(1, entrenamiento.getId());
                psDeleteConfigEjercicio.executeUpdate();

                // Insertar nuevas configuraciones y relaciones
                for (ConfiguracionEjercicio ejercicio : entrenamiento.getConfiguracionesEjercicio()) {
                    String sqlInsertConfigEjercicio = "INSERT INTO ConfiguracionEjercicio (NombreEjercicio, Series, Repeticiones) VALUES (?, ?, ?)";
                    PreparedStatement psInsertConfigEjercicio = con.prepareStatement(sqlInsertConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                    psInsertConfigEjercicio.setString(1, ejercicio.getEjercicio());
                    psInsertConfigEjercicio.setInt(2, ejercicio.getSeries());
                    psInsertConfigEjercicio.setInt(3, ejercicio.getRepeticiones());


                    int rowsInsertedConfigEjercicio = psInsertConfigEjercicio.executeUpdate();

                    if (rowsInsertedConfigEjercicio > 0) {
                        ResultSet generatedKeys = psInsertConfigEjercicio.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int configEjercicioId = generatedKeys.getInt(1);

                            String sqlInsertEntrenamientoXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                            PreparedStatement psInsertEntrenamientoXEjercicio = con.prepareStatement(sqlInsertEntrenamientoXEjercicio);
                            psInsertEntrenamientoXEjercicio.setInt(1, entrenamiento.getId());
                            psInsertEntrenamientoXEjercicio.setInt(2, configEjercicioId);
                            psInsertEntrenamientoXEjercicio.executeUpdate();
                            psInsertEntrenamientoXEjercicio.close();
                        }
                        generatedKeys.close();
                    }
                    psInsertConfigEjercicio.close();
                }

                psUpdateEntrenamiento.close();
                psDeleteEntrenamientoXEjercicio.close();
                psDeleteConfigEjercicio.close();
                con.close();

                // Notificar al usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedEntrenamiento > 0) {
                        Toast.makeText(context, "Entrenamiento actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar entrenamiento", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    public void updateEntrenamiento(Entrenamiento entrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sqlUpdateEntrenamiento = "UPDATE Entrenamientos SET Nombre = ?, Duracion = ?, Fecha = ? WHERE ID = ?";
                PreparedStatement psUpdateRutina = con.prepareStatement(sqlUpdateEntrenamiento);
                psUpdateRutina.setString(1, entrenamiento.getNombre());
                psUpdateRutina.setString(2, entrenamiento.getDuracion());
                psUpdateRutina.setString(3, entrenamiento.getFecha());
                psUpdateRutina.setInt(4, entrenamiento.getId());

                int rowsAffectedRutina = psUpdateRutina.executeUpdate();

                String sqlDeleteEntrenamientoXEjercicio = "DELETE FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement psDeleteRutinaXEjercicio = con.prepareStatement(sqlDeleteEntrenamientoXEjercicio);
                psDeleteRutinaXEjercicio.setInt(1, entrenamiento.getId());
                psDeleteRutinaXEjercicio.executeUpdate();
                psDeleteRutinaXEjercicio.close();

                String sqlDeleteConfigEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID IN (SELECT ID_ConfigEjercicio FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?)";
                PreparedStatement psDeleteConfigEjercicio = con.prepareStatement(sqlDeleteConfigEjercicio);
                psDeleteConfigEjercicio.setInt(1, entrenamiento.getId());
                psDeleteConfigEjercicio.executeUpdate();
                psDeleteConfigEjercicio.close();

                for (ConfiguracionEjercicio ejercicio : entrenamiento.getConfiguracionesEjercicio()) {

                    String sqlConfigEjercicio = "INSERT INTO ConfiguracionEjercicio (NombreEjercicio, Series, Repeticiones) VALUES (?, ?, ?)";
                    PreparedStatement psConfigEjercicio = con.prepareStatement(sqlConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                    psConfigEjercicio.setString(1, ejercicio.getEjercicio());
                    psConfigEjercicio.setInt(2, ejercicio.getSeries());
                    psConfigEjercicio.setInt(3, ejercicio.getRepeticiones());

                    int rowsAffectedConfigEjercicio = psConfigEjercicio.executeUpdate();

                    if (rowsAffectedConfigEjercicio > 0) {
                        ResultSet generatedKeysConfig = psConfigEjercicio.getGeneratedKeys();
                        if (generatedKeysConfig.next()) {
                            int configEjercicioId = generatedKeysConfig.getInt(1);

                            String sqlEntrenamientoXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                            PreparedStatement psEntrenamientoXEjercicio = con.prepareStatement(sqlEntrenamientoXEjercicio);
                            psEntrenamientoXEjercicio.setInt(1, entrenamiento.getId());
                            psEntrenamientoXEjercicio.setInt(2, configEjercicioId);

                            psEntrenamientoXEjercicio.executeUpdate();
                            psEntrenamientoXEjercicio.close();
                        }
                        generatedKeysConfig.close();
                    }
                    psConfigEjercicio.close();
                }

                psUpdateRutina.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedRutina > 0) {
                        Toast.makeText(context, "Entrenamiento actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar entrenamiento", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateEntrenamiento2(Entrenamiento entrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                // Actualizar la información de la tabla Entrenamientos
                String sqlUpdateEntrenamiento = "UPDATE Entrenamientos SET Nombre = ?, Duracion = ?, Fecha = ? WHERE ID = ?";
                PreparedStatement psUpdateRutina = con.prepareStatement(sqlUpdateEntrenamiento);
                psUpdateRutina.setString(1, entrenamiento.getNombre());
                psUpdateRutina.setString(2, entrenamiento.getDuracion());
                psUpdateRutina.setString(3, entrenamiento.getFecha());
                psUpdateRutina.setInt(4, entrenamiento.getId());
                int rowsAffectedRutina = psUpdateRutina.executeUpdate();

                // Eliminar configuraciones relacionadas en ConfiguracionEjercicio
                String sqlDeleteConfigEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID IN (SELECT ID_ConfigEjercicio FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?)";
                PreparedStatement psDeleteConfigEjercicio = con.prepareStatement(sqlDeleteConfigEjercicio);
                psDeleteConfigEjercicio.setInt(1, entrenamiento.getId());
                psDeleteConfigEjercicio.executeUpdate();
                psDeleteConfigEjercicio.close();

                // Eliminar relaciones de EntrenamientoXEjercicio
                String sqlDeleteEntrenamientoXEjercicio = "DELETE FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement psDeleteRutinaXEjercicio = con.prepareStatement(sqlDeleteEntrenamientoXEjercicio);
                psDeleteRutinaXEjercicio.setInt(1, entrenamiento.getId());
                psDeleteRutinaXEjercicio.executeUpdate();
                psDeleteRutinaXEjercicio.close();

                // Insertar nuevas configuraciones y relaciones
                for (ConfiguracionEjercicio ejercicio : entrenamiento.getConfiguracionesEjercicio()) {
                    String sqlConfigEjercicio = "INSERT INTO ConfiguracionEjercicio (NombreEjercicio, Series, Repeticiones) VALUES (?, ?, ?)";
                    PreparedStatement psConfigEjercicio = con.prepareStatement(sqlConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                    psConfigEjercicio.setString(1, ejercicio.getEjercicio());
                    psConfigEjercicio.setInt(2, ejercicio.getSeries());
                    psConfigEjercicio.setInt(3, ejercicio.getRepeticiones());

                    int rowsAffectedConfigEjercicio = psConfigEjercicio.executeUpdate();

                    if (rowsAffectedConfigEjercicio > 0) {
                        ResultSet generatedKeysConfig = psConfigEjercicio.getGeneratedKeys();
                        if (generatedKeysConfig.next()) {
                            int configEjercicioId = generatedKeysConfig.getInt(1);

                            String sqlEntrenamientoXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                            PreparedStatement psRutinaXEjercicio = con.prepareStatement(sqlEntrenamientoXEjercicio);
                            psRutinaXEjercicio.setInt(1, entrenamiento.getId());
                            psRutinaXEjercicio.setInt(2, configEjercicioId);
                            psRutinaXEjercicio.executeUpdate();
                            psRutinaXEjercicio.close();
                        }
                        generatedKeysConfig.close();
                    }
                    psConfigEjercicio.close();
                }

                psUpdateRutina.close();
                con.close();

                // Notificar al usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedRutina > 0) {
                        Toast.makeText(context, "Entrenamiento actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar entrenamiento", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateEntrenamiento3(Entrenamiento entrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Cargar el controlador y establecer conexión
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                // Iniciar transacción
                con.setAutoCommit(false);

                // Actualizar la información de la tabla Entrenamientos
                String sqlUpdateEntrenamiento = "UPDATE Entrenamientos SET Nombre = ?, Duracion = ?, Fecha = ? WHERE ID = ?";
                PreparedStatement psUpdateEntrenamiento = con.prepareStatement(sqlUpdateEntrenamiento);
                psUpdateEntrenamiento.setString(1, entrenamiento.getNombre());
                psUpdateEntrenamiento.setString(2, entrenamiento.getDuracion());
                psUpdateEntrenamiento.setString(3, entrenamiento.getFecha());
                psUpdateEntrenamiento.setInt(4, entrenamiento.getId());
                int rowsAffectedEntrenamiento = psUpdateEntrenamiento.executeUpdate();

                // Eliminar configuraciones relacionadas en ConfiguracionEjercicio
                String sqlDeleteConfigEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID IN (SELECT ID_ConfigEjercicio FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?)";
                PreparedStatement psDeleteConfigEjercicio = con.prepareStatement(sqlDeleteConfigEjercicio);
                psDeleteConfigEjercicio.setInt(1, entrenamiento.getId());
                psDeleteConfigEjercicio.executeUpdate();
                psDeleteConfigEjercicio.close();

                // Eliminar relaciones de EntrenamientoXEjercicio
                String sqlDeleteEntrenamientoXEjercicio = "DELETE FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement psDeleteEntrenamientoXEjercicio = con.prepareStatement(sqlDeleteEntrenamientoXEjercicio);
                psDeleteEntrenamientoXEjercicio.setInt(1, entrenamiento.getId());
                psDeleteEntrenamientoXEjercicio.executeUpdate();
                psDeleteEntrenamientoXEjercicio.close();

                // Insertar nuevas configuraciones y relaciones
                for (ConfiguracionEjercicio ejercicio : entrenamiento.getConfiguracionesEjercicio()) {
                    // Insertar configuración de ejercicio
                    String sqlConfigEjercicio = "INSERT INTO ConfiguracionEjercicio (NombreEjercicio, Series, Repeticiones) VALUES (?, ?, ?)";
                    PreparedStatement psConfigEjercicio = con.prepareStatement(sqlConfigEjercicio, Statement.RETURN_GENERATED_KEYS);
                    psConfigEjercicio.setString(1, ejercicio.getEjercicio());
                    psConfigEjercicio.setInt(2, ejercicio.getSeries());
                    psConfigEjercicio.setInt(3, ejercicio.getRepeticiones());

                    int rowsAffectedConfigEjercicio = psConfigEjercicio.executeUpdate();

                    if (rowsAffectedConfigEjercicio > 0) {
                        ResultSet generatedKeysConfig = psConfigEjercicio.getGeneratedKeys();
                        if (generatedKeysConfig.next()) {
                            int configEjercicioId = generatedKeysConfig.getInt(1);

                            // Insertar relación en EntrenamientoXEjercicio
                            String sqlEntrenamientoXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                            PreparedStatement psEntrenamientoXEjercicio = con.prepareStatement(sqlEntrenamientoXEjercicio);
                            psEntrenamientoXEjercicio.setInt(1, entrenamiento.getId());
                            psEntrenamientoXEjercicio.setInt(2, configEjercicioId);
                            psEntrenamientoXEjercicio.executeUpdate();
                            psEntrenamientoXEjercicio.close();
                        }
                        generatedKeysConfig.close();
                    }
                    psConfigEjercicio.close();
                }

                // Confirmar transacción
                con.commit();

                // Cerrar recursos
                psUpdateEntrenamiento.close();
                con.close();

                // Notificar al usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedEntrenamiento > 0) {
                        Toast.makeText(context, "Entrenamiento actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar entrenamiento", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void eliminarEntrenamiento(int idEntrenamiento) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean eliminado = false;

            try {

                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String deleteEntrenamientoXEjercicio = "DELETE FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement stmt2 = con.prepareStatement(deleteEntrenamientoXEjercicio);
                stmt2.setInt(1, idEntrenamiento);
                int filasEliminadas2 = stmt2.executeUpdate();


                String obtenerIDConfigEjercicio = "SELECT DISTINCT ID_ConfigEjercicio FROM EntrenamientoXEjercicio WHERE ID_Entrenamiento = ?";
                PreparedStatement stmtObtenerConfigEjercicio = con.prepareStatement(obtenerIDConfigEjercicio);
                stmtObtenerConfigEjercicio.setInt(1, idEntrenamiento);
                ResultSet rs = stmtObtenerConfigEjercicio.executeQuery();

                List<Integer> idsConfigEjercicio = new ArrayList<>();
                while (rs.next()) {
                    idsConfigEjercicio.add(rs.getInt("ID_ConfigEjercicio"));
                }

                if (!idsConfigEjercicio.isEmpty()) {
                    String deleteConfiguracionEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID_ConfigEjercicio = ?";
                    PreparedStatement stmt1 = con.prepareStatement(deleteConfiguracionEjercicio);
                    for (int idConfig : idsConfigEjercicio) {
                        stmt1.setInt(1, idConfig);
                        stmt1.addBatch();
                    }
                    stmt1.executeBatch();
                    stmt1.close();
                }

                String deleteRutina = "DELETE FROM Entrenamientos WHERE ID = ?";
                PreparedStatement stmt4 = con.prepareStatement(deleteRutina);
                stmt4.setInt(1, idEntrenamiento);
                int filasEliminadas4 = stmt4.executeUpdate();

                eliminado = (filasEliminadas2 > 0 ||  filasEliminadas4 > 0);

                // Cerrar las conexiones
                stmtObtenerConfigEjercicio.close();
                stmt2.close();
                stmt4.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Notificar el resultado en el hilo principal
            boolean finalEliminado = eliminado;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                if (finalEliminado) {
                    Toast.makeText(context, "Entrenamiento eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar entrenamiento", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}
