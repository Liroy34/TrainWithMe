package com.example.tpfinal.Conexion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

    public void updateRutina(int rutinaId, RutinaCargaDatos rutina) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sqlUpdateRutina = "UPDATE Rutinas SET Nombre = ?, Descripcion = ?, Frecuencia = ? WHERE ID = ?";
                PreparedStatement psUpdateRutina = con.prepareStatement(sqlUpdateRutina);
                psUpdateRutina.setString(1, rutina.getNombre());
                psUpdateRutina.setString(2, rutina.getDescripcion());
                psUpdateRutina.setString(3, rutina.getFrecuencia());
                psUpdateRutina.setInt(4, rutinaId);

                int rowsAffectedRutina = psUpdateRutina.executeUpdate();

                String sqlDeleteRutinaXEjercicio = "DELETE FROM RutinaXEjercicio WHERE ID_Rutina = ?";
                PreparedStatement psDeleteRutinaXEjercicio = con.prepareStatement(sqlDeleteRutinaXEjercicio);
                psDeleteRutinaXEjercicio.setInt(1, rutinaId);
                psDeleteRutinaXEjercicio.executeUpdate();
                psDeleteRutinaXEjercicio.close();

                String sqlDeleteConfigEjercicio = "DELETE FROM ConfiguracionEjercicio WHERE ID IN (SELECT ID_ConfigEjercicio FROM RutinaXEjercicio WHERE ID_Rutina = ?)";
                PreparedStatement psDeleteConfigEjercicio = con.prepareStatement(sqlDeleteConfigEjercicio);
                psDeleteConfigEjercicio.setInt(1, rutinaId);
                psDeleteConfigEjercicio.executeUpdate();
                psDeleteConfigEjercicio.close();

                for (ConfiguracionEjercicio ejercicio : rutina.getEjercicios()) {

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

                            String sqlRutinaXEjercicio = "INSERT INTO RutinaXEjercicio (ID_Rutina, ID_ConfigEjercicio) VALUES (?, ?)";
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

                psUpdateRutina.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (rowsAffectedRutina > 0) {
                        Toast.makeText(context, "Rutina actualizada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al actualizar la rutina", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void insertRutina(RutinaCargaDatos rutina, int userId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sqlRutina = "INSERT INTO Rutinas (Nombre, Descripcion, Tipo, Frecuencia) VALUES (?, ?, ?, ?)";
                PreparedStatement psRutina = con.prepareStatement(sqlRutina, Statement.RETURN_GENERATED_KEYS);
                psRutina.setString(1, rutina.getNombre());
                psRutina.setString(2, rutina.getDescripcion());
                psRutina.setString(3, "Propia");
                psRutina.setString(4, rutina.getFrecuencia());

                int rowsAffectedRutina = psRutina.executeUpdate();

                if (rowsAffectedRutina > 0) {

                    ResultSet generatedKeys = psRutina.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int rutinaId = generatedKeys.getInt(1);

                        String sqlRutinaXUsuario = "INSERT INTO RutinaXUsuario (ID_Usuario, ID_Rutina) VALUES (?, ?)";
                        PreparedStatement psRutinaXUsuario = con.prepareStatement(sqlRutinaXUsuario);
                        psRutinaXUsuario.setInt(1, userId);
                        psRutinaXUsuario.setInt(2, rutinaId);
                        psRutinaXUsuario.executeUpdate();
                        psRutinaXUsuario.close();


                        for (ConfiguracionEjercicio ejercicio : rutina.getEjercicios()) {

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

                                    String sqlRutinaXEjercicio = "INSERT INTO RutinaXEjercicio (ID_Rutina, ID_ConfigEjercicio) VALUES (?, ?)";
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

                while (rs.next()) {
                    Rutina rutina = new Rutina();

                    rutina.setTipo(rs.getString("Tipo"));
                    rutina.setDescripcion(rs.getString("Descripcion"));
                    rutina.setId(rs.getInt("ID"));
                    rutina.setNombre(rs.getString("Nombre"));
                    rutina.setFrecuencia(rs.getInt("Frecuencia"));

                    rutinaList.add(rutina);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                ((ActivityRutinasPropias) context).rutinasList.clear();
                ((ActivityRutinasPropias) context).rutinasList.addAll(rutinaList);

                RutinasAdapter adapter = new RutinasAdapter(context, rutinaList);
                ((ActivityRutinasPropias) context).lvRutinas.setAdapter(adapter);
            });
        });

    }

    public void getEjerciciosConfiguracionPropios(int idRutina, RutinaCompletaCallback rutinaCompleta) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RutinaCargaDatos rutina = new RutinaCargaDatos();
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();
            String nombre = null;
            String descripcion = null;
            String frecuencia = null;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT ce.*, r.Nombre as NombreR, r.Descripcion as DescripcionR, r.Frecuencia as FrecuenciaR FROM ConfiguracionEjercicio ce " +
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

                    if(nombre == null){
                        nombre = rs.getString("NombreR");
                        descripcion = rs.getString("DescripcionR");
                        frecuencia = rs.getString("FrecuenciaR");
                    }
                }
                rutina.setDescripcion(descripcion);
                rutina.setFrecuencia(frecuencia);
                rutina.setNombre(nombre);
                rutina.setEjercicios(configuracionEjercicioList);

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            RutinaCargaDatos rutinaFinal = rutina;

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                rutinaCompleta.onRutinaCompletaRecived(rutinaFinal);
            });



        });
    }

    public void getEjerciciosConfiguracionPredefinidos(String tipo) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT ce.* FROM ConfiguracionEjercicio ce JOIN RutinaXEjercicio rxe ON ce.ID = rxe.ID_ConfigEjercicio JOIN Rutinas r ON rxe.ID_Rutina = r.ID WHERE r.Tipo = ?";

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, tipo);
                ResultSet rs = preparedStatement.executeQuery();

                Log.e("Resultado get", rs.toString());

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


                String deleteRutinaXEjercicio = "DELETE FROM RutinaXEjercicio WHERE ID_Rutina = ?";
                PreparedStatement stmt2 = con.prepareStatement(deleteRutinaXEjercicio);
                stmt2.setInt(1, idRutina);
                int filasEliminadas2 = stmt2.executeUpdate();

                String obtenerIDConfigEjercicio = "SELECT DISTINCT ID_ConfigEjercicio FROM RutinaXEjercicio WHERE ID_Rutina = ?";
                PreparedStatement stmtObtenerConfigEjercicio = con.prepareStatement(obtenerIDConfigEjercicio);
                stmtObtenerConfigEjercicio.setInt(1, idRutina);
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


                String deleteRutinaXUsuario = "DELETE FROM RutinaXUsuario WHERE ID_Rutina = ?";
                PreparedStatement stmt3 = con.prepareStatement(deleteRutinaXUsuario);
                stmt3.setInt(1, idRutina);
                int filasEliminadas3 = stmt3.executeUpdate();


                String deleteRutina = "DELETE FROM Rutinas WHERE ID = ?";
                PreparedStatement stmt4 = con.prepareStatement(deleteRutina);
                stmt4.setInt(1, idRutina);
                int filasEliminadas4 = stmt4.executeUpdate();


                eliminado = (filasEliminadas2 > 0 || filasEliminadas3 > 0 || filasEliminadas4 > 0);


                stmtObtenerConfigEjercicio.close();
                stmt2.close();
                stmt3.close();
                stmt4.close();
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

    public interface RutinaCompletaCallback {
        void onRutinaCompletaRecived(RutinaCargaDatos rutinaCompleta);
    }

}
