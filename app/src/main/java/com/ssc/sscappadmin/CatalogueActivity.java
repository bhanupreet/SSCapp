package com.ssc.sscappadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
   // private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        CatalogToolbar = findViewById(R.id.catalogueappbar);
        setSupportActionBar(CatalogToolbar);
        getSupportActionBar().setTitle("Catalogue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mProgress = findViewById(R.id.catalogueprogress);
//        mProgress.setIndeterminate(true);
//        mProgress.setVisibility(View.VISIBLE);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we load the data");
        mprogressdialog.setMessage("Fetching data...");
        mprogressdialog.show();

        if(!connected){
              Toast.makeText(getApplicationContext(),"Please Connect to the internet, the data might not be available",Toast.LENGTH_LONG).show();
              mprogressdialog.dismiss();

        }

        CompanyNamesRecycler = findViewById(R.id.companynamesrecycler);
        CompanyNamesRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CompanyNamesRecycler.setLayoutManager(layoutManager);


        companiesList = new ArrayList<>();
        adapter = new CompaniesAdapter(this, companiesList);
        CompanyNamesRecycler.setAdapter(adapter);


        mCompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");
        mCompanyRef.keepSynced(true);

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Companies").orderByChild("name");
        query.keepSynced(true);

        addbtn = findViewById(R.id.addBtn);
//        addbtn.setClickable(false);
//        addbtn.setVisibility(View.INVISIBLE);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = new Intent(CatalogueActivity.this, AddCompActivity.class);
                startActivity(AddIntent);
            }
        });

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
                                .getReference().child("Companies").orderByChild("name");
                        query.keepSynced(true);
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
                        .getReference().child("Companies").orderByChild("name");
                query2.keepSynced(true);

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
                companiesList.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Companies companies = snapshot.getValue(Companies.class);
                        if (companies.name.toLowerCase().contains(searchtext.toLowerCase())) {
                            companiesList.add(companies);
                        }
                    }
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(CatalogueActivity.this, "no companies found", Toast.LENGTH_SHORT).show();
                }
                //mProgress.setVisibility(View.INVISIBLE);
                mprogressdialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // mProgress.setVisibility(View.INVISIBLE);
                mprogressdialog.dismiss();
                Toast.makeText(CatalogueActivity.this, "an error occured while fetching data", Toast.LENGTH_SHORT).show();

            }

        };
        return searchvalueEventListener;
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
                Toast.makeText(CatalogueActivity.this, "no companies found", Toast.LENGTH_SHORT).show();
            }
           // mProgress.setVisibility(View.INVISIBLE);
            mprogressdialog.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mprogressdialog.dismiss();
         //   mProgress.setVisibility(View.INVISIBLE);

            Toast.makeText(CatalogueActivity.this, "an error occured while fetching data", Toast.LENGTH_SHORT).show();

        }

    };
    public static String toTitleCase(String givenString) {

        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

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
                    CharSequence options[] = new CharSequence[]{"Delete","Rename"};
                    delete.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0)
                            {
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
                        if(which==1){
                                Intent RenameIntent = new Intent(CatalogueActivity.this,RenameCompanyActivity.class);
                                companyname = CompaniesList.get(i).name;
                                RenameIntent.putExtra("companyname",companyname);
                                startActivity(RenameIntent);
                                finish();
                            }
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