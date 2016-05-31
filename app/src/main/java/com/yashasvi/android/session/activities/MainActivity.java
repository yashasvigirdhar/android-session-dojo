package com.yashasvi.android.session.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yashasvi.android.session.R;
import com.yashasvi.android.session.adapters.CitiesAdapter;
import com.yashasvi.android.session.models.City;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDataset();
        initializeViews();

    }

    private void initializeDataset() {
        cities = new ArrayList<>();
        cities.add(new City("Bali", "Canggu"));
        cities.add(new City("Bali", "Ubud"));
        cities.add(new City("Thailand", "Chiang Mai"));
        cities.add(new City("Hungary", "Budapest"));

    }

    private void initializeViews() {
        RecyclerView contactsRView = (RecyclerView) findViewById(R.id.recyclerviewCities);
        CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
        contactsRView.setAdapter(citiesAdapter);
        contactsRView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
