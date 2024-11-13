package com.example.tpfinal.Conexion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Ejercicio;
import com.example.tpfinal.Entidades.Rutina;
import com.example.tpfinal.Entidades.Usuario;

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

    // ESTE ES PARA CARGAR LA LISTA DE RUTINAS, UNICAMENTE CON NOMBRE DE LA RUTINA
    public void getRutinasPropias(String idUsuario, RutinaCallback callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Rutina> rutinaList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);
                String sql = "SELECT r.* FROM Rutinas r JOIN RutinaXUsuario rxu ON r.ID = rxu.ID_Rutina WHERE rxu.ID_Usuario = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, idUsuario);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    Rutina rutina = new Rutina();

                    rutina.setTipo(rs.getString("tipo"));
                    rutina.setDescripcion(rs.getString("descripcion"));
                    rutina.setId(rs.getString("id"));
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
                //RutinasAdapter adapter = new RutinasAdapter(context, rutinaList);
                //gvRutinas.setAdapter(adapter);
            });
        });

    }

    public void getEjerciciosConfiguracionPropios(String idRutina, RutinaCallback callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT r.* FROM Rutinas r JOIN RutinaXUsuario rxu ON r.ID = rxu.ID_Rutina WHERE rxu.ID_Usuario = ?";
                //HACER QUERY QUE TRAIGA LOS EJERCICIOS DE LA RUTINA CON SU CONFIGURACION POR ID DE RUINA

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, idRutina);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    ConfiguracionEjercicio configuracionEjercicio = new ConfiguracionEjercicio();

                    configuracionEjercicio.setId(rs.getString("id"));
                    configuracionEjercicio.setRepeticiones(rs.getInt("repeticiones"));
                    configuracionEjercicio.setSeries(rs.getInt("series"));
                    configuracionEjercicio.setTiempo(rs.getString("tiempo"));
                    configuracionEjercicio.setEjercicio(new Ejercicio(null,rs.getString("nombreEjercicio")));

                    configuracionEjercicioList.add(configuracionEjercicio);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // CREAR ADAPTER DE CONFIGURACION DE EJERCICIOS
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                //ConfiguracionAdapter adapter = new ConfiguracionAdapter(context, configuracionEjercicioList);
                //lvEntrenamiento.setAdapter(adapter);
            });
        });

    }

    public void getEjerciciosConfiguracionPredefinidos(String tipo, RutinaCallback callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sql = "SELECT r.* FROM Rutinas r JOIN RutinaXUsuario rxu ON r.ID = rxu.ID_Rutina WHERE rxu.ID_Usuario = ?";
                //HACER QUERY QUE TRAIGA LOS EJERCICIOS DE LA RUTINA CON SU CONFIGURACION POR TIPO (DIFICULTAD)

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, tipo);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    ConfiguracionEjercicio configuracionEjercicio = new ConfiguracionEjercicio();

                    configuracionEjercicio.setId(rs.getString("id"));
                    configuracionEjercicio.setRepeticiones(rs.getInt("repeticiones"));
                    configuracionEjercicio.setSeries(rs.getInt("series"));
                    configuracionEjercicio.setTiempo(rs.getString("tiempo"));
                    configuracionEjercicio.setEjercicio(new Ejercicio(null,rs.getString("nombreEjercicio")));

                    configuracionEjercicioList.add(configuracionEjercicio);
                }

                rs.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // CREAR ADAPTER DE CONFIGURACION DE EJERCICIOS
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                //ConfiguracionAdapter adapter = new ConfiguracionAdapter(context, configuracionEjercicioList);
                //lvEntrenamiento.setAdapter(adapter);
            });
        });

    }

    public interface RutinaCallback {
        void onRutinaRecived(Rutina rutina);
    }

}
