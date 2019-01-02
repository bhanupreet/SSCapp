package com.ssc.sscapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddPartNo extends AppCompatActivity {
    private TextInputLayout mpartno;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private DatabaseReference mCompanyRef;
    private Toolbar mToolBar;
    private ProgressDialog mprogressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part_no);

        mToolBar = findViewById(R.id.addpartno_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Part No");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String companyname = getIntent().getStringExtra("Company name");

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainintent = new Intent(AddPartNo.this, CompanyActivity.class);
                mainintent.putExtra("Company name", companyname);
//              mainintent.putExtra("partnorefstring",partnorefstring);
                startActivity(mainintent);
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAddBtn = findViewById(R.id.addpartnoBtn_activity);
        FirebaseApp.initializeApp(this);
        mpartno = findViewById(R.id.partnoInput);

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we upload the data");
        mprogressdialog.setMessage("Uploading data...");

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressdialog.show();
                String partno = mpartno.getEditText().getText().toString();
                partno= partno.substring(0,1).toUpperCase() + partno.substring(1).toLowerCase();

                if(!partno.equals("")) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("companyname", companyname);
                    result.put("name", partno);
                    result.put("ssc_code", "default ssc code");
                    result.put("reference", "default reference");
                    result.put("suitable_for", "default");
                    result.put("model", "default model/size");
                    result.put("image","default image");
                    result.put("price","N.A");
                    result.put("cost_price","N.A.");

                    // DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Comapnies").push();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("PartNo").child(partno);
                    myRef.setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddPartNo.this ,"part no added successfully",Toast.LENGTH_SHORT).show();
                                mprogressdialog.dismiss();
                                Intent mainintent = new Intent(AddPartNo.this, CompanyActivity.class);
                                mainintent.putExtra("Company name", companyname);
//              mainintent.putExtra("partnorefstring",partnorefstring);
                                startActivity(mainintent);
                            }
                            else
                            {
                                Toast.makeText(AddPartNo.this  ,"an error occured while uploadig data",Toast.LENGTH_SHORT).show();
                                mprogressdialog.dismiss();
                            }

                        }
                    });
                }

            }
        });

    }

}
