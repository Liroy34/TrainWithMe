package com.example.tpfinal.Conexion;

import android.content.Context;

import com.example.tpfinal.Entidades.Estadistica;

import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConexionEstadistica {

    private Context context;

    public ConexionEstadistica(Context context) {
        this.context = context;
    }


    public void traerEstadisticas(EstadisticaCallback estadisticaCallback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            int cantidadUsuarios = 0;
            int cantidadEntrenamientos = 0;
            int cantidadRutinas = 0;
            int cantidadMinutosEntrenados = 0;

            try {
                Class.forName(DataBD.driver);
                Connection con = DriverManager.getConnection(DataBD.urlMySQL, DataBD.user, DataBD.pass);

                String sqlUsuarios = "SELECT COUNT(*) AS cantidad_usuarios FROM Usuarios";
                PreparedStatement psUsuarios = con.prepareStatement(sqlUsuarios);
                ResultSet rsUsuarios = psUsuarios.executeQuery();
                if (rsUsuarios.next()) {
                    cantidadUsuarios = rsUsuarios.getInt("cantidad_usuarios");
                }
                rsUsuarios.close();
                psUsuarios.close();

                String sqlEntrenamientos = "SELECT COUNT(*) AS cantidad_entrenamientos FROM Entrenamientos";
                PreparedStatement psEntrenamientos = con.prepareStatement(sqlEntrenamientos);
                ResultSet rsEntrenamientos = psEntrenamientos.executeQuery();
                if (rsEntrenamientos.next()) {
                    cantidadEntrenamientos = rsEntrenamientos.getInt("cantidad_entrenamientos");
                }
                rsEntrenamientos.close();
                psEntrenamientos.close();

                String sqlRutinas = "SELECT COUNT(*) AS cantidad_rutinas FROM Rutinas WHERE Tipo = 'Propia'";
                PreparedStatement psRutinas = con.prepareStatement(sqlRutinas);
                ResultSet rsRutinas = psRutinas.executeQuery();
                if (rsRutinas.next()) {
                    cantidadRutinas = rsRutinas.getInt("cantidad_rutinas");
                }
                rsRutinas.close();
                psRutinas.close();

                String sqlDuracion = "SELECT SUM(Duracion) AS suma_duracion_entrenamientos FROM Entrenamientos";
                PreparedStatement psDuracion = con.prepareStatement(sqlDuracion);
                ResultSet rsDuracion = psDuracion.executeQuery();
                if (rsDuracion.next()) {
                    cantidadMinutosEntrenados = rsDuracion.getInt("suma_duracion_entrenamientos");
                }
                rsDuracion.close();
                psDuracion.close();

                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Estadistica estadistica = new Estadistica(
                    cantidadUsuarios,
                    cantidadEntrenamientos,
                    cantidadRutinas,
                    cantidadMinutosEntrenados
            );

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                estadisticaCallback.onEstadisticaReceived(estadistica);
            });
        });
    }

    public interface EstadisticaCallback {
        void onEstadisticaReceived(Estadistica estadistica);
    }


}
