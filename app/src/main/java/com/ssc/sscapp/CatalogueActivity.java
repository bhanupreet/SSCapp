package com.ssc.sscapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CatalogueActivity extends AppCompatActivity {

    private String companyname;

    private DatabaseReference mCompanyRef;
    private FloatingActionButton addbtn;

    private androidx.appcompat.widget.Toolbar CatalogToolbar;
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


            } else {
                Toast.makeText(getApplicationContext(), "no companies found", Toast.LENGTH_SHORT).show();
            }
            mprogressdialog.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mprogressdialog.dismiss();
            Toast.makeText(getApplicationContext(), "an error occured while fetching data", Toast.LENGTH_SHORT).show();

        }

    };

    class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder> {

        private Context mctx;
        private List<Companies> CompaniesList;


        public CompaniesAdapter(Context mctx, List<Companies> CompaniesList) {
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
        public void onBindViewHolder(@NonNull final CompaniesViewHolder CompaniesViewHolder, final int i) {
            final Companies Companies = CompaniesList.get(i);
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


            CompaniesViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder delete = new AlertDialog.Builder(CatalogueActivity.this);
                    CharSequence options[] = new CharSequence[]{"Delete"};
                    delete.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(CatalogueActivity.this);
                            alert.setTitle("Delete entry");
                            alert.setMessage("Are you sure you want to delete?");
                            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    companyname = CompaniesList.get(i).name;
                                    FirebaseDatabase.getInstance().getReference().child("Companies").child(Companies.name).removeValue();
                                    Query delete = FirebaseDatabase.getInstance().getReference().child("PartNo").orderByChild("companyname").equalTo(companyname);
                                    delete.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            String partnorefstring = dataSnapshot.getKey();
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageRef = storage.getReference();
                                            StorageReference desertRef = storageRef.child("itemimages/" + partnorefstring + ".jpg");
                                            desertRef.delete();
                                            dataSnapshot.getRef().setValue(null);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    // FirebaseDatabase.getInstance().getReference().child("PartNo").
                                    // Intent profileIntent = new Intent(addPartnoDetailsActivity.this, CompanyActivity.class);
                                }
                            });
                            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close dialog
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });

                    delete.show();
                    return true;
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