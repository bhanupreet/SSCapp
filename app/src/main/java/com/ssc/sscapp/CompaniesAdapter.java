package com.ssc.sscapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder> {

    private Context mctx;
    private List<Companies> companiesList;


    public CompaniesAdapter (Context mctx, List<Companies> companiesList){
        this.mctx = mctx;
        this.companiesList = companiesList;
    }

    @NonNull
    @Override
    public CompaniesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mctx).inflate(R.layout.single_name_layout, parent, false);
        return new CompaniesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CompaniesViewHolder companiesViewHolder, int i) {
        Companies companies = companiesList.get(i);
        companiesViewHolder.name.setText(companies.name);
    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    public class CompaniesViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public CompaniesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);


        }
    }
}
