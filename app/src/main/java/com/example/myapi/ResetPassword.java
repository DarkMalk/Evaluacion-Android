package com.example.myapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    private EditText etNewPassword, etNewPasswordConfirm;
    private Button btnConfirmNewPassword;
    private DatabaseHelper db;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initializeElementsXML();

        db = new DatabaseHelper(this);

        // Obtenemos el usuario actual
        Intent intent = getIntent();
        myUser = (User) intent.getSerializableExtra("myUser");


        // Pasamos por parametro el ID del usuario al cual resetearemos la contraseña
        submit(myUser.getId());
    }

    private void initializeElementsXML() {
        // Inicializamos la referencia de los elementos en la vista (XML)
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewPasswordConfirm = findViewById(R.id.etNewPasswordConfirm);
        btnConfirmNewPassword = findViewById(R.id.btnConfirmNewPassword);
    }

    private boolean validateInputs(String newPassword, String newPasswordConfirm) {
        // Verificar que el campo no sea vacio
        if (newPassword.trim().isBlank()) {
            etNewPassword.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        // Verificar que el campo no sea vacio
        if (newPasswordConfirm.trim().isBlank()) {
            etNewPasswordConfirm.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        // Validamos que los campos de contraseña y confirmación de contraseña sean iguales
        if (!newPassword.equals(newPasswordConfirm)) {
            Toast.makeText(this, getString(R.string.toast_error_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void submit(int myUserId) {
        // Validamos y reseteamos contraseña al realizar un click en el boton
        btnConfirmNewPassword.setOnClickListener((view) -> {
            String newPassword = etNewPassword.getText().toString();
            String newPasswordConfirm = etNewPasswordConfirm.getText().toString();
            boolean isValid = validateInputs(newPassword, newPasswordConfirm);

            // Si no es valido la validación del input se corta la ejecución
            if (!isValid) {
                return;
            }

            // Si todos los inputs son validos, se cambia la contraseña al usuario
            db.resetPassword(myUserId, newPassword);
            Toast.makeText(this, getString(R.string.toast_reset_password_success), Toast.LENGTH_LONG).show();
            finish();
        });
    }
}