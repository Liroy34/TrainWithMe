package com.example.tpfinal;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;

public class ActivityRutinasPredefinidaSeleccionada extends AppCompatActivity {

    private String tipo;
    private ConexionRutinas conRutinas;
    private ImageButton btnVolverRutinaPredefSelect;
    private ListView ejerciciosLV;
    private TextView rutinaName;
    private TextView rutinaFrecuancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_predefinida_seleccionada);

        btnVolverRutinaPredefSelect = findViewById(R.id.btnVolverRutinaPredefinidaSeleccionada);

        Intent intentAnterior = getIntent();
        tipo = intentAnterior.getStringExtra("tipo");
        rutinaName = findViewById(R.id.txtNombreRutinaPredefinidaSeleccionada);
        rutinaFrecuancia = findViewById(R.id.txtFrecuenciaRutinaPredefinidaSeleccionada);

        rutinaFrecuancia.setText("Frecuencia: 3 x Semana");


        ejerciciosLV = findViewById(R.id.ejerciciosRutinaPredefinidaSeleccionadaList);

        rutinaName.setText(tipo);
        conRutinas = new ConexionRutinas(ActivityRutinasPredefinidaSeleccionada.this, ejerciciosLV);
        conRutinas.getEjerciciosConfiguracionPredefinidos(tipo);

        btnVolverRutinaPredefSelect.setOnClickListener(v -> {

            finish();

        });

    }

}
