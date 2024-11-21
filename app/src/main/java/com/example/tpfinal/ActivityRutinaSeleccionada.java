package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.RutinaCargaDatos;

import java.util.ArrayList;

public class ActivityRutinaSeleccionada extends AppCompatActivity {

    private ListView lvEjerciciosRutina;
    private ArrayList<ConfiguracionEjercicio> configuracionEjercicioList = new ArrayList<>();
    private ConfiguracionEjercicioAdapter adapter;

    private ImageButton btnVolverRutinaPropiaSeleccionada;
    private Button btnEditarRutinaPropiaseleccionada;
    private Button btnBajaRutinaSeleccionada;
    private ConexionRutinas conRutinas;
    private TextView nombreRutina;
    private TextView frecuenciaRutina;

    private int idRutina;
    private RutinaCargaDatos rutinaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_seleccionada);

        idRutina = getIntent().getIntExtra("idRutina", -1);

        nombreRutina = findViewById(R.id.txtNombreRutinaSeleccionada);
        frecuenciaRutina = findViewById(R.id.txtFrecuenciaRutinaSeleccionada);
        lvEjerciciosRutina = findViewById(R.id.ejerciciosRutinaList);

        btnVolverRutinaPropiaSeleccionada = findViewById(R.id.btnVolverRutinaSeleccionada);
        btnEditarRutinaPropiaseleccionada = findViewById(R.id.btnEditarRutina);
        btnBajaRutinaSeleccionada = findViewById(R.id.btnBajaRutina);

        conRutinas = new ConexionRutinas(ActivityRutinaSeleccionada.this);
        adapter = new ConfiguracionEjercicioAdapter(this, configuracionEjercicioList);
        lvEjerciciosRutina.setAdapter(adapter);

        cargarDatosRutina();

        btnVolverRutinaPropiaSeleccionada.setOnClickListener(v -> finish());

        btnEditarRutinaPropiaseleccionada.setOnClickListener(v -> {
            if (rutinaFinal != null) {
                Intent intent = new Intent(ActivityRutinaSeleccionada.this, ActivityEditarRutina.class);
                intent.putExtra("idRutina", idRutina);
                intent.putExtra("frecu", Integer.valueOf(rutinaFinal.getFrecuencia()));
                intent.putExtra("descripcionRutina", rutinaFinal.getDescripcion());
                intent.putExtra("nombreRutina", rutinaFinal.getNombre());
                intent.putParcelableArrayListExtra("listaConfig", configuracionEjercicioList);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Los datos de la rutina aún no están disponibles.", Toast.LENGTH_SHORT).show();
            }
        });

        btnBajaRutinaSeleccionada.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja esta rutina? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                conRutinas.eliminarRutina(idRutina);
                finish();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void cargarDatosRutina() {

        conRutinas.getEjerciciosConfiguracionPropios(idRutina, new ConexionRutinas.RutinaCompletaCallback() {
            @Override
            public void onRutinaCompletaRecived(RutinaCargaDatos rutinaCompleta) {
                if (rutinaCompleta != null) {
                    rutinaFinal = rutinaCompleta;


                    configuracionEjercicioList.clear();
                    configuracionEjercicioList.addAll(rutinaFinal.getEjercicios());
                    adapter.notifyDataSetChanged();

                    nombreRutina.setText(rutinaFinal.getNombre());
                    frecuenciaRutina.setText("Frecuencia: " + rutinaFinal.getFrecuencia());
                } else {
                    Toast.makeText(ActivityRutinaSeleccionada.this, "No se encontraron datos de la rutina.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                cargarDatosRutina();
            }
        }, 2000);

    }
}
