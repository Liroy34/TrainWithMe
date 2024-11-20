package com.example.tpfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionEntrenamientos;
import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Entidades.ConfiguracionEjercicio;
import com.example.tpfinal.Entidades.Entrenamiento;
import com.example.tpfinal.Entidades.RutinaCargaDatos;


import java.util.ArrayList;
import java.util.List;

public class RegistrarEntrenamientoActivity extends AppCompatActivity {

    private EditText etNombre, etDuracion, etFecha;
    private List<EditText> etEjercicios, etSeries, etRepeticiones;
    private Button btnCrearEntrenamiento;
    private ImageButton btnVolverEntrenamiento;
    private ConexionEntrenamientos conEntrenamiento;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_entrenamiento);

        idUsuario = getIntent().getIntExtra("idUsuario",-1);

        conEntrenamiento = new ConexionEntrenamientos(RegistrarEntrenamientoActivity.this);

        btnVolverEntrenamiento = findViewById(R.id.btnVolverCrearEntrenamiento);
        btnCrearEntrenamiento = findViewById(R.id.btnCrearEntrenamiento);

        setElementos();

        btnCrearEntrenamiento.setOnClickListener(v -> {

            if (!checkValues()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else if(!isValidDateFormat(etFecha.getText().toString())){
                Toast.makeText(this, "Formato de fecha invalido - dd-mm-aaaa", Toast.LENGTH_SHORT).show();
            }else {

                conEntrenamiento.insertEntrenamiento(crearEntrenamiento());
                finish();

            }
        });

        btnVolverEntrenamiento.setOnClickListener(v -> {

            finish();

        });
    }


    private void setElementos() {

        etNombre = findViewById(R.id.etNombreEntrenamientoCrear);
        etDuracion = findViewById(R.id.etDuracionEntrenamientoCrear);
        etFecha = findViewById(R.id.etFechaEntrenamientoCrear);

        int[] ejerciciosIds = {R.id.txtEntrenamiento1, R.id.txtEntrenamiento2, R.id.txtEntrenamiento3, R.id.txtEntrenamiento4, R.id.txtEntrenamiento5, R.id.txtEntrenamiento6, R.id.txtEntrenamiento7, R.id.txtEntrenamiento8};
        int[] seriesIds = {R.id.txtTSEntrenamiento1, R.id.txtTSEntrenamiento2, R.id.txtTSEntrenamiento3, R.id.txtTSEntrenamiento4, R.id.txtTSEntrenamiento5, R.id.txtTSEntrenamiento6, R.id.txtTSEntrenamiento7, R.id.txtTSEntrenamiento8};
        int[] repeticionesIds = {R.id.txtTREntrenamiento1, R.id.txtTREntrenamiento2, R.id.txtTREntrenamiento3, R.id.txtTREntrenamiento4, R.id.txtTREntrenamiento5, R.id.txtTREntrenamiento6, R.id.txtTREntrenamiento7, R.id.txtTREntrenamiento8};

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

        return new Entrenamiento(idUsuario, duracion, fecha, nombre, ejercicios);
    }

}
