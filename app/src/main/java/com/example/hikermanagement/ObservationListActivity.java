package com.example.hikermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.List;


public class ObservationListActivity
        extends AppCompatActivity {

    private TextView txtHikeObservationName;
    private TextView txtEmptyObservation;

    private Button btnAddObservation;

    private RecyclerView recyclerViewObservations;

    private DatabaseHelper databaseHelper;
    private ObservationAdapter observationAdapter;

    private int hikeId;
    private String hikeName;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_observation_list
        );

        txtHikeObservationName =
                findViewById(
                        R.id.txtHikeObservationName
                );

        txtEmptyObservation =
                findViewById(
                        R.id.txtEmptyObservation
                );

        btnAddObservation =
                findViewById(
                        R.id.btnAddObservation
                );

        recyclerViewObservations =
                findViewById(
                        R.id.recyclerViewObservations
                );

        hikeId = getIntent().getIntExtra(
                "HIKE_ID",
                -1
        );

        hikeName = getIntent().getStringExtra(
                "HIKE_NAME"
        );

        if (hikeId == -1) {

            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(
                            "Hike information could not be loaded."
                    )
                    .setPositiveButton(
                            "OK",
                            (dialog, which) -> finish()
                    )
                    .show();

            return;
        }

        txtHikeObservationName.setText(
                hikeName
        );

        databaseHelper =
                new DatabaseHelper(this);

        recyclerViewObservations.setLayoutManager(
                new LinearLayoutManager(this)
        );

        observationAdapter =
                new ObservationAdapter(
                        new ArrayList<>(),
                        this::showObservationOptions
                );

        recyclerViewObservations.setAdapter(
                observationAdapter
        );

        btnAddObservation.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ObservationListActivity.this,
                    AddObservationActivity.class
            );

            intent.putExtra(
                    "HIKE_ID",
                    hikeId
            );

            intent.putExtra(
                    "HIKE_NAME",
                    hikeName
            );

            startActivity(intent);
        });

        loadObservations();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadObservations() {

        List<Observation> list =
                databaseHelper
                        .getObservationsByHikeId(
                                hikeId
                        );

        if (list.isEmpty()) {

            txtEmptyObservation.setVisibility(
                    View.VISIBLE
            );

            recyclerViewObservations.setVisibility(
                    View.GONE
            );

        } else {

            txtEmptyObservation.setVisibility(
                    View.GONE
            );

            recyclerViewObservations.setVisibility(
                    View.VISIBLE
            );
        }

        observationAdapter.updateData(list);
    }

    private void showObservationOptions(
            Observation observation
    ) {

        String comment =
                observation.getComment();

        if (comment == null ||
                comment.trim().isEmpty()) {

            comment = "No comment";
        }

        String message =
                "Observation: "
                        + observation.getObservation()
                        + "\n\nTime: "
                        + observation.getObservationTime()
                        + "\n\nComment: "
                        + comment;

        new AlertDialog.Builder(this)
                .setTitle("Observation Details")
                .setMessage(message)

                .setPositiveButton(
                        "EDIT",
                        (dialog, which) -> {

                            openEditObservation(
                                    observation
                            );
                        }
                )

                .setNegativeButton(
                        "DELETE",
                        (dialog, which) -> {

                            confirmDeleteObservation(
                                    observation
                            );
                        }
                )

                .setNeutralButton(
                        "CLOSE",
                        null
                )

                .show();
    }

    private void openEditObservation(
            Observation observation
    ) {


        Intent intent = new Intent(
                ObservationListActivity.this,
                EditObservationActivity.class
        );

        intent.putExtra(
                "OBSERVATION_ID",
                observation.getId()
        );

        intent.putExtra(
                "HIKE_ID",
                hikeId
        );

        intent.putExtra(
                "HIKE_NAME",
                hikeName
        );

        intent.putExtra(
                "OBSERVATION_CONTENT",
                observation.getObservation()
        );

        intent.putExtra(
                "OBSERVATION_TIME",
                observation.getObservationTime()
        );

        intent.putExtra(
                "OBSERVATION_COMMENT",
                observation.getComment()
        );

        startActivity(intent);
    }
    private void confirmDeleteObservation(
            Observation observation
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage(
                        "Are you sure you want to delete this observation?"
                )
                .setPositiveButton(
                        "YES",
                        (dialog, which) -> {

                            boolean result =
                                    databaseHelper
                                            .deleteObservation(
                                                    observation.getId()
                                            );

                            if (result) {

                                loadObservations();

                            } else {

                                new AlertDialog.Builder(this)
                                        .setTitle("Error")
                                        .setMessage(
                                                "Failed to delete observation."
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

        if (databaseHelper != null &&
                hikeId != -1) {

            loadObservations();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}