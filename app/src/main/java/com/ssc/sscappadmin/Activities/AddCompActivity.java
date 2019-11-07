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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssc.sscappadmin.R;

import java.util.HashMap;

public class AddCompActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout mcompampanyname;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private ProgressDialog mprogressdialog;
    private Toolbar mToolbar;


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

        mAddBtn.setOnClickListener(this);
// new comment
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addCompBtn) {
            mprogressdialog.show();
            String companynamestring = mcompampanyname.getEditText().getText().toString();
            if (!TextUtils.isEmpty(companynamestring)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Company").push();
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", companynamestring);
                myRef.updateChildren(map).addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCompActivity.this, "Company added successfully", Toast.LENGTH_SHORT).show();
                    Intent mainintent = new Intent(AddCompActivity.this, ProductListActivity.class);
                    startActivity(mainintent);
                });
            } else
                Toast.makeText(AddCompActivity.this, "Field cannot be left blank", Toast.LENGTH_SHORT).show();
        }
    }
}

