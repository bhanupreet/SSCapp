package com.ssc.sscappadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.google.firebase.FirebaseApp;
import com.ssc.sscappadmin.Model.Companies;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button catalogbtn, aboutusbtn, searchbtn, transfer;
    private androidx.appcompat.widget.Toolbar mainAppbar;
    private List<Companies> companiesList = new ArrayList<>();
    private List<PartNo> partNoList = new ArrayList<>();
    long size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        catalogbtn = findViewById(R.id.main_cataloguebtn);
        aboutusbtn = findViewById(R.id.aboutusbtn);
        searchbtn = findViewById(R.id.Searchbtn);
        transfer = findViewById(R.id.transfer);
        mainAppbar = findViewById(R.id.mainAppbar);

        searchbtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        setSupportActionBar(mainAppbar);
        getSupportActionBar().setTitle("Home");

        transfer.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this,ExcelActivity.class);
            startActivity(intent);
        });

//        transfer.setOnClickListener(v -> {
////            Query query = FirebaseDatabase.getInstance().getReference().child("PartNo");
////            query.addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    if (dataSnapshot.exists()) {
////                        partNoList.clear();
////                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//////                            Integer i = snapshot.child("name").getValue(Integer.class);
////                            PartNo partNo = snapshot.getValue(PartNo.class);
//////                            if (i != null) {
//////                                partNo.setName(Integer.toString(partNo.name));
////////                            }
////                            if (!partNoList.contains(partNo))
////                                partNoList.add(partNo);
//////
////                        }
////                        Toast.makeText(getApplicationContext(), "data added to list" + partNoList.size(), Toast.LENGTH_SHORT).show();
////                        for (PartNo partNo : partNoList) {
//////                            partNo.cost_price = null;
////                            HashMap<String, Object> result = new HashMap<>();
////                            result.put("companyname", partNo.getCompanyname());
////                            result.put("name", partNo.getName());
////                            result.put("ssc_code", partNo.getSsc_code());
////                            result.put("reference", partNo.getReference());
////                            result.put("model", partNo.getModel());
////                            result.put("image", partNo.getImage());
////                            if (TextUtils.isEmpty(partNo.cost_price)) {
////                                result.put("visibility", true);
////                            } else {
////                                if (partNo.cost_price.toLowerCase().trim().contains("yes"))
////                                    result.put("visibility", true);
////                                else
////                                    result.put("visibility", false);
////                            }
////                            FirebaseDatabase.getInstance().getReference().child("PartNoList").push().updateChildren(result);
//////                            FirebaseDatabase.getInstance().getReference().child("PartNoList").push().setValue(partNo).addOnSuccessListener(new OnSuccessListener<Void>() {
//////                                @Override
//////                                public void onSuccess(Void aVoid) {
//////                                    Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
//////                                }
//////                            });
////                        }
////                        FirebaseDatabase.getInstance().getReference().child("PartNoList").addListenerForSingleValueEvent(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                Toast.makeText(MainActivity.this, Long.toString(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
////                            }
////
////                            @Override
////                            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                            }
////                        });
////
////
////                    }
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////                }
////            });
//
//
//            Query query2 = FirebaseDatabase.getInstance().getReference().child("Companies");
//            query2.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        companiesList.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Companies company = snapshot.getValue(Companies.class);
//                            if (!companiesList.contains(company))
//                                companiesList.add(company);
//                        }
//                        Toast.makeText(getApplicationContext(), "data added to list" + companiesList.size(), Toast.LENGTH_SHORT).show();
//                        for (Companies company : companiesList) {
//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Company").push();
//                            HashMap<String, Object> companymap = new HashMap<>();
//                            companymap.put("name", company.getName());
//                            ref.updateChildren(companymap).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "added new values", Toast.LENGTH_SHORT).show());
////                            FirebaseDatabase.getInstance().getReference().child("Companies").child(company.getName()).removeValue();
//                        }
//                        FirebaseDatabase.getInstance().getReference().child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Toast.makeText(MainActivity.this, Long.toString(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//
//        });


        catalogbtn.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(MainActivity.this, v, "transition");
            int revealX = (int) (v.getX() + v.getWidth() / 2);
            int revealY = (int) (v.getY() + v.getHeight() / 2);

            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            intent.putExtra(ProductListActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(ProductListActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        });

        aboutusbtn.setOnClickListener(v -> {

            Intent CatalogueIntent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(CatalogueIntent);


        });


    }
}
