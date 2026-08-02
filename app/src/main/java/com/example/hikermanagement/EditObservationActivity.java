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

public class EditObservationActivity
        extends AppCompatActivity {

    private TextView txtEditObservationHikeName;

    private TextInputLayout tilEditObservation;
    private TextInputLayout tilEditObservationTime;

    private TextInputEditText edtEditObservation;
    private TextInputEditText edtEditObservationTime;
    private TextInputEditText edtEditObservationComment;

    private Button btnUpdateObservation;

    private DatabaseHelper databaseHelper;

    private int observationId;
    private int hikeId;

    private String hikeName;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_edit_observation
        );

        bindViews();

        databaseHelper =
                new DatabaseHelper(this);

        receiveObservationData();

        edtEditObservationTime
                .setOnClickListener(v ->
                        showDateTimePicker()
                );

        btnUpdateObservation
                .setOnClickListener(v -> {

                    if (validateInput()) {
                        showUpdateConfirmation();
                    }
                });
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindViews() {

        txtEditObservationHikeName =
                findViewById(
                        R.id.txtEditObservationHikeName
                );

        tilEditObservation =
                findViewById(
                        R.id.tilEditObservation
                );

        tilEditObservationTime =
                findViewById(
                        R.id.tilEditObservationTime
                );

        edtEditObservation =
                findViewById(
                        R.id.edtEditObservation
                );

        edtEditObservationTime =
                findViewById(
                        R.id.edtEditObservationTime
                );

        edtEditObservationComment =
                findViewById(
                        R.id.edtEditObservationComment
                );

        btnUpdateObservation =
                findViewById(
                        R.id.btnUpdateObservation
                );
    }

    private void receiveObservationData() {

        observationId =
                getIntent().getIntExtra(
                        "OBSERVATION_ID",
                        -1
                );

        hikeId =
                getIntent().getIntExtra(
                        "HIKE_ID",
                        -1
                );

        hikeName =
                getIntent().getStringExtra(
                        "HIKE_NAME"
                );

        String observation =
                getIntent().getStringExtra(
                        "OBSERVATION_CONTENT"
                );

        String observationTime =
                getIntent().getStringExtra(
                        "OBSERVATION_TIME"
                );

        String comment =
                getIntent().getStringExtra(
                        "OBSERVATION_COMMENT"
                );

        if (observationId == -1) {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(
                            "Observation information could not be loaded."
                    )
                    .setPositiveButton(
                            "OK",
                            (dialog, which) ->
                                    finish()
                    )
                    .show();

            return;
        }

        txtEditObservationHikeName
                .setText(hikeName);

        edtEditObservation
                .setText(observation);

        edtEditObservationTime
                .setText(observationTime);

        edtEditObservationComment
                .setText(comment);
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
                calendar.get(
                        Calendar.HOUR_OF_DAY
                );

        int minute =
                calendar.get(
                        Calendar.MINUTE
                );

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

                            edtEditObservationTime
                                    .setText(dateTime);
                        },
                        hour,
                        minute,
                        true
                );

        timePickerDialog.show();
    }

    private boolean validateInput() {

        boolean valid = true;

        tilEditObservation.setError(null);
        tilEditObservationTime.setError(null);

        String observation =
                getText(edtEditObservation);

        String observationTime =
                getText(edtEditObservationTime);

        if (observation.isEmpty()) {

            tilEditObservation.setError(
                    "Please enter observation"
            );

            valid = false;
        }

        if (observationTime.isEmpty()) {

            tilEditObservationTime.setError(
                    "Please select date and time"
            );

            valid = false;
        }

        return valid;
    }

    private void showUpdateConfirmation() {

        String observation =
                getText(edtEditObservation);

        String observationTime =
                getText(edtEditObservationTime);

        String comment =
                getText(
                        edtEditObservationComment
                );

        String message =
                "Observation: "
                        + observation
                        + "\n\nDate and Time: "
                        + observationTime
                        + "\n\nComment: "
                        + (
                        comment.isEmpty()
                                ? "No comment"
                                : comment
                )
                        + "\n\nSave these changes?";

        new AlertDialog.Builder(this)
                .setTitle(
                        "Confirm Update"
                )
                .setMessage(message)
                .setPositiveButton(
                        "YES",
                        (dialog, which) -> {

                            updateObservation(
                                    observation,
                                    observationTime,
                                    comment
                            );
                        }
                )
                .setNegativeButton(
                        "NO",
                        null
                )
                .show();
    }

    private void updateObservation(
            String observation,
            String observationTime,
            String comment
    ) {

        boolean result =
                databaseHelper
                        .updateObservation(
                                observationId,
                                observation,
                                observationTime,
                                comment
                        );

        if (result) {

            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage(
                            "Observation updated successfully."
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
                            "Failed to update observation."
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
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}