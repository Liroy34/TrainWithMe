package com.example.tpfinal;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityEntrenamientos extends AppCompatActivity {

    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamientos);

        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        if (idUsuario == -1) {
            // Manejo de errores si no se recibió el idUsuario
            finish();
            return;
        }

        // Asegúrate de que el nombre del layout coincida

        // Configuración del botón para volver
        ImageButton btnVolver = findViewById(R.id.btnVolverEntrenamiento);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra esta actividad, regresando a la anterior en la pila de actividades
                finish();
            }
        });

        // Configuración del botón para ver entrenamientos
        Button btnVerEntrenamientos = findViewById(R.id.btnVerMisEntrenamientos);
        btnVerEntrenamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia una actividad donde se listan los entrenamientos
                Intent intent = new Intent(ActivityEntrenamientos.this, VerEntrenamientosActivity.class);
                intent.putExtra("idUsuario", idUsuario);

                startActivity(intent);
            }
        });

        // Configuración del botón para registrar un nuevo entrenamiento
        Button btnRegistrarEntrenamiento = findViewById(R.id.btnMostrarEntrenamientoLista);
        btnRegistrarEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia una actividad para registrar un nuevo entrenamiento
                Intent intent = new Intent(ActivityEntrenamientos.this, RegistrarEntrenamientoActivity.class);
                intent.putExtra("idUsuario", idUsuario);

                startActivity(intent);
            }
        });
    }
}
