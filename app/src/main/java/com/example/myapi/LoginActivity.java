package com.example.myapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegistro);

        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String lastUser = preferences.getString("last_user", "");
        if (!lastUser.isEmpty()) {
            etUsername.setText(lastUser);
        }

        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> openRegisterActivity());
        tvForgotPassword.setOnClickListener(v -> openForgotPasswordActivity());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        User user = dbHelper.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("last_user", username);
            editor.apply();
            Intent i = new Intent(LoginActivity.this, ListEvents.class);
            i.putExtra("myUser", user);
            startActivity(i);
        } else {
            Toast.makeText(this, getString(R.string.toast_error_login), Toast.LENGTH_SHORT).show();
        }
    }

    private void openRegisterActivity() {
        // Abre la actividad de registro
        startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
    }

    private void openForgotPasswordActivity() {
        // Abre la actividad para recuperar contraseña
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }
}
