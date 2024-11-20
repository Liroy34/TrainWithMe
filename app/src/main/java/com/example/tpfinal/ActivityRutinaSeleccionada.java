package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;

import java.util.ArrayList;

public class ActivityRutinaSeleccionada extends AppCompatActivity {

    public ListView lvEjerciciosRutina;
    public ArrayList<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();
    private ConfiguracionEjercicioAdapter adapter;

    private ImageButton btnVolverRutinaPropiaSeleccionada;
    private Button btnEditarRutinaPropiaseleccionada;
    private Button btnBajaRutinaSeleccionada;
    private ConexionRutinas conRutinas;
    private TextView nombreRutina;
    private TextView frecuenciaRutina;
    int idRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_seleccionada);

       idRutina = getIntent().getIntExtra("idRutina", -1);

        String rutinaname = getIntent().getStringExtra("nombreRutina");
        int rutinaFrecuencia = getIntent().getIntExtra("rutinaFrecuencia", -1);
        String descripcionRutina = getIntent().getStringExtra("descripcionRutina");


        nombreRutina = findViewById(R.id.txtNombreRutinaSeleccionada);
        frecuenciaRutina = findViewById(R.id.txtFrecuenciaRutinaSeleccionada);

        lvEjerciciosRutina = findViewById(R.id.ejerciciosRutinaList);

        nombreRutina.setText(rutinaname);
        frecuenciaRutina.setText("Frecuencia: " + rutinaFrecuencia);


        conRutinas = new ConexionRutinas(ActivityRutinaSeleccionada.this, lvEjerciciosRutina);
        conRutinas.getEjerciciosConfiguracionPropios(idRutina);

        btnVolverRutinaPropiaSeleccionada = findViewById(R.id.btnVolverRutinaSeleccionada);
        btnEditarRutinaPropiaseleccionada = findViewById(R.id.btnEditarRutina);
        btnBajaRutinaSeleccionada = findViewById(R.id.btnBajaRutina);

        btnVolverRutinaPropiaSeleccionada.setOnClickListener(v -> {

            finish();

        });

        btnEditarRutinaPropiaseleccionada.setOnClickListener(v -> {

            Intent intent = new Intent(ActivityRutinaSeleccionada.this, ActivityEditarRutina.class);

            intent.putExtra("idRutina", getIntent().getIntExtra("idRutina",-1));

            intent.putExtra("rutinaFrecuencia", rutinaFrecuencia);
            intent.putExtra("descripcionRutina", descripcionRutina);
            intent.putExtra("nombreRutina", rutinaname);

            intent.putParcelableArrayListExtra("listaConfig", configuracionEjercicioList);


            ActivityRutinaSeleccionada.this.startActivity(intent);



        });

        btnBajaRutinaSeleccionada.setOnClickListener(v -> {

            conRutinas = new ConexionRutinas(ActivityRutinaSeleccionada.this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja esta rutina? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                conRutinas.eliminarRutina(getIntent().getIntExtra("idRutina" , -1));
                finish();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                conRutinas.getEjerciciosConfiguracionPropios(idRutina);
            }
        }, 2000);
    }
}
