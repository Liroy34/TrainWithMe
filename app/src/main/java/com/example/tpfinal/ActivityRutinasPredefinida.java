package com.example.tpfinal;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Conexion.ConexionUsuario;

public class ActivityRutinasPredefinida extends AppCompatActivity {

    private Button btnPrincipiante, btnMedio, btnAvanzado;
    private ImageButton btnVolverRutinaPredefinida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_predefinida);

        btnPrincipiante = findViewById(R.id.btnNivelPrincipiante);
        btnMedio = findViewById(R.id.btnNivelMedio);
        btnAvanzado = findViewById(R.id.btnNivelAvanzado);
        btnVolverRutinaPredefinida = findViewById(R.id.btnVolverRutinaPredefinida);

        btnPrincipiante.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRutinasPredefinida.this, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "principiante");
            ActivityRutinasPredefinida.this.startActivity(intent);
        });

        btnMedio.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRutinasPredefinida.this, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "medio");
            ActivityRutinasPredefinida.this.startActivity(intent);
        });

        btnAvanzado.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRutinasPredefinida.this, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "avanzado");
            ActivityRutinasPredefinida.this.startActivity(intent);
        });



        btnVolverRutinaPredefinida.setOnClickListener(v -> {

            finish();

        });

    }
}
