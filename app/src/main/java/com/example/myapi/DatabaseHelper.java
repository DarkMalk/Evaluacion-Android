package com.example.myapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapi.Event;
import com.example.myapi.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDB";
    private static final int DATABASE_VERSION = 1;

    // Tabla de usuarios
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SECRET_ANSWER = "secret_answer";

    // Tabla de eventos
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_TITLE = "event_title";
    private static final String COLUMN_EVENT_DATE = "event_date";
    private static final String COLUMN_EVENT_IMPORTANCE = "event_importance";
    private static final String COLUMN_EVENT_DESCRIPTION = "event_description";
    private static final String COLUMN_EVENT_LOCATION = "event_location";
    private static final String COLUMN_EVENT_REMINDER_TIME = "event_reminder_time";
    private static final String COLUMN_EVENT_REFERENCE_USER = "id_user";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_SECRET_ANSWER + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_USERS);

        // Crear tabla de eventos
        String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + "("
                + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EVENT_REFERENCE_USER + " INTEGER REFERENCES " + TABLE_USERS + " (" + COLUMN_ID + "),"
                + COLUMN_EVENT_TITLE + " TEXT,"
                + COLUMN_EVENT_DATE + " TEXT,"
                + COLUMN_EVENT_IMPORTANCE + " INTEGER,"
                + COLUMN_EVENT_DESCRIPTION + " TEXT,"
                + COLUMN_EVENT_LOCATION + " TEXT,"
                + COLUMN_EVENT_REMINDER_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // Método para insertar un usuario en la base de datos
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_SECRET_ANSWER, user.getSecretAnswer());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Verificar si un usuario ya existe
    public boolean isUserExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Método para insertar un evento en la base de datos
    public void insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_REFERENCE_USER, event.getIdUser());
        values.put(COLUMN_EVENT_TITLE, event.getTitle());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_IMPORTANCE, event.getImportance());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_LOCATION, event.getLocation());
        values.put(COLUMN_EVENT_REMINDER_TIME, event.getReminderTime());
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_EVENT_ID + " = ?";
        String[] selectionArgs = new String[]{ String.valueOf(id) };
        int affectedRows = db.delete(TABLE_EVENTS, whereClause, selectionArgs);

        return affectedRows > 0;
    }

    // Método para obtener todos los eventos (puedes personalizarlo más)
    public Cursor getAllEvents(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EVENTS + " WHERE id_user = ?";
        String[] selectionArgs = { String.valueOf(idUser) };
        return db.rawQuery(sql, selectionArgs);
    }

    // Método para obtener un usuario por su nombre de usuario
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Realizamos la consulta para obtener el usuario por su nombre
        String[] columns = {COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_SECRET_ANSWER};
        String whereClouse = COLUMN_USERNAME + " = ?";
        String[] args = { username };
        Cursor cursor = db.query(TABLE_USERS, columns, whereClouse, args, null, null, null);

        // Verificar si el cursor tiene al menos un registro
        if (cursor != null && cursor.moveToFirst()) {
            // Accedemos directamente a los valores de las columnas
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            String secretAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_SECRET_ANSWER));

            // Creamos un objeto User con los valores obtenidos
            User user = new User(id, userName, password, secretAnswer);

            cursor.close(); // Cerramos el cursor después de usarlo
            return user; // Retornamos el usuario
        } else {
            cursor.close(); // Cerramos el cursor si no se encontró el usuario
            return null; // Retornamos null si no encontramos el usuario
        }
    }


}
