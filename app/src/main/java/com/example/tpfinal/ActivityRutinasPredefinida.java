package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionRutinas;
import com.example.tpfinal.Conexion.ConexionUsuario;

public class ActivityRutinasPredefinida extends AppCompatActivity {

    Button btnPrincipiante, btnMedio, btnAvanzado;
    ImageButton btnVolverRutinaPredefinida;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_predefinida);

        btnPrincipiante = findViewById(R.id.btnNivelPrincipiante);
        btnMedio = findViewById(R.id.btnNivelMedio);
        btnAvanzado = findViewById(R.id.btnNivelAvanzado);
        btnVolverRutinaPredefinida = findViewById(R.id.btnVolverRutinaPredefinida);

        btnPrincipiante.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "principiante");
            context.startActivity(intent);
        });

        btnMedio.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "medio");
            context.startActivity(intent);
        });

        btnAvanzado.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityRutinasPredefinidaSeleccionada.class);
            intent.putExtra("tipo", "avanzado");
            context.startActivity(intent);
        });



        btnVolverRutinaPredefinida.setOnClickListener(v -> {

            finish();

        });

    }
}
