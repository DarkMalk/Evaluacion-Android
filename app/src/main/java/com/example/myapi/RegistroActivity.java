package com.example.myapi;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etSecretAnswer;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializamos los campos de texto y el botón
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSecretAnswer = findViewById(R.id.etSecretRespuesta);
        btnRegister = findViewById(R.id.btnRegistro);

        // Acción para registrar al usuario cuando hace clic en el botón
        btnRegister.setOnClickListener(v -> registerUser());
    }

    // Método para registrar el usuario
    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String secretAnswer = etSecretAnswer.getText().toString().trim();

        // Validación básica para asegurarse de que los campos no estén vacíos
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(secretAnswer)) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto de la clase DatabaseHelper para insertar los datos del usuario
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Verificar si el usuario ya existe
        boolean userExists = dbHelper.isUserExist(username); // Corregir la llamada al método

        if (userExists) { // Usamos la variable userExists directamente
            Toast.makeText(this, getString(R.string.toast_user_already_exist), Toast.LENGTH_SHORT).show();
        } else {
            // Insertamos al nuevo usuario
            dbHelper.insertUser(new User(username, password, secretAnswer));
            Toast.makeText(this, getString(R.string.toast_user_register), Toast.LENGTH_SHORT).show();
        }


        // Volver a la actividad de Login
            finish();
        }
    }

