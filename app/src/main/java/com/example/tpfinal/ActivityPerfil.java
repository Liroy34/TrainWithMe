package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.Usuario;

public class ActivityPerfil extends AppCompatActivity {

    private TextView tvNombre, tvApellido, tvEmail, tvGenero;
    private Button btnEditarPerfil, btnDarDeBaja;
    private ImageButton btnVolverMiPerfil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvNombre = findViewById(R.id.tvNombre);
        tvApellido = findViewById(R.id.tvApellido);
        tvEmail = findViewById(R.id.tvEmail);
        tvGenero = findViewById(R.id.tvGenero);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnDarDeBaja = findViewById(R.id.btnDarDeBaja);
        btnVolverMiPerfil = findViewById(R.id.btnVolverMiPerfil);

        btnVolverMiPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfil.this, ActivityPaginaInicio.class); // Asegúrate de que PaginaPrincipal es el nombre correcto
            startActivity(intent);
            finish();
        });

        cargarDatosUsuario();

        btnEditarPerfil.setOnClickListener(v -> {
            editarPerfil();
        });

        btnDarDeBaja.setOnClickListener(v -> {
            Toast.makeText(ActivityPerfil.this, "Funcionalidad para dar de baja pendiente de implementación", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarDatosUsuario() {

        tvNombre.setText("Juan");
        tvApellido.setText("Pérez");
        tvEmail.setText("juan.perez@example.com");
        tvGenero.setText("Masculino");
    }

    private void editarPerfil() {
        // Obtén los datos actuales de los campos
        String nombre = tvNombre.getText().toString();
        String apellido = tvApellido.getText().toString();
        String email = tvEmail.getText().toString();
        String genero = tvGenero.getText().toString();

        // Crear objeto Usuario y enviarlo a la base de datos en el futuro
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(email);
        usuario.setGenero(genero);

        ConexionUsuario conexionUsuario = new ConexionUsuario(this);
        conexionUsuario.insertUsuario(usuario);

        // Mostrar mensaje temporal
        Toast.makeText(this, "Datos guardados correctamente (pendiente conexión BD)", Toast.LENGTH_SHORT).show();
    }
}
