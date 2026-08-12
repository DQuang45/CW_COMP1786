package com.example.hikermanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hike.db";
    private static final int DATABASE_VERSION = 2;

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

    // Observation table
    public static final String TABLE_OBSERVATIONS = "observations";

    public static final String OBS_COL_ID = "id";
    public static final String OBS_COL_HIKE_ID = "hike_id";
    public static final String OBS_COL_OBSERVATION = "observation";
    public static final String OBS_COL_TIME = "observation_time";
    public static final String OBS_COL_COMMENT = "comment";

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

        String createObservationTable =
                "CREATE TABLE " + TABLE_OBSERVATIONS + " (" +
                        OBS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        OBS_COL_HIKE_ID + " INTEGER NOT NULL, " +
                        OBS_COL_OBSERVATION + " TEXT NOT NULL, " +
                        OBS_COL_TIME + " TEXT NOT NULL, " +
                        OBS_COL_COMMENT + " TEXT, " +
                        "FOREIGN KEY(" + OBS_COL_HIKE_ID + ") REFERENCES " +
                        TABLE_NAME + "(" + COL_ID + ") ON DELETE CASCADE" +
                        ")";

        db.execSQL(createObservationTable);

    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_NAME
        );

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

    public boolean deleteHike(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.beginTransaction();

        try {

            db.delete(
                    TABLE_OBSERVATIONS,
                    OBS_COL_HIKE_ID + " = ?",
                    new String[]{
                            String.valueOf(id)
                    }
            );

            int result = db.delete(
                    TABLE_NAME,
                    COL_ID + " = ?",
                    new String[]{
                            String.valueOf(id)
                    }
            );

            db.setTransactionSuccessful();

            return result > 0;

        } catch (Exception exception) {

            exception.printStackTrace();

            return false;

        } finally {

            db.endTransaction();
        }
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
                        + " OR CAST(" + COL_LENGTH + " AS TEXT) LIKE ?"
                        + " OR " + COL_DIFFICULTY + " LIKE ?"
                        + " ORDER BY " + COL_ID + " DESC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        searchValue,
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


    public boolean insertObservation(
            int hikeId,
            String observation,
            String observationTime,
            String comment
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OBS_COL_HIKE_ID, hikeId);
        values.put(OBS_COL_OBSERVATION, observation);
        values.put(OBS_COL_TIME, observationTime);
        values.put(OBS_COL_COMMENT, comment);

        long result = db.insert(
                TABLE_OBSERVATIONS,
                null,
                values
        );

        return result != -1;
    }
    public List<Observation> getObservationsByHikeId(
            int hikeId
    ) {

        List<Observation> observationList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_OBSERVATIONS,
                null,
                OBS_COL_HIKE_ID + " = ?",
                new String[]{String.valueOf(hikeId)},
                null,
                null,
                OBS_COL_ID + " DESC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                OBS_COL_ID
                        )
                );

                String observation =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        OBS_COL_OBSERVATION
                                )
                        );

                String observationTime =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        OBS_COL_TIME
                                )
                        );

                String comment =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        OBS_COL_COMMENT
                                )
                        );

                Observation item =
                        new Observation(
                                id,
                                hikeId,
                                observation,
                                observationTime,
                                comment
                        );

                observationList.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return observationList;
    }
    public boolean deleteObservation(int observationId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result = db.delete(
                TABLE_OBSERVATIONS,
                OBS_COL_ID + " = ?",
                new String[]{
                        String.valueOf(observationId)
                }
        );

        return result > 0;
    }

    public boolean updateObservation(
            int observationId,
            String observation,
            String observationTime,
            String comment
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                OBS_COL_OBSERVATION,
                observation
        );

        values.put(
                OBS_COL_TIME,
                observationTime
        );

        values.put(
                OBS_COL_COMMENT,
                comment
        );

        int result = db.update(
                TABLE_OBSERVATIONS,
                values,
                OBS_COL_ID + " = ?",
                new String[]{
                        String.valueOf(observationId)
                }
        );

        return result > 0;
    }

    public boolean deleteAllHikes() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.beginTransaction();

        try {

            db.delete(
                    TABLE_OBSERVATIONS,
                    null,
                    null
            );

            db.delete(
                    TABLE_NAME,
                    null,
                    null
            );

            db.setTransactionSuccessful();

            return true;

        } catch (Exception exception) {

            exception.printStackTrace();

            return false;

        } finally {

            db.endTransaction();
        }
    }

}