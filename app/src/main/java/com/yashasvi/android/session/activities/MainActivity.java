package com.yashasvi.android.session.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yashasvi.android.session.R;
import com.yashasvi.android.session.adapters.CitiesAdapter;
import com.yashasvi.android.session.db.CitiesDAO;
import com.yashasvi.android.session.models.City;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    private void initialize() {
        initializeDatabase();
        initializeViews();
        initializeAddContactDialog();
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
        CitiesAdapter citiesAdapter = new CitiesAdapter(cities);
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
    }

    @Override
    protected void onResume() {
        citiesDAO.open();
        super.onResume();
    }
}
