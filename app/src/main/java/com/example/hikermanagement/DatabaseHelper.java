package com.example.hikermanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hike.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "hikes";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_LOCATION = "location";
    public static final String COL_DATE = "date";
    public static final String COL_PARKING = "parking";
    public static final String COL_LENGTH = "length";
    public static final String COL_DIFFICULTY = "difficulty";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_WEATHER = "weather";
    public static final String COL_DURATION = "duration";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_NAME + " TEXT, "
                        + COL_LOCATION + " TEXT, "
                        + COL_DATE + " TEXT, "
                        + COL_PARKING + " TEXT, "
                        + COL_LENGTH + " INTEGER, "
                        + COL_DIFFICULTY + " TEXT, "
                        + COL_DESCRIPTION + " TEXT, "
                        + COL_WEATHER + " TEXT, "
                        + COL_DURATION + " TEXT"
                        + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertHike(
            String name,
            String location,
            String date,
            String parking,
            int length,
            String difficulty,
            String description,
            String weather,
            String duration) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LOCATION, location);
        values.put(COL_DATE, date);
        values.put(COL_PARKING, parking);
        values.put(COL_LENGTH, length);
        values.put(COL_DIFFICULTY, difficulty);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_WEATHER, weather);
        values.put(COL_DURATION, duration);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }
    public List<Hike> getAllHikes() {

        List<Hike> hikeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_NAME)
                );

                String location = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_LOCATION)
                );

                String date = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DATE)
                );

                String parking = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_PARKING)
                );

                int length = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_LENGTH)
                );

                String difficulty = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DIFFICULTY)
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DESCRIPTION)
                );

                String weather = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_WEATHER)
                );

                String duration = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DURATION)
                );

                Hike hike = new Hike(
                        id,
                        name,
                        location,
                        date,
                        parking,
                        length,
                        difficulty,
                        description,
                        weather,
                        duration
                );

                hikeList.add(hike);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return hikeList;
    }


    public List<Hike> searchHikes(String keyword) {

        List<Hike> hikeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String searchValue = "%" + keyword + "%";

        String query =
                "SELECT * FROM " + TABLE_NAME
                        + " WHERE " + COL_NAME + " LIKE ?"
                        + " OR " + COL_LOCATION + " LIKE ?"
                        + " OR " + COL_DATE + " LIKE ?"
                        + " OR " + COL_DIFFICULTY + " LIKE ?"
                        + " ORDER BY " + COL_ID + " DESC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        searchValue,
                        searchValue,
                        searchValue,
                        searchValue
                }
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_NAME)
                );

                String location = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_LOCATION)
                );

                String date = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DATE)
                );

                String parking = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_PARKING)
                );

                int length = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_LENGTH)
                );

                String difficulty = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DIFFICULTY)
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DESCRIPTION)
                );

                String weather = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_WEATHER)
                );

                String duration = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DURATION)
                );

                Hike hike = new Hike(
                        id,
                        name,
                        location,
                        date,
                        parking,
                        length,
                        difficulty,
                        description,
                        weather,
                        duration
                );

                hikeList.add(hike);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return hikeList;
    }

    public boolean deleteHike(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    public boolean updateHike(
            int id,
            String name,
            String location,
            String date,
            String parking,
            int length,
            String difficulty,
            String description,
            String weather,
            String duration
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_LOCATION, location);
        values.put(COL_DATE, date);
        values.put(COL_PARKING, parking);
        values.put(COL_LENGTH, length);
        values.put(COL_DIFFICULTY, difficulty);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_WEATHER, weather);
        values.put(COL_DURATION, duration);

        int result = db.update(
                TABLE_NAME,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

}