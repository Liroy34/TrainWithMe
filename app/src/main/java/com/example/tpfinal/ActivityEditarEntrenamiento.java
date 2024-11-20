package com.example.tpfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionEntrenamientos;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Entrenamiento;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditarEntrenamiento extends AppCompatActivity {

    private EditText etNombre, etDuracion, etFecha;
    private List<EditText> etEjercicios, etSeries, etRepeticiones;
    private Button btnEditarEditar;
    private ImageButton btnVolverEditarEntrenamiento;
    private ConexionEntrenamientos conEntrenamiento;
    private int idUsuario = -1;
    private Entrenamiento entrenamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_entrenamiento);

        entrenamiento = getIntent().getParcelableExtra("entrenamiento");

        btnVolverEditarEntrenamiento = findViewById(R.id.btnVolverEditarEntrenamiento);
        btnEditarEditar = findViewById(R.id.btnEditarEditarEntrenamiento);

        conEntrenamiento = new ConexionEntrenamientos(ActivityEditarEntrenamiento.this);

        setElementos();
        loadElementos();

        btnEditarEditar.setOnClickListener(v -> {

            if (!checkValues()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else if(!isValidDateFormat(etFecha.getText().toString())){
                Toast.makeText(this, "Formato de fecha invalido - dd-mm-aaaa", Toast.LENGTH_SHORT).show();
            }else {

                conEntrenamiento.updateEntrenamiento(crearEntrenamiento());
                finish();

            }
        });


        btnVolverEditarEntrenamiento.setOnClickListener(v -> {

            finish();

        });


    }

    private void loadElementos(){

        etNombre.setText(entrenamiento.getNombre());
        etDuracion.setText(entrenamiento.getDuracion());
        etFecha.setText(String.valueOf(entrenamiento.getFecha()));

        for (int i = 0; i < entrenamiento.getConfiguracionesEjercicio().size(); i++) {
            ConfiguracionEjercicio config = entrenamiento.getConfiguracionesEjercicio().get(i);

            etEjercicios.get(i).setText(config.getEjercicio());
            etSeries.get(i).setText(String.valueOf(config.getSeries()));
            etRepeticiones.get(i).setText(String.valueOf(config.getRepeticiones()));
        }

    }

    private void setElementos() {

        etNombre = findViewById(R.id.etNombreEntrenamientoEditar);
        etDuracion = findViewById(R.id.etDuracionEntrenamientoEditar);
        etFecha = findViewById(R.id.etFechaEntrenamientoEditar);

        int[] ejerciciosIds = {R.id.txtEntrenamientoEditar1, R.id.txtEntrenamientoEditar2, R.id.txtEntrenamientoEditar3, R.id.txtEntrenamientoEditar4, R.id.txtEntrenamientoEditar5, R.id.txtEntrenamientoEditar6, R.id.txtEntrenamientoEditar7, R.id.txtEntrenamientoEditar8};
        int[] seriesIds = {R.id.txtTSEntrenamientoEditar1, R.id.txtTSEntrenamientoEditar2, R.id.txtTSEntrenamientoEditar3, R.id.txtTSEntrenamientoEditar4, R.id.txtTSEntrenamientoEditar5, R.id.txtTSEntrenamientoEditar6, R.id.txtTSEntrenamientoEditar7, R.id.txtTSEntrenamientoEditar8};
        int[] repeticionesIds = {R.id.txtTREntrenamientoEditar1, R.id.txtTREntrenamientoEditar2, R.id.txtTREntrenamientoEditar3, R.id.txtTREntrenamientoEditar4, R.id.txtTREntrenamientoEditar5, R.id.txtTREntrenamientoEditar6, R.id.txtTREntrenamientoEditar7, R.id.txtTREntrenamientoEditar8};

        etEjercicios = findEditTextsByIds(ejerciciosIds);
        etSeries = findEditTextsByIds(seriesIds);
        etRepeticiones = findEditTextsByIds(repeticionesIds);
    }

    private List<EditText> findEditTextsByIds(int[] ids) {
        List<EditText> editTexts = new ArrayList<>();
        for (int id : ids) {
            editTexts.add(findViewById(id));
        }
        return editTexts;
    }

    private boolean checkValues() {

        if (etNombre.getText().toString().trim().isEmpty() ||
                etDuracion.getText().toString().trim().isEmpty() ||
                etFecha.getText().toString().trim().isEmpty()) {
            return false;
        }

        for (int i = 0; i < etEjercicios.size(); i++) {
            if (!etEjercicios.get(i).getText().toString().trim().isEmpty() &&
                    (etSeries.get(i).getText().toString().trim().isEmpty() ||
                            etRepeticiones.get(i).getText().toString().trim().isEmpty())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDateFormat(String date) {
        String datePattern = "^([0-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$";
        return date.matches(datePattern);
    }

    private Entrenamiento crearEntrenamiento() {

        String nombre = etNombre.getText().toString().trim();
        String duracion = etDuracion.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        List<ConfiguracionEjercicio> ejercicios = new ArrayList<>();
        for (int i = 0; i < etEjercicios.size(); i++) {
            String nombreEjercicio = etEjercicios.get(i).getText().toString().trim();
            if (!nombreEjercicio.isEmpty()) {
                int series = Integer.parseInt(etSeries.get(i).getText().toString().trim());
                int repeticiones = Integer.parseInt(etRepeticiones.get(i).getText().toString().trim());
                ejercicios.add(new ConfiguracionEjercicio(nombreEjercicio, series, repeticiones));
            }
        }

        return new Entrenamiento(entrenamiento.getId(), idUsuario, duracion, fecha, nombre, ejercicios);
    }


}
