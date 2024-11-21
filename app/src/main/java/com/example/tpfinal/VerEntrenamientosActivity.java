package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Adapters.ConfiguracionEjercicioAdapter;
import com.example.tpfinal.Adapters.EntrenamientoAdapter;
import com.example.tpfinal.Conexion.ConexionEntrenamientos;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.Entidades.RutinaCargaDatos;

import java.util.ArrayList;
import java.util.List;

public class VerEntrenamientosActivity extends AppCompatActivity {

    private ImageButton btnVolver;
    private Button btnDarBaja, btnEditarEntrenamiento;
    private int entrenamientoId;
    private ConexionEntrenamientos conEntrenamientos;
    private TextView etNombreEntrenamiento;
    private Entrenamiento entrenamientoFinal;
    private ListView lvEntrenamientoSeleccionado;
    private List<ConfiguracionEjercicio> configEjerciciosList = new ArrayList<>();
    private ConfiguracionEjercicioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_seleccionado);

        entrenamientoId = getIntent().getIntExtra("entrenamientoId", -1);
        etNombreEntrenamiento = findViewById(R.id.txtNombreEntrenamiento);

        btnVolver = findViewById(R.id.btnVolverEntrenamiento);
        btnDarBaja = findViewById(R.id.btnBajaEntrenamiento);
        btnEditarEntrenamiento = findViewById(R.id.btnEditarEntrenamiento);

        lvEntrenamientoSeleccionado = findViewById(R.id.ejerciciosEntrenamiento);

        conEntrenamientos = new ConexionEntrenamientos(VerEntrenamientosActivity.this);
        adapter = new ConfiguracionEjercicioAdapter(VerEntrenamientosActivity.this, configEjerciciosList);
        lvEntrenamientoSeleccionado.setAdapter(adapter);

        cargarDatosEntrenamiento();


        btnEditarEntrenamiento.setOnClickListener(v -> {

            Intent intent = new Intent(VerEntrenamientosActivity.this, ActivityEditarEntrenamiento.class);
            intent.putExtra("entrenamiento", entrenamientoFinal);
            startActivity(intent);

        });


        btnVolver.setOnClickListener(v-> {
            finish();
        });


        btnDarBaja.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja este entrenamiento? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                conEntrenamientos.eliminarEntrenamiento(entrenamientoId);
                finish();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void cargarDatosEntrenamiento() {

        conEntrenamientos.getEjerciciosConfiguracionPropios(entrenamientoId, new ConexionEntrenamientos.EntrenamientoCallBack() {
            @Override
            public void onEntrenamientoRecived(Entrenamiento entrenamiento) {
                if(entrenamiento!=null){
                    entrenamientoFinal = entrenamiento;
                    entrenamientoFinal.setId(entrenamientoId);

                    configEjerciciosList.clear();
                    configEjerciciosList.addAll(entrenamientoFinal.getConfiguracionesEjercicio());
                    adapter.notifyDataSetChanged();

                    etNombreEntrenamiento.setText(entrenamientoFinal.getNombre());
                }
                else{
                    Toast.makeText(VerEntrenamientosActivity.this, "No se encontraron datos del entrenamiento.", Toast.LENGTH_SHORT).show();
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

                cargarDatosEntrenamiento();
            }
        }, 2000);
    }


}
