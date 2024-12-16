package com.example.myapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    private EditText etEventTitle, etEventDate, etEventDescription, etEventLocation, etEventReminderTime;
    private Button btnAddEvent;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent i = getIntent();
        myUser = (User) i.getSerializableExtra("myUser");

        // Inicializa los campos de texto y el botón
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDate = findViewById(R.id.etEventDate);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventLocation = findViewById(R.id.etEventLocation);
        etEventReminderTime = findViewById(R.id.etEventReminderTime);
        btnAddEvent = findViewById(R.id.btnAddEvent);

        btnAddEvent.setOnClickListener(v -> addEvent(myUser.getId()));
    }

    private void addEvent(int idUser) {
        String title = etEventTitle.getText().toString();
        String date = etEventDate.getText().toString();
        String description = etEventDescription.getText().toString();
        String location = etEventLocation.getText().toString();
        int reminderTime = Integer.parseInt(etEventReminderTime.getText().toString());

        // Crear un objeto Event
        Event event = new Event(idUser, title, date, 1, description, location, reminderTime);  // '1' como ejemplo de importancia

        // Llamar al método insertEvent para agregarlo a la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertEvent(event);

        finish();
    }
}
