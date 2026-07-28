package com.example.hikermanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewHikesActivity extends AppCompatActivity {

    private ListView listViewHikes;
    private TextView txtEmpty;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hikes);

        listViewHikes = findViewById(R.id.listViewHikes);
        txtEmpty = findViewById(R.id.txtEmpty);

        databaseHelper = new DatabaseHelper(this);

        loadHikes();
    }

    private void loadHikes() {

        List<Hike> hikes = databaseHelper.getAllHikes();

        if (hikes.isEmpty()) {

            txtEmpty.setVisibility(View.VISIBLE);
            listViewHikes.setVisibility(View.GONE);

            return;
        }

        txtEmpty.setVisibility(View.GONE);
        listViewHikes.setVisibility(View.VISIBLE);

        List<String> hikeDisplayList = new ArrayList<>();

        for (Hike hike : hikes) {

            String hikeText =
                    "Name: " + hike.getName()
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

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
    }
}