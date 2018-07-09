package com.yashasvi.android.session.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yashasvi.android.session.R;
import com.yashasvi.android.session.adapters.CitiesAdapter;
import com.yashasvi.android.session.db.CitiesDAO;
import com.yashasvi.android.session.models.City;
import com.yashasvi.android.session.services.CitiesDownloadService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    CitiesAdapter citiesAdapter;
    FloatingActionButton addContactFAButton;

    AlertDialog.Builder dialogBuilder;
    AlertDialog addContactDialog;

    EditText etName, etDescription;

    private List<City> cities;

    CitiesDAO citiesDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        syncContactsWithServer();
    }

    private void initialize() {
        initializeDatabase();
        initializeViews();
        initializeAddContactDialog();
    }

    private void syncContactsWithServer() {
        String url = "https://raw.githubusercontent.com/yashasvigirdhar/SimpleTextHosting/master/cities.json";
        Intent intent = new Intent(this, CitiesDownloadService.class);
        intent.putExtra(CitiesDownloadService.CITIES_URL, url);
        startService(intent);
    }

    private void initializeDatabase() {
        citiesDAO = new CitiesDAO(this);
        citiesDAO.open();
        cities = citiesDAO.getAllCities();
        citiesAdapter = new CitiesAdapter(cities);
    }

    private void initializeViews() {

        addContactFAButton = (FloatingActionButton) findViewById(R.id.fabAddContact);
        addContactFAButton.setOnClickListener(this);

        RecyclerView citiesRView = (RecyclerView) findViewById(R.id.recyclerviewCities);
        citiesRView.setAdapter(citiesAdapter);
        citiesRView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initializeAddContactDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_contact_dialog, null);
        dialogView.findViewById(R.id.bOk).setOnClickListener(this);
        dialogView.findViewById(R.id.bCancel).setOnClickListener(this);
        etName = (EditText) dialogView.findViewById(R.id.etName);
        etDescription = (EditText) dialogView.findViewById(R.id.etDescription);
        dialogBuilder.setView(dialogView);
        addContactDialog = dialogBuilder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddContact:
                addContactDialog.show();
                break;
            case R.id.bOk:
                if (validateInput()) {
                    City city = citiesDAO.addCity(etName.getText().toString(), etDescription.getText().toString());
                    citiesAdapter.addContact(city);
                }
            case R.id.bCancel:
                etName.setText("");
                etDescription.setText("");
                etName.requestFocus();
                if (addContactDialog.isShowing())
                    addContactDialog.dismiss();
                break;
        }
    }

    private boolean validateInput() {
        if (etName.getText().toString().length() > 0 || etDescription.getText().toString().length() > 0)
            return true;
        Toast.makeText(this, "Name or Description Can't be empty", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onPause() {
        citiesDAO.close();
        super.onPause();
        unregisterReceiver(receiver);

    }

    @Override
    protected void onResume() {
        citiesDAO.open();
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CitiesDownloadService.RECEIVER_FILTER));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String jsonString = bundle.getString(CitiesDownloadService.CITIES_KEY);
                Type collectionType = new TypeToken<List<City>>() {
                }.getType();
                try {
                    List<City> receivedCities = (ArrayList<City>) new Gson().fromJson(jsonString, collectionType);
                    boolean exists;
                    for (int i = 0; i < receivedCities.size(); i++) {
                        exists = false;
                        City curCity = receivedCities.get(i);
                        for (int j = 0; j < cities.size(); j++) {
                            if (curCity.getName().equals(cities.get(j).getName())) {
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            Log.d(LOG_TAG, "doesn't exists : " + curCity.getName());
                            final City contact = citiesDAO.addCity(curCity.getName(), curCity.getDescription());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    citiesAdapter.addContact(contact);
                                }
                            });

                        } else {
                            Log.d(LOG_TAG, "already exists : " + curCity.getName());
                        }
                    }

                    for (int i = 0; i < cities.size(); i++)
                        Log.i(LOG_TAG, cities.get(i).getName());

                } catch (JsonSyntaxException e) {
                    //not able to parse response, after requesting all places

                }
            }
        }
    };
}
