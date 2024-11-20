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

                                    String sqlRutinaXEjercicio = "INSERT INTO EntrenamientoXEjercicio (ID_Entrenamiento, ID_ConfigEjercicio) VALUES (?, ?)";
                                    PreparedStatement psRutinaXEjercicio = con.prepareStatement(sqlRutinaXEjercicio);
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


}
