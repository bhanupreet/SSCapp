package com.ssc.sscapp;

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
    private List<String> partNoList;
    private PartNoAdapter adapter;
    private FloatingActionButton addbtn;
    private DatabaseReference PartNoRef;
    private String partnorefstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        final String companyName = getIntent().getStringExtra("Company name");
        Toast.makeText(this,companyName,Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.companyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(companyName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PartnoRecycler = findViewById(R.id.partnoRecycler);
        PartnoRecycler.setHasFixedSize(true);
        PartnoRecycler.setLayoutManager(new LinearLayoutManager(this));

        partNoList = new ArrayList<>();
        adapter = new PartNoAdapter(this, partNoList);
        PartnoRecycler.setAdapter(adapter);


         PartNoRef= FirebaseDatabase.getInstance().getReference().child("Companies").child(companyName);
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Companies").child(companyName);

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
                partnorefstring= dataSnapshot.getKey();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String partNo = snapshot.getValue(String.class);
                    partNoList.add(partNo);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    class PartNoAdapter extends RecyclerView.Adapter<PartNoAdapter.PartNoViewHolder> {

        private Context mctx;
        private List<String> partNoList;


        public PartNoAdapter (Context mctx, List<String> partNoList){
            this.mctx = mctx;
            this.partNoList = partNoList;
        }

        @NonNull
        @Override
        public PartNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(mctx).inflate(R.layout.single_name_layout, parent, false);

            return new PartNoViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull PartNoViewHolder partNoViewHolder, int i) {
            String partNo = partNoList.get(i);
            partNoViewHolder.name.setText(partNo);


        }

        @Override
        public int getItemCount() {
            return partNoList.size();
        }

        public class PartNoViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            public PartNoViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);


            }
        }
    }

}