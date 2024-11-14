package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;

import java.util.ArrayList;

public class ActivityRutinaSeleccionada extends AppCompatActivity {

    private ListView ejerciciosRutinaList;
    private ArrayList<ConfiguracionEjercicio> configuracionEjercicioList;
    private ConfiguracionEjercicioAdapter adapter;

    private ImageButton btnVolverRutinaPropiaSeleccionada;
    private Button btnEditarRutinaPropiaseleccionada;
    private Button btnBajaRutinaSeleccionada;
    private Context context;
    private ConexionRutinas conRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_seleccionada);

        ejerciciosRutinaList = findViewById(R.id.ejerciciosRutinaList);

        configuracionEjercicioList = getIntent().getParcelableArrayListExtra("configuracionEjercicioList");

        adapter = new ConfiguracionEjercicioAdapter(this, configuracionEjercicioList);
        ejerciciosRutinaList.setAdapter(adapter);

        btnVolverRutinaPropiaSeleccionada = findViewById(R.id.btnVolverRutinaSeleccionada);
        btnEditarRutinaPropiaseleccionada = findViewById(R.id.btnEditarRutina);
        btnBajaRutinaSeleccionada = findViewById(R.id.btnBajaRutina);

        btnVolverRutinaPropiaSeleccionada.setOnClickListener(v -> {

            finish();

        });

        btnEditarRutinaPropiaseleccionada.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityEditarRutina.class);
            intent.putExtra("idRutina", getIntent().getIntExtra("idRutina",-1));
            context.startActivity(intent);

        });

        btnBajaRutinaSeleccionada.setOnClickListener(v -> {

            conRutinas = new ConexionRutinas(context);

            conRutinas.eliminarRutina(getIntent().getIntExtra("idRutina" , -1));

            finish();

        });
    }
}
