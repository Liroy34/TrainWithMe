package com.example.tpfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionEntrenamientos;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Entrenamiento;

import java.util.ArrayList;
import java.util.List;

public class VerEntrenamientosActivity extends AppCompatActivity {

    private ImageButton btnVolver;
    private Button btnDarBaja, btnEditarEntrenamiento;
    private Entrenamiento entrenamiento;
    private ConexionEntrenamientos conEntrenamientos;

    public ListView lvEntrenamientoSeleccionado;
    public List<ConfiguracionEjercicio> configEjerciciosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_seleccionado);

        entrenamiento = getIntent().getParcelableExtra("entrenamiento");

        btnVolver = findViewById(R.id.btnVolverEntrenamiento);
        btnDarBaja = findViewById(R.id.btnBajaEntrenamiento);
        btnEditarEntrenamiento = findViewById(R.id.btnEditarEntrenamiento);

        lvEntrenamientoSeleccionado.findViewById(R.id.ejerciciosEntrenamiento);

        conEntrenamientos = new ConexionEntrenamientos(VerEntrenamientosActivity.this);
        conEntrenamientos.getEjerciciosConfiguracionPropios(entrenamiento.getId());



        btnVolver.setOnClickListener(v-> {
            finish();
        });

        // HACER LOGICA DE ELEMINAR

//        btnDarBaja.setOnClickListener(v -> {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Confirmar acción");
//            builder.setMessage("¿Estás seguro de que deseas dar de baja este entrenamiento? Esta acción no se puede deshacer.");
//
//            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
//                conEntrenamientos.eliminarRutina(getIntent().getIntExtra("idRutina" , -1));
//                finish();
//            });
//
//            builder.setNegativeButton("Cancelar", (dialog, which) -> {
//                dialog.dismiss(); // Cierra el modal sin realizar ninguna acción
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        });
    }




}
