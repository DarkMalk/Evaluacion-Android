package com.example.myapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewInfoEvent extends AppCompatActivity {
    private Event event;
    private TextView infoTitle, infoDescription, infoLocation, infoDate, infoReminder;
    private Button btnDeleteEvent, btnCloseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_event);
        initializeItemsXML();

        Intent i = getIntent();
        event = (Event) i.getSerializableExtra("object");

        setDataInElements();
        events();
    }

    private void initializeItemsXML() {
        infoTitle = findViewById(R.id.infoTitle);
        infoDescription = findViewById(R.id.infoDescription);
        infoLocation = findViewById(R.id.infoLocation);
        infoDate = findViewById(R.id.infoDate);
        infoReminder = findViewById(R.id.infoReminder);

        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);
        btnCloseInfo = findViewById(R.id.btnCloseInfo);
    }

    private void setDataInElements() {
        infoTitle.setText(getString(R.string.title_info_event) + " " + event.getTitle());
        infoDescription.setText(getString(R.string.description_info_event) + " " + event.getDescription());
        infoLocation.setText(getString(R.string.location_info_event) + " " + event.getLocation());
        infoDate.setText(getString(R.string.date_info_event) + " " + event.getDate());
        infoReminder.setText(getString(R.string.reminder_info_event) + " " + event.getReminderTime());
    }

    private void events() {
        btnCloseInfo.setOnClickListener((view) -> {
            finish();
        });

        btnDeleteEvent.setOnClickListener((view) -> {
            DatabaseHelper db = new DatabaseHelper(this);

            boolean isDelete = db.deleteEvent(event.getId());
            if (isDelete) {
                Toast.makeText(this, getString(R.string.toast_delete_event), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.toast_error_delete_event), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}