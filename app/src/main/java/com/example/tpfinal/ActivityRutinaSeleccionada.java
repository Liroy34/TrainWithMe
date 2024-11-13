package com.example.tpfinal;

import android.os.Bundle;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;

import java.util.ArrayList;

public class ActivityRutinaSeleccionada extends AppCompatActivity {

    private ListView ejerciciosRutinaList;
    private ArrayList<ConfiguracionEjercicio> configuracionEjercicioList;
    private ConfiguracionEjercicioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_seleccionada);

        ejerciciosRutinaList = findViewById(R.id.ejerciciosRutinaList);

        configuracionEjercicioList = getIntent().getParcelableArrayListExtra("configuracionEjercicioList");

        adapter = new ConfiguracionEjercicioAdapter(this, configuracionEjercicioList);
        ejerciciosRutinaList.setAdapter(adapter);
    }
}
