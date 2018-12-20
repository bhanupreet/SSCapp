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
    private String partnorefsrtring,ssccoderefstring,referencestring,suitableforstring,pricestring,costpricestring,modelstring;

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
                addintent.putExtra("ssccoderefstring",ssccoderefstring);
                addintent.putExtra("referencestring",referencestring);
                addintent.putExtra("suitableforstring",suitableforstring);
                addintent.putExtra("pricestring",pricestring);
                addintent.putExtra("costpricestring",costpricestring);
                addintent.putExtra("modelstring",modelstring);
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

            View view = LayoutInflater.from(mctx).inflate(R.layout.single_partnodetails, parent, false);
            return new PartNoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PartNoViewHolder partNoViewHolder, final int i) {
            PartNo partNo = partNoList.get(i);
            partNoViewHolder.mSamplePartNo.setText(partNo.name);
            partNoViewHolder.mSSc_code.setText(partNo.ssc_code);
            partNoViewHolder.mreference.setText(partNo.reference);
            partNoViewHolder.msuitable_for.setText(partNo.suitable_for);
            partNoViewHolder.mPrice.setText(partNo.price);
            partNoViewHolder.mCost_price.setText(partNo.cost_price);
            partNoViewHolder.mModel.setText(partNo.model);

            ssccoderefstring = partNo.ssc_code;
            referencestring = partNo.reference;
            suitableforstring = partNo.suitable_for;
            pricestring = partNo.price;
            costpricestring = partNo.cost_price;
            modelstring = partNo.model;

            Picasso.get().load(partNo.image).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.ic_settings_black_24dp).into(partNoViewHolder.mItemImage);

        }

        @Override
        public int getItemCount() {
            return partNoList.size();
        }
    }

    private class PartNoViewHolder extends RecyclerView.ViewHolder {

        TextView  mSSc_code, mreference, msuitable_for, mModel,mSamplePartNo,mPrice,mCost_price;
        ImageView mItemImage;
        public PartNoViewHolder(@NonNull View itemView) {
            super(itemView);
            mSamplePartNo = itemView.findViewById(R.id.samplepartno);
            mSSc_code = itemView.findViewById(R.id.ssc_code);
            mreference = itemView.findViewById(R.id.reference);
            msuitable_for = itemView.findViewById(R.id.suitablefor);
            mPrice = itemView.findViewById(R.id.price);
            mCost_price = itemView.findViewById(R.id.cost_price);
            mModel = itemView.findViewById(R.id.model);
            mItemImage =   itemView.findViewById(R.id.itemimage_partnoDetaild);

        }
    }
}
