package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionEstadistica;
import com.example.tpfinal.Entidades.Estadistica;

public class ActivityVerEstadisticas extends AppCompatActivity {

    private ImageButton btnVolverEstadisticas;
    private TextView txtUsuarios, txtEntrenamientos, txtRutinas, txtDuracion;
    private ConexionEstadistica conEstadistica;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        btnVolverEstadisticas = findViewById(R.id.btnVolverEstadisticas);
        txtUsuarios = findViewById(R.id.tvCantidadUsuarios);
        txtEntrenamientos = findViewById(R.id.tvCantidadEntrenamientos);
        txtRutinas = findViewById(R.id.tvCantidadRutinas);
        txtDuracion = findViewById(R.id.tvCantidadMinutos);

        conEstadistica = new ConexionEstadistica(ActivityVerEstadisticas.this);

        conEstadistica.traerEstadisticas(new ConexionEstadistica.EstadisticaCallback() {
            @Override
            public void onEstadisticaReceived(Estadistica estadistica) {

                txtUsuarios.setText(String.valueOf(estadistica.getCantidadUsuarios()));
                txtEntrenamientos.setText(String.valueOf(estadistica.getCantidadEntrenamientos()));
                txtRutinas.setText(String.valueOf(estadistica.getCantidadRutinas()));
                txtDuracion.setText(String.valueOf(estadistica.getCantidadMinutosEntrenados()));

            }
        });

        btnVolverEstadisticas.setOnClickListener(v -> {

            finish();

        });
    }

}
