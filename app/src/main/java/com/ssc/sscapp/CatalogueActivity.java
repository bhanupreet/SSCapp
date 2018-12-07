package com.ssc.sscapp;

import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class CatalogueActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar CatalogToolbar;
    private RecyclerView CompanyNamesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        CatalogToolbar = findViewById(R.id.catalogueappbar);
        setSupportActionBar(CatalogToolbar);
        getSupportActionBar().setTitle("Catalogue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CompanyNamesList = findViewById(R.id.CompanyNamesList);
        CompanyNamesList.setHasFixedSize(true);
        CompanyNamesList.setLayoutManager(new LinearLayoutManager(this));


    }
}
