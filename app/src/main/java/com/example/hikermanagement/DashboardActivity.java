package com.example.hikermanagement;

// Import necessary libraries for Activity, Intent, and UI handling
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    // EditText used to enter the search keyword "hike"
    private TextInputEditText edtSearch;

    // Buttons for adding a new hike and deleting all hikes
    private Button btnAddHike;
    private Button btnDeleteAllHikes;

    // RecyclerView used to display a list of hikes
    private RecyclerView recyclerViewHikes;

    // TextView displaying a message when there are no hikes
    // and the number of search results found
    private TextView txtEmpty;
    private TextView txtResultCount;

    // DatabaseHelper is used to perform database operations.
    private DatabaseHelper databaseHelper;

    // Adapter connecting Hike data to the RecyclerView
    private HikeAdapter hikeAdapter;

    // List of currently displayed hikes
    private List<Hike> currentHikeList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Load the dashboard layout
        setContentView(R.layout.activity_dashboard);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize UI components
        edtSearch = findViewById(R.id.edtSearch);

        btnAddHike = findViewById(R.id.btnAddHike);

        btnDeleteAllHikes = findViewById(R.id.btnDeleteAllHikes);

        recyclerViewHikes = findViewById(R.id.recyclerViewHikes);

        txtEmpty = findViewById(R.id.txtEmpty);

        txtResultCount = findViewById(R.id.txtResultCount);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Set a vertical layout manager for the RecyclerView
        recyclerViewHikes.setLayoutManager(new LinearLayoutManager(this));

        // Create the hike adapter and define the action
        // when a hike is selected
        hikeAdapter = new HikeAdapter(new ArrayList<>(), this::showHikeDetails);

        // Attach the adapter to the RecyclerView
        recyclerViewHikes.setAdapter(hikeAdapter);

        // Handle the Add Hike button click
        btnAddHike.setOnClickListener(v -> {

            // Create an Intent to open the MainActivity
            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            // Open the MainActivity
            startActivity(intent);
        });

        // Handle the Delete All Hikes button click
        btnDeleteAllHikes.setOnClickListener(v -> {

            // Show a confirmation dialog before deleting
            confirmDeleteAllHikes();

        });

        // Monitor changes in the search field
        edtSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence text,
                            int start,
                            int count,
                            int after
                    ) {

                    }

                    @Override
                    public void onTextChanged(
                            CharSequence text,
                            int start,
                            int before,
                            int count
                    ) {

                        searchHikes(
                                text.toString().trim()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable editable
                    ) {

                    }
                }
        );

        loadAllHikes();
    }

    private void loadAllHikes() {

        currentHikeList = databaseHelper.getAllHikes();

        displayHikes(currentHikeList);
    }

    private void searchHikes(String keyword) {

        if (keyword.isEmpty()) {

            loadAllHikes();

        } else {

            // Search the database using the keyword
            currentHikeList = databaseHelper.searchHikes(keyword);

            displayHikes(currentHikeList);
        }
    }

    private void displayHikes(
            List<Hike> hikeList
    ) {

        if (hikeList == null
                || hikeList.isEmpty()) {

            recyclerViewHikes.setVisibility(
                    View.GONE
            );

            txtEmpty.setVisibility(
                    View.VISIBLE
            );

            txtResultCount.setText(
                    "0 hikes found"
            );

            hikeAdapter.updateData(
                    new ArrayList<>()
            );

            return;
        }

        recyclerViewHikes.setVisibility(
                View.VISIBLE
        );

        txtEmpty.setVisibility(
                View.GONE
        );

        txtResultCount.setText(
                hikeList.size()
                        + " hike(s) found"
        );

        hikeAdapter.updateData(hikeList);
    }

    // Display the details of the selected hike
    private void showHikeDetails(Hike hike) {

        String message =
                "Name: " + hike.getName()
                        + "\n\nLocation: "
                        + hike.getLocation()
                        + "\n\nDate: "
                        + hike.getDate()
                        + "\n\nParking: "
                        + hike.getParking()
                        + "\n\nLength: "
                        + hike.getLength()
                        + " km"
                        + "\n\nDifficulty: "
                        + hike.getDifficulty()
                        + "\n\nDescription: "
                        + hike.getDescription()
                        + "\n\nWeather: "
                        + hike.getWeather()
                        + "\n\nEstimated Duration: "
                        + hike.getDuration()
                        + " hour(s)";

        new AlertDialog.Builder(this)
                .setTitle("Hike Details")
                .setMessage(message)

                .setPositiveButton(
                        "EDIT",
                        (dialog, which) ->
                                openEditHike(hike)
                )

                .setNegativeButton(
                        "DELETE",
                        (dialog, which) ->
                                confirmDeleteHike(hike)
                )

                .setNeutralButton(
                        "OBSERVATIONS",
                        (dialog, which) ->
                                openObservations(hike)
                )

                .show();
    }

    private void openEditHike(Hike hike) {

        Intent intent = new Intent(
                DashboardActivity.this,
                EditHikeActivity.class
        );

        intent.putExtra(
                "HIKE_ID",
                hike.getId()
        );

        intent.putExtra(
                "HIKE_NAME",
                hike.getName()
        );

        intent.putExtra(
                "HIKE_LOCATION",
                hike.getLocation()
        );

        intent.putExtra(
                "HIKE_DATE",
                hike.getDate()
        );

        intent.putExtra(
                "HIKE_PARKING",
                hike.getParking()
        );

        intent.putExtra(
                "HIKE_LENGTH",
                hike.getLength()
        );

        intent.putExtra(
                "HIKE_DIFFICULTY",
                hike.getDifficulty()
        );

        intent.putExtra(
                "HIKE_DESCRIPTION",
                hike.getDescription()
        );

        intent.putExtra(
                "HIKE_WEATHER",
                hike.getWeather()
        );

        intent.putExtra(
                "HIKE_DURATION",
                hike.getDuration()
        );

        startActivity(intent);
    }

    private void openObservations(Hike hike) {

        Intent intent = new Intent(
                DashboardActivity.this,
                ObservationListActivity.class
        );

        intent.putExtra(
                "HIKE_ID",
                hike.getId()
        );

        intent.putExtra(
                "HIKE_NAME",
                hike.getName()
        );

        startActivity(intent);
    }

    private void confirmDeleteHike(
            Hike hike
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage(
                        "Are you sure you want to delete \""
                                + hike.getName()
                                + "\"?"
                )

                .setPositiveButton(
                        "YES",
                        (dialog, which) -> {

                            boolean result =
                                    databaseHelper
                                            .deleteHike(
                                                    hike.getId()
                                            );

                            if (result) {

                                new AlertDialog.Builder(
                                        this
                                )
                                        .setTitle(
                                                "Success"
                                        )
                                        .setMessage(
                                                "Hike deleted successfully."
                                        )
                                        .setPositiveButton(
                                                "OK",
                                                (successDialog,
                                                 successWhich) ->
                                                        loadAllHikes()
                                        )
                                        .show();

                            } else {

                                new AlertDialog.Builder(
                                        this
                                )
                                        .setTitle("Error")
                                        .setMessage(
                                                "Failed to delete hike."
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

    @Override
    protected void onResume() {

        super.onResume();

        if (edtSearch != null) {

            String keyword = "";

            if (edtSearch.getText() != null) {

                keyword = edtSearch
                        .getText()
                        .toString()
                        .trim();
            }

            searchHikes(keyword);
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

    private void confirmDeleteAllHikes() {

        // Check whether there are any hikes to delete
        if (currentHikeList == null
                || currentHikeList.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("No Hikes")
                    .setMessage(
                            "There are no hikes to delete."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();

            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete All Hikes")
                .setMessage(
                        "Are you sure you want to delete all hikes?\n\n"
                                + "All related observations will also be deleted.\n\n"
                                + "This action cannot be undone."
                )
                .setPositiveButton(
                        "DELETE ALL",
                        (dialog, which) -> {

                            deleteAllHikes();

                        }
                )
                .setNegativeButton(
                        "CANCEL",
                        null
                )
                .show();
    }
    private void deleteAllHikes() {

        // Delete all hikes using the database helper
        boolean result = databaseHelper.deleteAllHikes();

        if (result) {

            edtSearch.setText("");

            currentHikeList =
                    new ArrayList<>();

            displayHikes(currentHikeList);

            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage(
                            "All hikes and observations were deleted successfully."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(
                            "Failed to delete all hikes."
                    )
                    .setPositiveButton(
                            "OK",
                            null
                    )
                    .show();
        }
    }
}