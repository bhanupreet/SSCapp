package com.ssc.sscappadmin;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String partnorefsrtring, ssccoderefstring, referencestring, costpricestring, modelstring, imagestring, companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_no_details);

         // add ssc code on image watermark
        // contact us tax
        // yes/no for stock
        // delete photo functionality

        partnorefsrtring = getIntent().getStringExtra("partnorefstring");
        companyName = getIntent().getStringExtra("Company name");

        PartnoRecycler = findViewById(R.id.partnodetailsReycler);
        maddbtn = findViewById(R.id.add_partnoDetails_btn);
        mToolbar = findViewById(R.id.partnodetails_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(partnorefsrtring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainintent = new Intent(PartNoDetails.this, CompanyActivity.class);
                mainintent.putExtra("Company name", companyName);
                mainintent.putExtra("partnorefstring", partnorefsrtring);
                startActivity(mainintent);
                // perform whatever you want on back arrow click
            }
        });

//        maddbtn.setVisibility(View.INVISIBLE);
//        maddbtn.setClickable(false);

        maddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addintent = new Intent(PartNoDetails.this, addPartnoDetailsActivity.class);
                addintent.putExtra("partnorefstring", partnorefsrtring);
                addintent.putExtra("ssccoderefstring", ssccoderefstring);
                addintent.putExtra("referencestring", referencestring);
                addintent.putExtra("costpricestring", costpricestring);
                addintent.putExtra("modelstring", modelstring);
                addintent.putExtra("image", imagestring);
                addintent.putExtra("Company name", companyName);
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
        query.keepSynced(true);


        query.addValueEventListener(valueEventListener);

    }

    public ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            partNoList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PartNo partNo = snapshot.getValue(PartNo.class);
                    partNoList.add(partNo);
                }
                adapter.notifyDataSetChanged();

            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    private class PartNoAdapter extends RecyclerView.Adapter<PartNoViewHolder> {
        private Context mctx;
        private List<PartNo> partNoList;

        public PartNoAdapter(Context mctx, List<PartNo> partNoList) {

            this.partNoList = partNoList;
            this.mctx = mctx;
        }

        @NonNull
        @Override
        public PartNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(mctx).inflate(R.layout.single_partnodetails, parent, false);
            return new PartNoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PartNoViewHolder partNoViewHolder, final int i) {

            final PartNo partNo = partNoList.get(i);
            partNoViewHolder.mSamplePartNo.setText(partNo.name);
            partNoViewHolder.mSSc_code.setText(partNo.ssc_code);
            partNoViewHolder.mreference.setText(partNo.reference);
//            partNoViewHolder.mCost_price.setText(partNo.cost_price);
            partNoViewHolder.mModel.setText(partNo.model);

            Picasso.get().load(partNo.image).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.ic_settings_black_24dp).into(partNoViewHolder.mItemImage);
            partNoViewHolder.mWatermark.setImageResource(R.drawable.watemark);
            partNoViewHolder.mWatermark.setAlpha(0.7f);


            if (!partNo.image.equals("default image")) {
                partNoViewHolder.mItemImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent picIntent = new Intent(PartNoDetails.this, imageActivity.class);
                        picIntent.putExtra("image", partNo.image);
                        picIntent.putExtra("partnorefstring", partNo.name);
                        picIntent.putExtra("Company name", partNo.companyname);
                        picIntent.putExtra("imagecode",partNo.ssc_code);
                        startActivity(picIntent);
                    }
                });
                partNoViewHolder.mWatermark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent picIntent = new Intent(PartNoDetails.this, imageActivity.class);
                        picIntent.putExtra("image", partNo.image);
                        picIntent.putExtra("partnorefstring", partNo.name);
                        picIntent.putExtra("Company name", partNo.companyname);
                        picIntent.putExtra("imagecode",partNo.ssc_code);
                        startActivity(picIntent);

                    }
                });
            }

            ssccoderefstring = partNo.ssc_code;
            referencestring = partNo.reference;
            costpricestring = partNo.cost_price;
            modelstring = partNo.model;
            imagestring = partNo.image;
            companyName = partNo.companyname;

        }

        @Override
        public int getItemCount() {
            return partNoList.size();
        }
    }

    private class PartNoViewHolder extends RecyclerView.ViewHolder {

        TextView mSSc_code, mreference, mModel, mSamplePartNo;
//                TextView mCost_price;
        ImageView mItemImage, mWatermark;

        public PartNoViewHolder(@NonNull View itemView) {
            super(itemView);
            mWatermark = itemView.findViewById(R.id.watermark);
            mSamplePartNo = itemView.findViewById(R.id.samplepartno);
            mSSc_code = itemView.findViewById(R.id.ssc_code);
            mreference = itemView.findViewById(R.id.reference);
//            mCost_price = itemView.findViewById(R.id.cost_price);
            mModel = itemView.findViewById(R.id.model);
            mItemImage = itemView.findViewById(R.id.itemimage_partnoDetaild);

        }
    }
}
