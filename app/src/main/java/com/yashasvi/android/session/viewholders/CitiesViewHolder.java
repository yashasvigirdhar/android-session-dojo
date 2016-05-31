package com.yashasvi.android.session.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yashasvi.android.session.R;

public class CitiesViewHolder extends RecyclerView.ViewHolder {

    public TextView name, description;

    public CitiesViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tvName);
        description = (TextView) itemView.findViewById(R.id.tvDescription);
    }
}
