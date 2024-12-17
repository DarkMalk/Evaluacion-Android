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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ListEvents extends AppCompatActivity {
    private ListView listViewEvents;
    private Spinner spinnerOptions;
    private Button btnNewEvent, btnOpenSpinner;
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
        showSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(myUser.getId());

        adapter.notifyDataSetChanged();
    }

    private void getData(int filterIdUser) {
        // Limpiamos el arrayList
        this.events.clear();

        // Recuperamos todos los eventos de la base de datos
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

            // Añadimos los eventos al arrayList
            this.events.add(event);
        }
    }

    // Inicializamos los elementos que se encuentran en la vista
    private void initializeElementsXML() {
        listViewEvents = findViewById(R.id.listViewEvents);
        btnNewEvent = findViewById(R.id.btnNewEvent);
        btnOpenSpinner = findViewById(R.id.btnOpenSpinner);
        spinnerOptions = findViewById(R.id.spinnerOptions);
    }

    // Navegamos a la vista para crear nuevos eventos
    private void navigateToAddEvent() {
        btnNewEvent.setOnClickListener((view) -> {
            Intent i = new Intent(ListEvents.this, EventActivity.class);
            i.putExtra("myUser", myUser);
            startActivity(i);
        });
    }

    // Navegamos a la información del evento seleccionado
    private void navigateToInfoEvent() {
        listViewEvents.setOnItemClickListener(((parent, view, position, id) -> {
            Event selection = events.get(position);

            Intent i = new Intent(ListEvents.this, ViewInfoEvent.class);
            i.putExtra("object", selection);
            startActivity(i);
        }));
    }

    // Exportamos la información de los eventos en formato CSV en carpeta descargas
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
            Toast.makeText(context, getString(R.string.toast_export_data_success), Toast.LENGTH_LONG).show();
        }
    }

    // Manejamos el menu Spinner y sus diferentes opciones
    private void showSpinner() {
        String[] values = {
                getString(R.string.spinner_initial_option),
                getString(R.string.spinner_option_export_data),
                getString(R.string.spinner_option_delete_account),
                getString(R.string.spinner_option_reset_password),
                getString(R.string.spinner_option_logout)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, values);
        spinnerOptions.setAdapter(adapter);

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Context context = parent.getContext();

                if (selectedItem.equals(getString(R.string.spinner_initial_option))) {
                    return;
                }

                if (selectedItem.equals(getString(R.string.spinner_option_export_data))) {
                    exportData(context, events);
                }

                if (selectedItem.equals(getString(R.string.spinner_option_delete_account))) {
                    // Creamos un alert dialog para confirmar o cancelar la acción de eliminar el usuario
                    new AlertDialogHelper().showConfirmationDialog(
                            context,
                            getString(R.string.alert_dialog_title),
                            getString(R.string.alert_dialog_button_confirm),
                            getString(R.string.alert_dialog_button_cancel),
                            getString(R.string.alert_dialog_description),
                            (accepted -> {
                                if (!accepted) {
                                    return;
                                }

                                // Si aceptamos el alert dialog, procedemos a eliminar todos los eventos y luego el usuario
                                for (Event event : events) {
                                    db.deleteEvent(event.getId());
                                }

                                boolean isDeleteUser = db.deleteUser(myUser.getId());

                                if (isDeleteUser) {
                                    Toast.makeText(context, getString(R.string.toast_delete_user_success), Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, getString(R.string.toast_error_delete_user), Toast.LENGTH_LONG).show();
                                }
                            })
                    );
                }

                if (selectedItem.equals(getString(R.string.spinner_option_reset_password))) {
                    Intent i = new Intent(context, ResetPassword.class);
                    i.putExtra("myUser", myUser);
                    startActivity(i);
                }

                if (selectedItem.equals(getString(R.string.spinner_option_logout))) {
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnOpenSpinner.setOnClickListener(view -> spinnerOptions.performClick());
    }
}