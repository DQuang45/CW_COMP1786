package com.example.hikermanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // TextInputLayout
    private TextInputLayout tilName, tilLocation, tilDate,
            tilDescription, tilDuration, tilLength;


    // EditText
    private TextInputEditText edtName, edtLocation, edtDate,
            edtDescription, edtDuration, edtLength;

    // RadioGroup
    private RadioGroup rgParking;

    // Parking validation message
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

        // TextInputLayout
        tilName = findViewById(R.id.tilName);
        tilLocation = findViewById(R.id.tilLocation);
        tilDate = findViewById(R.id.tilDate);
        tilDescription = findViewById(R.id.tilDescription);
        tilDuration = findViewById(R.id.tilDuration);

        // TextInputEditText
        edtName = findViewById(R.id.edtName);
        edtLocation = findViewById(R.id.edtLocation);
        edtDate = findViewById(R.id.edtDate);
        edtDescription = findViewById(R.id.edtDescription);
        edtDuration = findViewById(R.id.edtDuration);

        // Parking
        rgParking = findViewById(R.id.rgParking);
        txtParkingError = findViewById(R.id.txtParkingError);

        // Length
        tilLength = findViewById(R.id.tilLength);
        edtLength = findViewById(R.id.edtLength);

        // Spinner
        spDifficulty = findViewById(R.id.spDifficulty);
        spWeather = findViewById(R.id.spWeather);

        // Save button
        btnSave = findViewById(R.id.btnSave);

        /*
         * Difficulty is required.
         * The first option is a placeholder and is not a valid selection.
         */
        String[] difficultyOptions = {
                "Select difficulty",
                "Easy",
                "Medium",
                "Hard"
        };

        ArrayAdapter<String> difficultyAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        difficultyOptions
                );

        difficultyAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spDifficulty.setAdapter(difficultyAdapter);

        /*
         * Weather Forecast is one of the additional fields.
         */
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
                        weather
                );

        weatherAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spWeather.setAdapter(weatherAdapter);

        /*
         * Date is selected using DatePickerDialog
         * to reduce manual input and formatting errors.
         */
        edtDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            MainActivity.this,
                            (view,
                             selectedYear,
                             selectedMonth,
                             selectedDay) -> {

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

        /*
         * Validate first.
         * The confirmation dialog is displayed only when
         * all required fields are valid.
         */
        btnSave.setOnClickListener(v -> {

            if (validateInput()) {
                showConfirmationDialog();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Add Hike");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showConfirmationDialog() {

        String name =
                edtName.getText() == null
                        ? ""
                        : edtName.getText().toString().trim();

        String location =
                edtLocation.getText() == null
                        ? ""
                        : edtLocation.getText().toString().trim();

        String date =
                edtDate.getText() == null
                        ? ""
                        : edtDate.getText().toString().trim();

        String description =
                edtDescription.getText() == null
                        ? ""
                        : edtDescription.getText().toString().trim();

        String duration =
                edtDuration.getText() == null
                        ? ""
                        : edtDuration.getText().toString().trim();

        int length = Integer.parseInt(
                getText(edtLength)
        );
        String difficulty =
                spDifficulty.getSelectedItem() == null
                        ? ""
                        : spDifficulty.getSelectedItem()
                        .toString();

        String weather =
                spWeather.getSelectedItem() == null
                        ? ""
                        : spWeather.getSelectedItem()
                        .toString();

        String parking;

        int checkedId =
                rgParking.getCheckedRadioButtonId();

        if (checkedId == R.id.rbYes) {

            parking = "YES";

        } else if (checkedId == R.id.rbNo) {

            parking = "NO";

        } else {

            parking = "";
        }

        /*
         * Description and duration are not core required fields.
         * Empty optional values are displayed clearly for confirmation.
         */
        String displayedDescription =
                description.isEmpty()
                        ? "Not provided"
                        : description;

        String displayedDuration =
                duration.isEmpty()
                        ? "Not provided"
                        : duration + " hour(s)";

        String message =
                "Name: " + name +
                        "\n\nLocation: " + location +
                        "\n\nDate: " + date +
                        "\n\nParking: " + parking +
                        "\n\nLength: " + length + " km" +
                        "\n\nDifficulty: " + difficulty +
                        "\n\nDescription: " + displayedDescription +
                        "\n\nWeather: " + weather +
                        "\n\nEstimated Duration: " + displayedDuration +
                        "\n\nConfirm these hike details?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike")
                .setMessage(message)
                .setPositiveButton(
                        "YES",
                        (dialog, which) -> {

                            boolean result =
                                    databaseHelper.insertHike(
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

                                new AlertDialog.Builder(
                                        MainActivity.this
                                )
                                        .setTitle("Success")
                                        .setMessage(
                                                "Hike saved successfully."
                                        )
                                        .setPositiveButton(
                                                "OK",
                                                (successDialog,
                                                 successWhich) -> {

                                                    finish();
                                                }
                                        )
                                        .show();

                            } else {

                                new AlertDialog.Builder(
                                        MainActivity.this
                                )
                                        .setTitle("Error")
                                        .setMessage(
                                                "Failed to save hike."
                                        )
                                        .setPositiveButton(
                                                "OK",
                                                null
                                        )
                                        .show();
                            }
                        }
                )
                .setNegativeButton(
                        "NO",
                        null
                )
                .show();
    }

    private void clearForm() {

        edtName.setText("");
        edtLocation.setText("");
        edtDate.setText("");
        edtDescription.setText("");
        edtDuration.setText("");

        rgParking.clearCheck();

        txtParkingError.setVisibility(
                View.GONE
        );

        edtLength.setText("");

        /*
         * Position 0 is "Select difficulty".
         */
        spDifficulty.setSelection(0);

        if (spWeather.getAdapter() != null
                && spWeather.getCount() > 0) {

            spWeather.setSelection(0);
        }

        tilName.setError(null);
        tilLocation.setError(null);
        tilDate.setError(null);
        tilLength.setError(null);

        /*
         * Description and duration are optional,
         * but existing errors are cleared when resetting.
         */
        tilDescription.setError(null);
        tilDuration.setError(null);
    }

    private boolean validateInput() {

        boolean isValid = true;

        /*
         * Clear previous validation errors.
         */
        tilLength.setError(null);
        tilName.setError(null);
        tilLocation.setError(null);
        tilDate.setError(null);
        tilDescription.setError(null);
        tilDuration.setError(null);

        txtParkingError.setVisibility(
                View.GONE
        );

        String name =
                edtName.getText() == null
                        ? ""
                        : edtName.getText()
                        .toString()
                        .trim();

        String location =
                edtLocation.getText() == null
                        ? ""
                        : edtLocation.getText()
                        .toString()
                        .trim();

        String date =
                edtDate.getText() == null
                        ? ""
                        : edtDate.getText()
                        .toString()
                        .trim();
        String lengthText = getText(edtLength);

        /*
         * Name — Required
         */
        if (name.isEmpty()) {

            tilName.setError(
                    "Please enter hike name"
            );

            isValid = false;
        }

        /*
         * Location — Required
         */
        if (location.isEmpty()) {

            tilLocation.setError(
                    "Please enter location"
            );

            isValid = false;
        }

        /*
         * Date — Required
         */
        if (date.isEmpty()) {

            tilDate.setError(
                    "Please select date"
            );

            isValid = false;
        }

        /*
         * Parking — Required
         */
        if (rgParking.getCheckedRadioButtonId() == -1) {

            txtParkingError.setText(
                    "Please select parking option"
            );

            txtParkingError.setVisibility(
                    View.VISIBLE
            );

            isValid = false;
        }

        /*
         * Length — Required
         */
        if (lengthText.isEmpty()) {
            tilLength.setError("Please enter hike length");
            isValid = false;
        } else {
            try {
                int length = Integer.parseInt(lengthText);

                if (length <= 0) {
                    tilLength.setError(
                            "Length must be greater than 0"
                    );
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilLength.setError(
                        "Please enter a valid length"
                );
                isValid = false;
            }
        }

        /*
         * Difficulty — Required.
         * Position 0 is the placeholder "Select difficulty".
         */
        if (spDifficulty.getSelectedItemPosition() == 0) {

            new AlertDialog.Builder(this)
                    .setTitle("Difficulty Required")
                    .setMessage(
                            "Please select a difficulty level."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();

            isValid = false;
        }

        return isValid;
    }

    private String getText(TextInputEditText editText) {

        if (editText.getText() == null) {
            return "";
        }

        return editText.getText()
                .toString()
                .trim();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}