package com.ssc.sscapp;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CatalogueActivity extends AppCompatActivity {


    private DatabaseReference mCompanyRef;
    private FloatingActionButton addbtn;

    private android.support.v7.widget.Toolbar CatalogToolbar;
    private RecyclerView CompanyNamesRecycler;

    private List<Companies> companiesList;
    private CompaniesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        CatalogToolbar = findViewById(R.id.catalogueappbar);
        setSupportActionBar(CatalogToolbar);
        getSupportActionBar().setTitle("Catalogue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CompanyNamesRecycler = findViewById(R.id.companynamesrecycler);
        CompanyNamesRecycler.setHasFixedSize(true);
        CompanyNamesRecycler.setLayoutManager(new LinearLayoutManager(this));

        companiesList = new ArrayList<>();
        adapter = new CompaniesAdapter(this, companiesList);
        CompanyNamesRecycler.setAdapter(adapter);


        mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Companies");

        addbtn = findViewById(R.id.addBtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = new Intent(getApplicationContext(), AddCompActivity.class);
                startActivity(AddIntent);
            }
        });

        query.addValueEventListener(valueEventListener);


        //mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            companiesList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Companies companies = snapshot.getValue(Companies.class);
                    companiesList.add(companies);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

