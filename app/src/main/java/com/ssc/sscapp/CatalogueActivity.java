package com.ssc.sscapp;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private String companyname;

    private DatabaseReference mCompanyRef;
    private FloatingActionButton addbtn;

    private android.support.v7.widget.Toolbar CatalogToolbar;
    private RecyclerView CompanyNamesRecycler;

    private List<Companies> companiesList;
    private CompaniesAdapter adapter;
    private ProgressDialog mprogressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        CatalogToolbar = findViewById(R.id.catalogueappbar);
        setSupportActionBar(CatalogToolbar);
        getSupportActionBar().setTitle("Catalogue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we load the data");
        mprogressdialog.setMessage("Fetching data...");
        mprogressdialog.show();

        CompanyNamesRecycler = findViewById(R.id.companynamesrecycler);
        CompanyNamesRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CompanyNamesRecycler.setLayoutManager(layoutManager);


        companiesList = new ArrayList<>();
        adapter = new CompaniesAdapter(this, companiesList);
        CompanyNamesRecycler.setAdapter(adapter);


        mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Companies").orderByChild("name");

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



    public ValueEventListener valueEventListener = new ValueEventListener() {
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
            else
            {
                Toast.makeText(getApplicationContext(),"no companies found",Toast.LENGTH_SHORT).show();
            }
            mprogressdialog.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mprogressdialog.dismiss();
            Toast.makeText(getApplicationContext()  ,"an error occured while fetching data",Toast.LENGTH_SHORT).show();

        }

    };

    class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder> {

        private Context mctx;
        private List<Companies> CompaniesList;


        public CompaniesAdapter (Context mctx, List<Companies> CompaniesList){
            this.mctx = mctx;
            this.CompaniesList = CompaniesList;
        }

        @NonNull
        @Override
        public CompaniesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(mctx).inflate(R.layout.single_name_layout, parent, false);

            return new CompaniesViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull CompaniesViewHolder CompaniesViewHolder, final int i) {
            Companies Companies = CompaniesList.get(i);
            CompaniesViewHolder.name.setText(Companies.name);


            CompaniesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyname = CompaniesList.get(i).name;
                    Intent profileIntent = new Intent(CatalogueActivity.this, CompanyActivity.class);
                    profileIntent.putExtra("Company name", companyname);
                    startActivity(profileIntent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return CompaniesList.size();
        }

        public class CompaniesViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public CompaniesViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);


            }
        }
    }
}