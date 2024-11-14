package com.example.tpfinal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinal.Conexion.ConexionUsuario;
import com.example.tpfinal.Entidades.Usuario;

public class ActivityRegistrarse extends AppCompatActivity{

    private EditText nombre, apellido, mail, cel, usuario, password;
    private Spinner generos;
    private ConexionUsuario conUsuario;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        generos = findViewById(R.id.spinnerGeneroRegistrarse);
        String[] opciones = {"Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generos.setAdapter(adapter);

        btnRegistrarse = findViewById(R.id.btnRegistrarseRegistrarse);

        nombre = findViewById(R.id.etNombreRegistrarse);
        apellido = findViewById(R.id.etApellidoRegistrarse);
        mail = findViewById(R.id.etEmailRegistrarse);
        cel = findViewById(R.id.etCelularRegistrarse);
        usuario = findViewById(R.id.etUsuarioRegistrarse);
        password = findViewById(R.id.etContrasenaRegistrarse);

        btnRegistrarse.setOnClickListener(v -> {

            if(checkValues(nombre) || checkValues(apellido) || checkValues(mail) || checkValues(cel) || checkValues(usuario) || checkValues(password)){
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
            else{
                if(isValidEmail(mail.toString())){
                    Toast.makeText(this, "Formato de mail incorrecto", Toast.LENGTH_SHORT).show();
                }
                else {

                    String nombreString = nombre.getText().toString();
                    String apellidoString = apellido.getText().toString();
                    String mailString = mail.getText().toString();
                    String celString = cel.getText().toString();
                    String usuarioString = usuario.getText().toString();
                    String passwordString = password.getText().toString();
                    String generoString = generos.getSelectedItem().toString();

                    Usuario usuario = new Usuario(nombreString, apellidoString, generoString, mailString, celString, usuarioString, passwordString);

                    conUsuario.insertUsuario(usuario);
                }

            }

        });


    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";
        return email.matches(emailPattern);
    }

    private boolean checkValues(TextView value){

        String valueString = value.getText().toString();

        return valueString.isEmpty();
    }
}