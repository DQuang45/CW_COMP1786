package com.example.hikermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextInputEditText edtSearch;

    private Button btnAddHike;

    private ListView listViewHikes;

    private TextView txtEmpty;
    private TextView txtResultCount;

    private DatabaseHelper databaseHelper;

    private List<Hike> currentHikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        edtSearch = findViewById(R.id.edtSearch);
        btnAddHike = findViewById(R.id.btnAddHike);
        listViewHikes = findViewById(R.id.listViewHikes);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtResultCount = findViewById(R.id.txtResultCount);

        databaseHelper = new DatabaseHelper(this);

        btnAddHike.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence charSequence,
                    int start,
                    int count,
                    int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence charSequence,
                    int start,
                    int before,
                    int count) {

                String keyword = charSequence.toString().trim();

                searchHikes(keyword);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listViewHikes.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Hike selectedHike = currentHikeList.get(position);

                    showHikeDetails(selectedHike);
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

            currentHikeList = databaseHelper.searchHikes(keyword);

            displayHikes(currentHikeList);
        }
    }

    private void displayHikes(List<Hike> hikeList) {

        if (hikeList == null || hikeList.isEmpty()) {

            listViewHikes.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);

            txtResultCount.setText("0 hikes found");

            return;
        }

        listViewHikes.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);

        txtResultCount.setText(
                hikeList.size() + " hike(s) found"
        );

        List<String> hikeDisplayList = new ArrayList<>();

        for (Hike hike : hikeList) {

            String hikeText =
                    hike.getName()
                            + "\nLocation: " + hike.getLocation()
                            + "\nDate: " + hike.getDate()
                            + "\nLength: " + hike.getLength() + " km"
                            + "\nDifficulty: " + hike.getDifficulty();

            hikeDisplayList.add(hikeText);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        hikeDisplayList
                );

        listViewHikes.setAdapter(adapter);
    }

    private void showHikeDetails(Hike hike) {

        String message =
                "Name: " + hike.getName()
                        + "\n\nLocation: " + hike.getLocation()
                        + "\n\nDate: " + hike.getDate()
                        + "\n\nParking: " + hike.getParking()
                        + "\n\nLength: " + hike.getLength() + " km"
                        + "\n\nDifficulty: " + hike.getDifficulty()
                        + "\n\nDescription: " + hike.getDescription()
                        + "\n\nWeather: " + hike.getWeather()
                        + "\n\nEstimated Duration: "
                        + hike.getDuration() + " hour(s)";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Hike Details")
                .setMessage(message)
                .setPositiveButton("CLOSE", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (edtSearch != null) {

            String keyword =
                    edtSearch.getText() == null
                            ? ""
                            : edtSearch.getText()
                            .toString()
                            .trim();

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