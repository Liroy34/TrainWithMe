package com.example.tpfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.Usuario;

public class ActivityRecuperarPass extends AppCompatActivity {

    private EditText mail;
    private Button btnRecuperarPass;
    private ConexionUsuario conUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_con);

        mail = findViewById(R.id.etEmailRecuperar);
        btnRecuperarPass = findViewById(R.id.btnRecuperarContrasena);

        btnRecuperarPass.setOnClickListener(v -> {

            if(!isValidEmail(mail.toString())) {
                Toast.makeText(this, "Formato de mail incorrecto", Toast.LENGTH_SHORT).show();
            }
            else{

                conUsuario.fetchRecuperarPass(mail.toString(), new ConexionUsuario.UsuarioCallback() {

                    @Override
                    public void onUsuarioReceived(Usuario usuario) {

                        enviarCorreoConPassword(mail.toString(), usuario.getPassword());

                    }
                });
            }

        });

    }

    private void enviarCorreoConPassword(String email, String pass) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email}); // Dirección de correo del usuario
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recuperación de Contraseña");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola,\n\nTu contraseña es: " + pass + "\n\nSaludos.");

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
        } else {

            Toast.makeText(this, "No se pudo abrir la aplicación de correo.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";
        return email.matches(emailPattern);
    }
}
