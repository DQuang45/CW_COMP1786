package com.example.hikermanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.DatePickerDialog;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    // TextInputLayout
    private TextInputLayout tilName, tilLocation, tilDate, tilDescription, tilDuration;

    // EditText
    private TextInputEditText edtName, edtLocation, edtDate, edtDescription, edtDuration;

    // RadioGroup
    private RadioGroup rgParking;

    // NumberPicker
    private NumberPicker npLength;
    private TextView txtParkingError;

    // Spinner
    private Spinner spDifficulty, spWeather;

    // Button
    private Button btnSave;

    // Database
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        tilName = findViewById(R.id.tilName);
        tilLocation = findViewById(R.id.tilLocation);
        tilDate = findViewById(R.id.tilDate);
        tilDescription = findViewById(R.id.tilDescription);
        tilDuration = findViewById(R.id.tilDuration);
        txtParkingError = findViewById(R.id.txtParkingError);

        edtName = findViewById(R.id.edtName);
        edtLocation = findViewById(R.id.edtLocation);
        edtDate = findViewById(R.id.edtDate);
        edtDescription = findViewById(R.id.edtDescription);
        edtDuration = findViewById(R.id.edtDuration);

        rgParking = findViewById(R.id.rgParking);

        npLength = findViewById(R.id.npLength);

        spDifficulty = findViewById(R.id.spDifficulty);
        spWeather = findViewById(R.id.spWeather);

        btnSave = findViewById(R.id.btnSave);
        npLength.setMinValue(1);
        npLength.setMaxValue(50);
        npLength.setValue(5);

        String[] difficulty = {
                "Easy",
                "Medium",
                "Hard"
        };

        ArrayAdapter<String> difficultyAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        difficulty);

        difficultyAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spDifficulty.setAdapter(difficultyAdapter);

        String[] weather = {
                "Sunny",
                "Cloudy",
                "Rainy",
                "Windy"
        };

        ArrayAdapter<String> weatherAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        weather);

        weatherAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spWeather.setAdapter(weatherAdapter);

        edtDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String date = String.format(
                                "%02d/%02d/%04d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear
                        );

                        edtDate.setText(date);

                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.show();

        });


        btnSave.setOnClickListener(v -> {

            if (validateInput()) {
                showConfirmationDialog();

            }

        });

    }
    private void showConfirmationDialog() {

        String name = edtName.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String duration = edtDuration.getText().toString().trim();

        int length = npLength.getValue();

        String difficulty = spDifficulty.getSelectedItem().toString();
        String weather = spWeather.getSelectedItem().toString();

        String parking;

        int checkedId = rgParking.getCheckedRadioButtonId();

        if (checkedId == R.id.rbYes) {
            parking = "YES";
        } else if (checkedId == R.id.rbNo) {
            parking = "NO";
        } else {
            parking = "";
        }

        String message =
                "Name: " + name +
                        "\nLocation: " + location +
                        "\nDate: " + date +
                        "\nParking: " + parking +
                        "\nLength: " + length + " km" +
                        "\nDifficulty: " + difficulty +
                        "\nDescription: " + description +
                        "\nWeather: " + weather +
                        "\nEstimated Duration: " + duration + " hour(s)" +
                        "\n\nConfirm?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike")
                .setMessage(message)
                .setPositiveButton("YES", (dialog, which) -> {

                    boolean result = databaseHelper.insertHike(
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

                    if (result) {
                        clearForm();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Success")
                                .setMessage("Hike saved successfully.")
                                .setPositiveButton("OK", (successDialog, successWhich) -> {

                                    finish();

                                })
                                .show();
                    } else {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Failed to save hike.")
                                .setPositiveButton("OK", null)
                                .show();

                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }
    private void clearForm() {

        edtName.setText("");
        edtLocation.setText("");
        edtDate.setText("");
        edtDescription.setText("");
        edtDuration.setText("");

        rgParking.clearCheck();
        txtParkingError.setVisibility(View.GONE);

        npLength.setValue(5);

        spDifficulty.setSelection(0);
        spWeather.setSelection(0);

        tilName.setError(null);
        tilLocation.setError(null);
        tilDate.setError(null);
        tilDescription.setError(null);
        tilDuration.setError(null);
    }

    private boolean validateInput() {

        boolean isValid = true;

        // Xóa lỗi cũ
        tilName.setError(null);
        tilLocation.setError(null);
        tilDate.setError(null);
        tilDuration.setError(null);
        tilDescription.setError(null);

        // Hike Name
        if (edtName.getText().toString().trim().isEmpty()) {
            tilName.setError("Please enter hike name");
            isValid = false;
        }

        // Location
        if (edtLocation.getText().toString().trim().isEmpty()) {
            tilLocation.setError("Please enter location");
            isValid = false;
        }

        // Date
        if (edtDate.getText().toString().trim().isEmpty()) {
            tilDate.setError("Please select date");
            isValid = false;
        }


        // Estimated Duration
        String duration = edtDuration.getText().toString().trim();

        if (duration.isEmpty()) {
            tilDuration.setError("Please enter estimated duration");
            isValid = false;
        } else {
            try {
                int hour = Integer.parseInt(duration);

                if (hour <= 0) {
                    tilDuration.setError("Duration must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilDuration.setError("Invalid duration");
                isValid = false;
            }
        }

        if (rgParking.getCheckedRadioButtonId() == -1) {
            txtParkingError.setText("Please select parking option");
            txtParkingError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            txtParkingError.setVisibility(View.GONE);
        }

        return isValid;
    }
}