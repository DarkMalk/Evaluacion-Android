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

        Intent intent = getIntent();
        myUser = (User) intent.getSerializableExtra("myUser");


        submit(myUser.getId());
    }

    private void initializeElementsXML() {
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewPasswordConfirm = findViewById(R.id.etNewPasswordConfirm);
        btnConfirmNewPassword = findViewById(R.id.btnConfirmNewPassword);
    }

    private boolean validateInputs(String newPassword, String newPasswordConfirm) {
        if (newPassword.trim().isBlank()) {
            etNewPassword.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (newPasswordConfirm.trim().isBlank()) {
            etNewPasswordConfirm.setError(getString(R.string.toast_error_input_not_empty));
            return false;
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            Toast.makeText(this, getString(R.string.toast_error_dont_match), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void submit(int myUserId) {
        btnConfirmNewPassword.setOnClickListener((view) -> {
            String newPassword = etNewPassword.getText().toString();
            String newPasswordConfirm = etNewPasswordConfirm.getText().toString();
            boolean isValid = validateInputs(newPassword, newPasswordConfirm);

            if (!isValid) {
                return;
            }

            db.resetPassword(myUserId, newPassword);
            Toast.makeText(this, getString(R.string.toast_reset_password_success), Toast.LENGTH_LONG).show();
            finish();
        });
    }
}