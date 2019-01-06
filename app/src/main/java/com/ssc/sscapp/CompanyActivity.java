package com.ssc.sscapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
    private String partnorefstring,companyName;
    private ProgressDialog mprogressdialog;
    private Toolbar mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        companyName = getIntent().getStringExtra("Company name");

        //Toast.makeText(this,companyName,Toast.LENGTH_SHORT).show();
        mtoolbar = findViewById(R.id.companyToolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(companyName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainintent = new Intent(CompanyActivity.this, CatalogueActivity.class);
//                mainintent.putExtra("Company name", companyName);
//                mainintent.putExtra("partnorefstring",partnorefstring);
                startActivity(mainintent);
            }
        });

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("please wait while we load the data");
        mprogressdialog.setMessage("fetching data");


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
        addbtn.setVisibility(View.INVISIBLE);
        addbtn.setClickable(false);
//        addbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent AddIntent = new Intent(CompanyActivity.this, AddPartNo.class);
//                AddIntent.putExtra("Company name",companyName);
//                startActivity(AddIntent);
//            }
//        });
        mprogressdialog.show();

        query.addValueEventListener(valueEventListener);



        //mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.search_actions, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);// Do not iconify the widget; expand it by defaul

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    return false;
                } else {
                    newText = toTitleCase(newText);
                    //  search(newText);
                    // This is your adapter that will be filtered

                    Query query = FirebaseDatabase.getInstance()
                            .getReference().child("PartNo").orderByChild("name");
                    query.addValueEventListener(search(newText));
                    // Toast.makeText(getApplicationContext(), "textChanged :" + newText, Toast.LENGTH_LONG).show();


                    return true;
                }

            }

            public boolean onQueryTextSubmit(String query) {
                // **Here you can get the value "query" which is entered in the search box.**

                query = toTitleCase(query);
                //   search(query);
                Query query2 = FirebaseDatabase.getInstance()
                        .getReference().child("PartNo").orderByChild("name");

                query2.addValueEventListener(search(query));
                //  Toast.makeText(getApplicationContext(),"searchvalue :"+query,Toast.LENGTH_LONG).show();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    public ValueEventListener search(final String searchtext) {
        ValueEventListener searchvalueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                partNoList.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PartNo partNo = snapshot.getValue(PartNo.class);
                        if (partNo.name.toLowerCase().contains(searchtext.toLowerCase())) {
                            partNoList.add(partNo);
                        }
                    }
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(CompanyActivity.this, "no parts found", Toast.LENGTH_SHORT).show();
                }
                mprogressdialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mprogressdialog.dismiss();
                Toast.makeText(CompanyActivity.this, "an error occured while fetching data", Toast.LENGTH_SHORT).show();

            }

        };
        return searchvalueEventListener;
    }
    public static String toTitleCase(String givenString) {

        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
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
            else {
                Toast.makeText(CompanyActivity.this,"no parts found",Toast.LENGTH_SHORT).show();
                mprogressdialog.dismiss();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mprogressdialog.dismiss();
            Toast.makeText(CompanyActivity.this  ,"an error occured while fetching data",Toast.LENGTH_SHORT).show();

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
                    profileIntent.putExtra("Company name",companyName);
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