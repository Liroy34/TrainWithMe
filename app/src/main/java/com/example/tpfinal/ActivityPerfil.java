package com.example.tpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.Usuario;

public class ActivityPerfil extends AppCompatActivity {

    private TextView tvNombre, tvApellido, tvEmail, tvGenero;
    private Button btnEditarPerfil, btnDarDeBaja;
    private ImageButton btnVolverMiPerfil;
    private int idUsuario;

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

         idUsuario = getIntent().getIntExtra("idUsuario", -1);

        if (idUsuario == -1) {
            // Manejo de errores si no se recibió el idUsuario
            finish();
            return;
        }

        btnVolverMiPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfil.this, ActivityPaginaInicio.class); // Asegúrate de que PaginaPrincipal es el nombre correcto
            startActivity(intent);
            finish();
        });

        cargarDatosUsuario();

        configurarBotonDarDeBaja();


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

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(email);
        usuario.setGenero(genero);

        ConexionUsuario conexionUsuario = new ConexionUsuario(this);
        conexionUsuario.updateUsuario(usuario);

        Toast.makeText(this, "Datos guardados correctamente (pendiente conexión BD)", Toast.LENGTH_SHORT).show();
    }

    private void configurarBotonDarDeBaja() {
        btnDarDeBaja.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmar acción");
            builder.setMessage("¿Estás seguro de que deseas dar de baja este usuario? Esta acción no se puede deshacer.");

            builder.setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                darDeBajaUsuario();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss(); // Cierra el modal sin realizar ninguna acción
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void darDeBajaUsuario() {
        // Implementar la lógica para dar de baja al usuario
        ConexionUsuario conexionUsuario = new ConexionUsuario(this);
        conexionUsuario.deleteUsuario(idUsuario); // Asegúrate de tener este método en tu clase de conexión
    }
}
