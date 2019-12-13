package com.ssc.Derox.Activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssc.Derox.Fragments.ProductListFragment;
import com.ssc.Derox.Model.PartNo;
import com.ssc.Derox.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<PartNo> mList = new ArrayList<>();
    Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
//            finishAffinity();
            return true;

        } else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = findViewById(R.id.search_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("PartNoList");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PartNo partNo = snapshot.getValue(PartNo.class);
                        partNo.setUid(snapshot.getKey());
                        mList.add(partNo);
                    }
                }
                ProductListFragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("objectlist", (ArrayList<? extends Parcelable>) mList);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.productlist_container, fragment)
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
