package com.example.myapi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {
    private EditText etUsername, etNewPassword, etNewPasswordConfirm, etSecretRespuesta;
    private Button btnConfirmNewPassword;
    private User user;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeElementsXML();

        db = new DatabaseHelper(this);

        submit();
    }

    private void initializeElementsXML() {
        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewPasswordConfirm = findViewById(R.id.etNewPasswordConfirm);
        etSecretRespuesta = findViewById(R.id.etSecretRespuesta);
        btnConfirmNewPassword = findViewById(R.id.btnConfirmNewPassword);
    }

    private boolean validateInputs(String username, String newPassword, String confirmPassword, String secretRepuesta, User user) {
        if (username.trim().isBlank()) {
            etUsername.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (newPassword.trim().isBlank()) {
            etNewPassword.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (confirmPassword.trim().isBlank()) {
            etNewPasswordConfirm.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (secretRepuesta.trim().isBlank()) {
            etSecretRespuesta.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.toast_error_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!secretRepuesta.equals(user.getSecretAnswer())) {
            etSecretRespuesta.setError(getString(R.string.error_dont_match_secret_response));
            return false;
        }

        return true;
    }

    private void submit() {
        btnConfirmNewPassword.setOnClickListener((view) -> {
            String username = etUsername.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etNewPasswordConfirm.getText().toString();
            String secretRespuesta = etSecretRespuesta.getText().toString();

            user = db.getUser(username);

            boolean isValid = validateInputs(username, newPassword, confirmPassword, secretRespuesta, user);
            if (!isValid) {
                return;
            }

            db.resetPassword(user.getId(), newPassword);
            Toast.makeText(this, getString(R.string.toast_reset_password_success), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}