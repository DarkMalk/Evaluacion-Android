package com.example.myapi;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ListEvents extends AppCompatActivity {
    private ListView listViewEvents;
    private Button btnNewEvent, btnExportData;
    private DatabaseHelper db;
    private final ArrayList<Event> events = new ArrayList<>();
    private ArrayAdapter<Event> adapter;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Intent intent = getIntent();
        myUser = (User) intent.getSerializableExtra("myUser");

        initializeElementsXML();
        db = new DatabaseHelper(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        listViewEvents.setAdapter(adapter);


        navigateToAddEvent();
        navigateToInfoEvent();
        onClickExport();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(myUser.getId());

        adapter.notifyDataSetChanged();
    }

    private void getData(int filterIdUser) {
        this.events.clear();

        Cursor allEvents = db.getAllEvents(filterIdUser);
        while (allEvents.moveToNext()) {
            int id = allEvents.getInt(allEvents.getColumnIndexOrThrow("event_id"));
            int idUser = allEvents.getInt(allEvents.getColumnIndexOrThrow("id_user"));
            String title = allEvents.getString(allEvents.getColumnIndexOrThrow("event_title"));
            String date = allEvents.getString(allEvents.getColumnIndexOrThrow("event_date"));
            int importance = allEvents.getInt(allEvents.getColumnIndexOrThrow("event_importance"));
            String description = allEvents.getString(allEvents.getColumnIndexOrThrow("event_description"));
            String location = allEvents.getString(allEvents.getColumnIndexOrThrow("event_location"));
            int reminderTime = allEvents.getInt(allEvents.getColumnIndexOrThrow("event_reminder_time"));

            Event event = new Event(id, idUser, title, date, importance, description, location, reminderTime);

            this.events.add(event);
        }
    }

    private void initializeElementsXML() {
        listViewEvents = findViewById(R.id.listViewEvents);
        btnNewEvent = findViewById(R.id.btnNewEvent);
        btnExportData = findViewById(R.id.btnExportData);
    }

    private void navigateToAddEvent() {
        btnNewEvent.setOnClickListener((view) -> {
            Intent i = new Intent(ListEvents.this, EventActivity.class);
            i.putExtra("myUser", myUser);
            startActivity(i);
        });
    }

    private void navigateToInfoEvent() {
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selection = events.get(position);

                Intent i = new Intent(ListEvents.this, ViewInfoEvent.class);
                i.putExtra("object", selection);
                startActivity(i);
            }
        });
    }
    private void onClickExport() {
        btnExportData.setOnClickListener(view -> {
            exportData(ListEvents.this, events);
        });
    }

    private void exportData(Context context, ArrayList<Event> data) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "data.csv");

        try {
            FileWriter writer = new FileWriter(file);
            writer.append("title,date,description,location,reminderTime");
            writer.append("\n");

            for (Event event : data) {
                String title = event.getTitle();
                String date = event.getDate();
                String description = event.getDescription();
                String location = event.getLocation();
                String reminderTime = String.valueOf(event.getReminderTime());

                String row = title + "," + date + "," + description + "," + location + "," + reminderTime;
                writer.append(row);
                writer.append("\n");
            }

            writer.flush();
            writer.close();

            Toast.makeText(context, getString(R.string.toast_export_data_success), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, getString(R.string.toast_export_data_success), Toast.LENGTH_LONG).show();
        }
    }
}