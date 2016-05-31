package com.yashasvi.android.session.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yashasvi.android.session.R;
import com.yashasvi.android.session.models.City;
import com.yashasvi.android.session.viewholders.CitiesViewHolder;

import java.util.List;


public class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder> {

    private List<City> cities;

    public CitiesAdapter(List<City> cities) {
        this.cities = cities;
    }

    public void addContact(City contact) {
        cities.add(contact);
        notifyDataSetChanged();
    }

    @Override
    public CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_row, parent, false);
        return new CitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CitiesViewHolder holder, int position) {
        City city = cities.get(position);
        holder.name.setText(city.getName());
        holder.description.setText(city.getDescription());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
