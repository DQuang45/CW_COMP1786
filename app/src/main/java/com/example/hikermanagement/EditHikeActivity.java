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

    // Layout containers used to display validation errors
    private TextInputLayout tilEditName, tilEditLocation, tilEditDate
            , tilEditDuration, tilEditLength, tilEditDescription;


    // Input fields for editing hike information
    private TextInputEditText edtEditName, edtEditLocation, edtEditDate
            , edtEditDescription, edtEditDuration, edtEditLength;


    // RadioGroup used to select the parking option
    private RadioGroup rgEditParking;

    // Spinners used to select difficulty and weather
    private Spinner spEditDifficulty;
    private Spinner spEditWeather;

    // Button used to update the hike
    private Button btnUpdateHike;

    // Helper class for database operations
    private DatabaseHelper databaseHelper;

    // ID of the hike currently being edited
    private int hikeId;

    // Available difficulty options
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
        // Load the edit hike layout
        setContentView(R.layout.activity_edit_hike);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Connect Java variables with XML views
        bindViews();

        // Configure the difficulty and weather spinners
        setupSpinners();

        // Receive and display the selected hike data
        receiveHikeData();

        // Configure the date picker
        setupDatePicker();

        // Validate the input before showing the update confirmation
        btnUpdateHike.setOnClickListener(v -> {

            if (validateInput()) {
                showUpdateConfirmation();
            }
        });
        // Configure the ActionBar for the edit screen
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Edit Hike");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Initialize and connect all UI components
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


    // Set up adapters and options for the spinners
    private void setupSpinners() {

        // Create an adapter for the difficulty spinner
        ArrayAdapter<String> difficultyAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        difficultyOptions
                );

        // Define the layout used for the dropdown list
        difficultyAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        // Attach the difficulty adapter to the spinner
        spEditDifficulty.setAdapter(difficultyAdapter);

        // Create an adapter for the weather spinner
        ArrayAdapter<String> weatherAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        weatherOptions
                );

        // Define the layout used for the dropdown list
        weatherAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        // Attach the weather adapter to the spinner
        spEditWeather.setAdapter(weatherAdapter);
    }

    // Retrieve the selected hike data passed from the previous Activity
    private void receiveHikeData() {
        // Retrieve the hike ID from the Intent

        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        // Retrieve the existing hike information
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

        // Check whether a valid hike ID was received
        if (hikeId == -1) {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Hike information could not be loaded.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();

            return;
        }

        // Populate the input fields with the existing hike data
        edtEditName.setText(name);
        edtEditLocation.setText(location);
        edtEditDate.setText(date);
        edtEditDescription.setText(description);
        edtEditDuration.setText(duration);
        edtEditLength.setText(String.valueOf(length));

        // Set the correct parking option
        if ("YES".equalsIgnoreCase(parking)) {
            rgEditParking.check(R.id.rbEditYes);
        } else if ("NO".equalsIgnoreCase(parking)) {
            rgEditParking.check(R.id.rbEditNo);
        }

        // Set the selected difficulty value
        setSpinnerSelection(
                spEditDifficulty,
                difficultyOptions,
                difficulty
        );

        // Set the selected weather value
        setSpinnerSelection(
                spEditWeather,
                weatherOptions,
                weather
        );
    }

    // Select the matching value in a Spinner
    private void setSpinnerSelection(
            Spinner spinner,
            String[] options,
            String selectedValue
    ) {

        // Stop if there is no selected value
        if (selectedValue == null) {
            return;
        }

        // Search for the matching option
        for (int i = 0; i < options.length; i++) {

            if (options[i].equalsIgnoreCase(selectedValue)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setupDatePicker() {

        edtEditDate.setOnClickListener(v -> {

            // Get the current date
            Calendar calendar = Calendar.getInstance();

            // Create a date picker dialog
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            EditHikeActivity.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                // Format the selected date
                                String selectedDate = String.format(
                                        "%02d/%02d/%04d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear
                                );

                                // Display the selected date
                                edtEditDate.setText(selectedDate);
                            },
                            year,
                            month,
                            day
                    );

            // Display the date picker
            datePickerDialog.show();
        });
    }

    // Validate all required input fields
    private boolean validateInput() {

        boolean isValid = true;

        // Clear previous validation errors
        tilEditLength.setError(null);
        tilEditName.setError(null);
        tilEditLocation.setError(null);
        tilEditDate.setError(null);
        tilEditDuration.setError(null);

        // Retrieve and clean input values
        String name = getText(edtEditName);
        String location = getText(edtEditLocation);
        String date = getText(edtEditDate);
        String duration = getText(edtEditDuration);
        String lengthText = getText(edtEditLength);

        // Validate the hike name
        if (name.isEmpty()) {
            tilEditName.setError("Please enter hike name");
            isValid = false;
        }

        // Validate the hike location
        if (location.isEmpty()) {
            tilEditLocation.setError("Please enter location");
            isValid = false;
        }

        // Validate the hike date
        if (date.isEmpty()) {
            tilEditDate.setError("Please select date");
            isValid = false;
        }

        // Validate the hike length
        if (lengthText.isEmpty()) {

            tilEditLength.setError(
                    "Please enter hike length"
            );

            isValid = false;

        } else {

            try {

                // Convert the length from String to integer
                int length =
                        Integer.parseInt(lengthText);

                // Check that the length is greater than zero
                if (length <= 0) {

                    tilEditLength.setError(
                            "Length must be greater than 0"
                    );

                    isValid = false;
                }

            } catch (NumberFormatException e) {

                // Handle invalid numeric input
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

    // Safely retrieve and trim text from an input field
    private String getText(TextInputEditText editText) {

        // Return an empty string if the field contains no text
        if (editText.getText() == null) {
            return "";
        }

        // Convert the input to String and remove extra spaces
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

        // Build a summary of the updated information
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

                // Update the hike when the user confirms
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

                // Cancel the update
                .setNegativeButton("NO", null)
                .show();
    }

    // Update the selected hike in the database
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

        // Send the updated information to the database
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

        // Close the database connection when the Activity is destroyed
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