package com.ssc.sscapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AddPartNo extends AppCompatActivity {
    private TextInputLayout mpartno;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private DatabaseReference mCompanyRef;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part_no);

        mToolBar = findViewById(R.id.addpartno_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Part No");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String companyname = getIntent().getStringExtra("Company name");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAddBtn = findViewById(R.id.addpartnoBtn_activity);
        FirebaseApp.initializeApp(this);
        mpartno = findViewById(R.id.partnoInput);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partno = mpartno.getEditText().getText().toString();


                if(!partno.equals("")) {
                    // DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Comapnies").push();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("PartNo").child(partno).child("company name");
                    myRef.setValue(companyname);
                    DatabaseReference myRef2 = database.getReference().child("PartNo").child(partno).child("name");
                    myRef2.setValue(partno);
                }

            }
        });

    }

}
