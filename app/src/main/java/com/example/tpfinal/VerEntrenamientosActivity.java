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
    private Entrenamiento entrenamiento;
    private ConexionEntrenamientos conEntrenamientos;
    private TextView etNombreEntrenamiento;

    public ListView lvEntrenamientoSeleccionado;
    public List<ConfiguracionEjercicio> configEjerciciosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_seleccionado);

        entrenamiento = getIntent().getParcelableExtra("entrenamiento");

        etNombreEntrenamiento = findViewById(R.id.txtNombreEntrenamiento);

        etNombreEntrenamiento.setText(entrenamiento.getNombre());

        btnVolver = findViewById(R.id.btnVolverEntrenamiento);
        btnDarBaja = findViewById(R.id.btnBajaEntrenamiento);
        btnEditarEntrenamiento = findViewById(R.id.btnEditarEntrenamiento);

        lvEntrenamientoSeleccionado = findViewById(R.id.ejerciciosEntrenamiento);

        conEntrenamientos = new ConexionEntrenamientos(VerEntrenamientosActivity.this);
        conEntrenamientos.getEjerciciosConfiguracionPropios(entrenamiento.getId());

        entrenamiento.setConfiguracionesEjercicio(configEjerciciosList);

        btnEditarEntrenamiento.setOnClickListener(v -> {

            Intent intent = new Intent(VerEntrenamientosActivity.this, ActivityEditarEntrenamiento.class);
            intent.putExtra("entrenamiento", entrenamiento);
            startActivity(intent);

        });


        btnVolver.setOnClickListener(v-> {
            finish();
        });

        // HACER LOGICA DE ELEMINAR

        btnDarBaja.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja este entrenamiento? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                conEntrenamientos.eliminarEntrenamiento(entrenamiento.getId());
                finish();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss(); // Cierra el modal sin realizar ninguna acción
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }




}
