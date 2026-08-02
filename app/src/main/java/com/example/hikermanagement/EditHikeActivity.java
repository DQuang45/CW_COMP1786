package com.example.hikermanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class EditHikeActivity extends AppCompatActivity {

    // TextInputLayout
    private TextInputLayout tilEditName, tilEditLocation, tilEditDate
            , tilEditDuration, tilEditLength, tilEditDescription;


    // EditText
    private TextInputEditText edtEditName, edtEditLocation, edtEditDate
            , edtEditDescription, edtEditDuration, edtEditLength;


    // Parking
    private RadioGroup rgEditParking;

    // Spinner
    private Spinner spEditDifficulty;
    private Spinner spEditWeather;

    // Button
    private Button btnUpdateHike;

    // Database
    private DatabaseHelper databaseHelper;

    // ID của Hike đang sửa
    private int hikeId;

    // Dữ liệu Spinner
    private final String[] difficultyOptions = {
            "Easy",
            "Medium",
            "Hard"
    };

    private final String[] weatherOptions = {
            "Sunny",
            "Cloudy",
            "Rainy",
            "Windy"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);

        databaseHelper = new DatabaseHelper(this);

        bindViews();


        setupSpinners();

        receiveHikeData();

        setupDatePicker();

        btnUpdateHike.setOnClickListener(v -> {

            if (validateInput()) {
                showUpdateConfirmation();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Edit Hike");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindViews() {

        tilEditName = findViewById(R.id.tilEditName);
        tilEditLocation = findViewById(R.id.tilEditLocation);
        tilEditDate = findViewById(R.id.tilEditDate);
        tilEditDescription = findViewById(R.id.tilEditDescription);
        tilEditDuration = findViewById(R.id.tilEditDuration);

        edtEditName = findViewById(R.id.edtEditName);
        edtEditLocation = findViewById(R.id.edtEditLocation);
        edtEditDate = findViewById(R.id.edtEditDate);
        edtEditDescription = findViewById(R.id.edtEditDescription);
        edtEditDuration = findViewById(R.id.edtEditDuration);

        rgEditParking = findViewById(R.id.rgEditParking);

        tilEditLength = findViewById(R.id.tilEditLength);

        edtEditLength = findViewById(R.id.edtEditLength);

        spEditDifficulty = findViewById(R.id.spEditDifficulty);
        spEditWeather = findViewById(R.id.spEditWeather);

        btnUpdateHike = findViewById(R.id.btnUpdateHike);
    }


    private void setupSpinners() {

        ArrayAdapter<String> difficultyAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        difficultyOptions
                );

        difficultyAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spEditDifficulty.setAdapter(difficultyAdapter);

        ArrayAdapter<String> weatherAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        weatherOptions
                );

        weatherAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spEditWeather.setAdapter(weatherAdapter);
    }

    private void receiveHikeData() {

        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        String name =
                getIntent().getStringExtra("HIKE_NAME");

        String location =
                getIntent().getStringExtra("HIKE_LOCATION");

        String date =
                getIntent().getStringExtra("HIKE_DATE");

        String parking =
                getIntent().getStringExtra("HIKE_PARKING");

        int length =
                getIntent().getIntExtra("HIKE_LENGTH", 1);

        String difficulty =
                getIntent().getStringExtra("HIKE_DIFFICULTY");

        String description =
                getIntent().getStringExtra("HIKE_DESCRIPTION");

        String weather =
                getIntent().getStringExtra("HIKE_WEATHER");

        String duration =
                getIntent().getStringExtra("HIKE_DURATION");

        if (hikeId == -1) {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Hike information could not be loaded.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();

            return;
        }

        edtEditName.setText(name);
        edtEditLocation.setText(location);
        edtEditDate.setText(date);
        edtEditDescription.setText(description);
        edtEditDuration.setText(duration);
        edtEditLength.setText(String.valueOf(length));

        if ("YES".equalsIgnoreCase(parking)) {
            rgEditParking.check(R.id.rbEditYes);
        } else if ("NO".equalsIgnoreCase(parking)) {
            rgEditParking.check(R.id.rbEditNo);
        }

        setSpinnerSelection(
                spEditDifficulty,
                difficultyOptions,
                difficulty
        );

        setSpinnerSelection(
                spEditWeather,
                weatherOptions,
                weather
        );
    }

    private void setSpinnerSelection(
            Spinner spinner,
            String[] options,
            String selectedValue
    ) {

        if (selectedValue == null) {
            return;
        }

        for (int i = 0; i < options.length; i++) {

            if (options[i].equalsIgnoreCase(selectedValue)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setupDatePicker() {

        edtEditDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            EditHikeActivity.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                String selectedDate = String.format(
                                        "%02d/%02d/%04d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear
                                );

                                edtEditDate.setText(selectedDate);
                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });
    }

    private boolean validateInput() {

        boolean isValid = true;

        tilEditLength.setError(null);
        tilEditName.setError(null);
        tilEditLocation.setError(null);
        tilEditDate.setError(null);
        tilEditDuration.setError(null);

        String name = getText(edtEditName);
        String location = getText(edtEditLocation);
        String date = getText(edtEditDate);
        String duration = getText(edtEditDuration);
        String lengthText = getText(edtEditLength);

        if (name.isEmpty()) {
            tilEditName.setError("Please enter hike name");
            isValid = false;
        }

        if (location.isEmpty()) {
            tilEditLocation.setError("Please enter location");
            isValid = false;
        }

        if (date.isEmpty()) {
            tilEditDate.setError("Please select date");
            isValid = false;
        }

        if (lengthText.isEmpty()) {

            tilEditLength.setError(
                    "Please enter hike length"
            );

            isValid = false;

        } else {

            try {

                int length =
                        Integer.parseInt(lengthText);

                if (length <= 0) {

                    tilEditLength.setError(
                            "Length must be greater than 0"
                    );

                    isValid = false;
                }

            } catch (NumberFormatException e) {

                tilEditLength.setError(
                        "Please enter a valid length"
                );

                isValid = false;
            }
        }

        if (duration.isEmpty()) {
            tilEditDuration.setError(
                    "Please enter estimated duration"
            );
            isValid = false;
        }

        if (rgEditParking.getCheckedRadioButtonId() == -1) {

            new AlertDialog.Builder(this)
                    .setTitle("Validation")
                    .setMessage("Please select parking option.")
                    .setPositiveButton("OK", null)
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

    private void showUpdateConfirmation() {

        String name = getText(edtEditName);
        String location = getText(edtEditLocation);
        String date = getText(edtEditDate);
        String description = getText(edtEditDescription);
        String duration = getText(edtEditDuration);
        int length = Integer.parseInt(getText(edtEditLength));

        String difficulty =
                spEditDifficulty.getSelectedItem().toString();

        String weather =
                spEditWeather.getSelectedItem().toString();

        String parking;

        int checkedParkingId =
                rgEditParking.getCheckedRadioButtonId();

        if (checkedParkingId == R.id.rbEditYes) {
            parking = "YES";
        } else {
            parking = "NO";
        }

        String message =
                "Name: " + name
                        + "\nLocation: " + location
                        + "\nDate: " + date
                        + "\nParking: " + parking
                        + "\nLength: " + length + " km"
                        + "\nDifficulty: " + difficulty
                        + "\nDescription: " + description
                        + "\nWeather: " + weather
                        + "\nEstimated Duration: "
                        + duration + " hour(s)"
                        + "\n\nSave these changes?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage(message)
                .setPositiveButton("YES", (dialog, which) -> {

                    updateHike(
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

                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void updateHike(
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

        boolean result = databaseHelper.updateHike(
                hikeId,
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

            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Hike updated successfully.")
                    .setPositiveButton("OK", (dialog, which) -> {

                        finish();

                    })
                    .show();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Failed to update hike.")
                    .setPositiveButton("OK", null)
                    .show();
        }
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