package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;

public class ActivityRutinasPredefinidaSeleccionada extends AppCompatActivity {

    private String tipo;
    ConexionRutinas conRutinas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_predefinida_seleccionada);

        Intent intentAnterior = getIntent();
        tipo = intentAnterior.getStringExtra("tipo");

        conRutinas.getEjerciciosConfiguracionPredefinidos(tipo, new ConexionRutinas.RutinaCallback() {
            @Override
            public void onComplete(Object result) {
                // poner logica de adapter a la lista
            }
        });

    }

}
