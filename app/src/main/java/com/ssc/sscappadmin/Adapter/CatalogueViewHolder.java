package com.ssc.sscappadmin.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.sscappadmin.R;

public class CatalogueViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    CardView layout;

    public CatalogueViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        layout = itemView.findViewById(R.id.companyname_layout);
    }
}
