package com.ssc.sscapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyActivity extends AppCompatActivity {

    private RecyclerView PartnoRecycler;
    private List<PartNo> partNoList;
    private PartNoAdapter adapter;
    private FloatingActionButton addbtn;
    private DatabaseReference PartNoRef;
    private String partnorefstring;
    private ProgressDialog mprogressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        final String companyName = getIntent().getStringExtra("Company name");
        Toast.makeText(this,companyName,Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.companyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(companyName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("please wait while we load the data");
        mprogressdialog.setMessage("fetching data");
        mprogressdialog.show();

        PartnoRecycler = findViewById(R.id.partnoRecycler);
        PartnoRecycler.setHasFixedSize(true);
        PartnoRecycler.setLayoutManager(new LinearLayoutManager(this));

        partNoList = new ArrayList<>();
        adapter = new PartNoAdapter(this, partNoList);
        PartnoRecycler.setAdapter(adapter);


         PartNoRef= FirebaseDatabase.getInstance().getReference().child("Companies").child(companyName);
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("PartNo").orderByChild("companyname").equalTo(companyName);

        addbtn = findViewById(R.id.addpartnoBtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = new Intent(getApplicationContext(), AddPartNo.class);
                AddIntent.putExtra("Company name",companyName);
                startActivity(AddIntent);
            }
        });

        query.addValueEventListener(valueEventListener);


        //mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
    }

    public ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            partNoList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     PartNo partNo= snapshot.getValue(PartNo.class);
                    partNoList.add(partNo);
                }
                adapter.notifyDataSetChanged();
                mprogressdialog.dismiss();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mprogressdialog.dismiss();
            Toast.makeText(getApplicationContext()  ,"an error occured while fetching data",Toast.LENGTH_SHORT).show();

        }
    };

    private class PartNoAdapter extends RecyclerView.Adapter<PartNoViewHolder>{
        private Context mctx;
        private List<PartNo> partNoList;
        public PartNoAdapter(Context mctx, List<PartNo> partNoList) {

            this.partNoList= partNoList;
            this.mctx = mctx;
        }

        @NonNull
        @Override
        public PartNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(mctx).inflate(R.layout.single_name_layout, parent, false);
            return new PartNoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PartNoViewHolder partNoViewHolder, final int i) {
            PartNo partNo = partNoList.get(i);
            partNoViewHolder.name.setText(partNo.name);

            partNoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    partnorefstring = partNoList.get(i).name;
                    Intent profileIntent = new Intent(CompanyActivity.this, PartNoDetails.class);
                    profileIntent.putExtra("partnorefstring", partnorefstring);
                    startActivity(profileIntent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return partNoList.size();
        }
    }

    private class PartNoViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        public PartNoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);


        }
    }
}