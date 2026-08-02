package com.example.hikermanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddObservationActivity
        extends AppCompatActivity {

    private TextView txtObservationHikeName;

    private TextInputLayout tilObservation;
    private TextInputLayout tilObservationTime;

    private TextInputEditText edtObservation;
    private TextInputEditText edtObservationTime;
    private TextInputEditText edtObservationComment;

    private Button btnSaveObservation;

    private DatabaseHelper databaseHelper;

    private int hikeId;
    private String hikeName;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_observation
        );

        txtObservationHikeName =
                findViewById(
                        R.id.txtObservationHikeName
                );

        tilObservation =
                findViewById(
                        R.id.tilObservation
                );

        tilObservationTime =
                findViewById(
                        R.id.tilObservationTime
                );

        edtObservation =
                findViewById(
                        R.id.edtObservation
                );

        edtObservationTime =
                findViewById(
                        R.id.edtObservationTime
                );

        edtObservationComment =
                findViewById(
                        R.id.edtObservationComment
                );

        btnSaveObservation =
                findViewById(
                        R.id.btnSaveObservation
                );

        hikeId = getIntent().getIntExtra(
                "HIKE_ID",
                -1
        );

        hikeName = getIntent().getStringExtra(
                "HIKE_NAME"
        );

        if (hikeId == -1) {
            finish();
            return;
        }

        txtObservationHikeName.setText(
                hikeName
        );

        databaseHelper =
                new DatabaseHelper(this);

        /*
         * defaults to the current date and time.
         */
        setCurrentDateTime();

        edtObservationTime.setOnClickListener(v ->
                showDateTimePicker()
        );

        btnSaveObservation.setOnClickListener(v -> {

            if (validateInput()) {
                saveObservation();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setCurrentDateTime() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        int hour =
                calendar.get(Calendar.HOUR_OF_DAY);

        int minute =
                calendar.get(Calendar.MINUTE);

        String currentDateTime =
                String.format(
                        "%02d/%02d/%04d %02d:%02d",
                        day,
                        month + 1,
                        year,
                        hour,
                        minute
                );

        edtObservationTime.setText(
                currentDateTime
        );
    }

    private void showDateTimePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view,
                         selectedYear,
                         selectedMonth,
                         selectedDay) -> {

                            showTimePicker(
                                    selectedYear,
                                    selectedMonth,
                                    selectedDay
                            );
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    private void showTimePicker(
            int selectedYear,
            int selectedMonth,
            int selectedDay
    ) {

        Calendar calendar =
                Calendar.getInstance();

        int hour =
                calendar.get(Calendar.HOUR_OF_DAY);

        int minute =
                calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(
                        this,
                        (view,
                         selectedHour,
                         selectedMinute) -> {

                            String dateTime =
                                    String.format(
                                            "%02d/%02d/%04d %02d:%02d",
                                            selectedDay,
                                            selectedMonth + 1,
                                            selectedYear,
                                            selectedHour,
                                            selectedMinute
                                    );

                            edtObservationTime.setText(
                                    dateTime
                            );
                        },
                        hour,
                        minute,
                        true
                );

        timePickerDialog.show();
    }

    private boolean validateInput() {

        boolean valid = true;

        tilObservation.setError(null);
        tilObservationTime.setError(null);

        String observation =
                getText(edtObservation);

        String observationTime =
                getText(edtObservationTime);

        if (observation.isEmpty()) {

            tilObservation.setError(
                    "Please enter observation"
            );

            valid = false;
        }

        if (observationTime.isEmpty()) {

            tilObservationTime.setError(
                    "Please select date and time"
            );

            valid = false;
        }

        return valid;
    }

    private void saveObservation() {

        String observation =
                getText(edtObservation);

        String observationTime =
                getText(edtObservationTime);

        String comment =
                getText(edtObservationComment);

        boolean result =
                databaseHelper.insertObservation(
                        hikeId,
                        observation,
                        observationTime,
                        comment
                );

        if (result) {

            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage(
                            "Observation saved successfully."
                    )
                    .setPositiveButton(
                            "OK",
                            (dialog, which) ->
                                    finish()
                    )
                    .show();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(
                            "Failed to save observation."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();
        }
    }

    private String getText(
            TextInputEditText editText
    ) {

        if (editText.getText() == null) {
            return "";
        }

        return editText
                .getText()
                .toString()
                .trim();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}