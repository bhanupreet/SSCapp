package com.ssc.sscapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCompActivity extends AppCompatActivity {

    private TextInputLayout mcompampanyname;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private DatabaseReference mCompanyRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comp);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAddBtn = findViewById(R.id.addCompBtn);
        FirebaseApp.initializeApp(this);
        mcompampanyname = findViewById(R.id.name_input);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companynamestring = mcompampanyname.getEditText().getText().toString();
                if(!companynamestring.equals("")) {
                    // DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Comapnies").push();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Companies").child(companynamestring);
                    myRef.child("name").setValue(companynamestring);
                }

            }
        });

    }
}

