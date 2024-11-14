package com.example.tpfinal;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;

public class ActivityRutinasPredefinidaSeleccionada extends AppCompatActivity {

    private String tipo;
    ConexionRutinas conRutinas;
    ImageButton btnVolverRutinaPredefSelect;
    private ListView ejerciciosLV;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_predefinida_seleccionada);

        btnVolverRutinaPredefSelect = findViewById(R.id.btnVolverRutinaPredefinidaSeleccionada);

        Intent intentAnterior = getIntent();
        tipo = intentAnterior.getStringExtra("tipo");

        ejerciciosLV = findViewById(R.id.ejerciciosRutinaPredefinidaSeleccionadaList);
        conRutinas = new ConexionRutinas(context, ejerciciosLV);
        conRutinas.getEjerciciosConfiguracionPredefinidos(tipo);

        btnVolverRutinaPredefSelect.setOnClickListener(v -> {

            finish();

        });

    }

}
