package com.ssc.sscapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PartNoDetails extends AppCompatActivity {



    private FloatingActionButton maddbtn;
    private Toolbar mToolbar;
    private RecyclerView PartnoRecycler;
    private List<PartNo> partNoList;
    private PartNoAdapter adapter;
    private String partnorefsrtring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_no_details);



        PartnoRecycler = findViewById(R.id.partnodetailsReycler);
        maddbtn =   findViewById(R.id.add_partnoDetails_btn);
        mToolbar = findViewById(R.id.partnodetails_toolbar);
        setSupportActionBar(mToolbar);
        partnorefsrtring = getIntent().getStringExtra("partnorefstring");
        getSupportActionBar().setTitle(partnorefsrtring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        maddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addintent = new Intent(getApplicationContext(),addPartnoDetailsActivity.class);
                addintent.putExtra("partnorefstring",partnorefsrtring);
                startActivity(addintent);
            }
        });

        PartnoRecycler.setHasFixedSize(true);
        PartnoRecycler.setLayoutManager(new LinearLayoutManager(this));

        partNoList = new ArrayList<>();
        adapter = new PartNoAdapter(this, partNoList);
        PartnoRecycler.setAdapter(adapter);


        Query query = FirebaseDatabase.getInstance()
                .getReference().child("PartNo").orderByChild("name").equalTo(partnorefsrtring);


        query.addValueEventListener(valueEventListener);

    }
    public ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            partNoList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PartNo partNo= snapshot.getValue(PartNo.class);
                    partNoList.add(partNo);
                }
                adapter.notifyDataSetChanged();

            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

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

            View view = LayoutInflater.from(mctx).inflate(R.layout.single_partno_details, parent, false);
            return new PartNoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PartNoViewHolder partNoViewHolder, final int i) {
            PartNo partNo = partNoList.get(i);
            partNoViewHolder.mGPnumber.setText(partNo.gpNumber);
            partNoViewHolder.mApplication.setText(partNo.application);
            partNoViewHolder.mModel.setText(partNo.model);
            partNoViewHolder.moemnumber.setText(partNo.oemNumber);
            partNoViewHolder.mSamplePartNo.setText(partnorefsrtring);
            Picasso.get().load(partNo.image).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.ic_settings_black_24dp).into(partNoViewHolder.mItemImage);

        }

        @Override
        public int getItemCount() {
            return partNoList.size();
        }
    }

    private class PartNoViewHolder extends RecyclerView.ViewHolder {

        TextView  mGPnumber, moemnumber, mApplication, mModel,mSamplePartNo;
        ImageView mItemImage;
        public PartNoViewHolder(@NonNull View itemView) {
            super(itemView);
            mSamplePartNo = itemView.findViewById(R.id.samplepartno);
            mGPnumber = itemView.findViewById(R.id.gpnumber);
            moemnumber = itemView.findViewById(R.id.oemnumber);
            mApplication = itemView.findViewById(R.id.application);
            mModel = itemView.findViewById(R.id.Model);
            mItemImage =   itemView.findViewById(R.id.itemimage_partnoDetaild);

        }
    }
}
