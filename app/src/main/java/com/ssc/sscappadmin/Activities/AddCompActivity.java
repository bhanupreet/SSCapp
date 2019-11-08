package com.ssc.sscappadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssc.sscappadmin.Model.Companies;
import com.ssc.sscappadmin.R;

import java.util.HashMap;

public class AddCompActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout mcompampanyname;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private ProgressDialog mprogressdialog;
    private Toolbar mToolbar;
    DatabaseReference myRef;
    Companies companyObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comp);

        mToolbar = findViewById(R.id.addcomp_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Company");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we upload the data");
        mprogressdialog.setMessage("Uploading data...");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAddBtn = findViewById(R.id.addCompBtn);
        FirebaseApp.initializeApp(this);
        mcompampanyname = findViewById(R.id.name_input);

        companyObject = getIntent().getParcelableExtra("object");
        if (companyObject != null) {
            mcompampanyname.getEditText().setText(companyObject.getName());
            mAddBtn.setText("Update");
            getSupportActionBar().setTitle(companyObject.getName());

        }

        mAddBtn.setOnClickListener(this);
// new comment
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addCompBtn) {
            mprogressdialog.show();
            String companynamestring = mcompampanyname.getEditText().getText().toString();
            if (!TextUtils.isEmpty(companynamestring)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                if (companyObject != null) {
                    myRef = database.getReference().child("Company").child(companyObject.getUid());

                    String oldname = companyObject.getName();
                    Query query = FirebaseDatabase.getInstance()
                            .getReference().child("PartNoList").orderByChild("companyname").equalTo(oldname);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    HashMap<String, Object> map1 = new HashMap<>();
                                    map1.put("companyname", companynamestring);
                                    snapshot.getRef().updateChildren(map1);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else
                    myRef = database.getReference().child("Company").push();

                HashMap<String, Object> map = new HashMap<>();
                map.put("name", companynamestring);

                myRef.updateChildren(map).addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCompActivity.this, "Company added successfully", Toast.LENGTH_SHORT).show();
                    Intent mainintent = new Intent(AddCompActivity.this, ProductListActivity.class);
                    startActivity(mainintent);
                    finishAffinity();
                });
            } else
                Toast.makeText(AddCompActivity.this, "Field cannot be left blank", Toast.LENGTH_SHORT).show();
        }
    }
}

