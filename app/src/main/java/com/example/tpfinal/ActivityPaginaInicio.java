package com.example.tpfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPaginaInicio extends AppCompatActivity {

    ImageButton btnCerrarSesion;
    Button btnEntrenamientos, btnRutinasPropias, btnRutinasPredefinidas, btnVerPerfil;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_principal);

        Intent intentAnterior = getIntent();
        int idUsuario = intentAnterior.getIntExtra("idUsuario", -1);

        btnEntrenamientos = findViewById(R.id.btnMisEntrenamientos);
        btnRutinasPropias = findViewById(R.id.btnMisRutinas);
        btnRutinasPredefinidas = findViewById(R.id.btnRutinasPredefinidas);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarPaginaPrincipal);

        btnRutinasPropias.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityRutinasPropias.class);
            intent.putExtra("idUsuario", idUsuario);
            context.startActivity(intent);

        });

        btnRutinasPredefinidas.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityRutinasPredefinida.class);
            context.startActivity(intent);

        });

        btnVerPerfil.setOnClickListener(v -> { //agregar logica para cargar los datos en ver perfil

            Intent intent = new Intent(context, ActivityPerfil.class);
            intent.putExtra("idUsuario", idUsuario);
            context.startActivity(intent);

        });

        btnCerrarSesion.setOnClickListener(v -> {

            finish();

        });

    }

}
