package com.example.hikermanagement;

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

public class DashboardActivity
        extends AppCompatActivity {

    private TextInputEditText edtSearch;

    private Button btnAddHike;

    private RecyclerView recyclerViewHikes;

    private TextView txtEmpty;
    private TextView txtResultCount;

    private DatabaseHelper databaseHelper;

    private HikeAdapter hikeAdapter;

    private List<Hike> currentHikeList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        edtSearch = findViewById(R.id.edtSearch);

        btnAddHike = findViewById(R.id.btnAddHike);

        recyclerViewHikes = findViewById(R.id.recyclerViewHikes);

        txtEmpty = findViewById(R.id.txtEmpty);

        txtResultCount = findViewById(R.id.txtResultCount);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewHikes.setLayoutManager(new LinearLayoutManager(this));

        hikeAdapter = new HikeAdapter(new ArrayList<>(), this::showHikeDetails);

        recyclerViewHikes.setAdapter(hikeAdapter);

        btnAddHike.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
        });

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

            currentHikeList =
                    databaseHelper.searchHikes(
                            keyword
                    );

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
                        "CLOSE",
                        null
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

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}