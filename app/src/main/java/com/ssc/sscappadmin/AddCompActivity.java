package com.ssc.sscappadmin;

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

public class AddCompActivity extends AppCompatActivity {

    private TextInputLayout mcompampanyname;
    private Button mAddBtn;
    private DatabaseReference mDatabase;
    private DatabaseReference mCompanyRef;
    private ProgressDialog mprogressdialog;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comp);

        mToolbar= findViewById(R.id.addcomp_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Company");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainintent = new Intent(AddCompActivity.this, CatalogueActivity.class);
                startActivity(mainintent);
            }
        });

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we upload the data");
        mprogressdialog.setMessage("Uploading data...");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAddBtn = findViewById(R.id.addCompBtn);
        FirebaseApp.initializeApp(this);
        mcompampanyname = findViewById(R.id.name_input);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressdialog.show();
                String companynamestring = mcompampanyname.getEditText().getText().toString();
                companynamestring = toTitleCase(companynamestring);
                //companynamestring = companynamestring.substring(0,1).toUpperCase() + companynamestring.substring(1).toLowerCase();
                if(!companynamestring.equals("")) {
                    // DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Comapnies").push();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Companies").child(companynamestring);
                    myRef.child("name").setValue(companynamestring).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mprogressdialog.dismiss();
                                Toast.makeText(AddCompActivity.this, "Company added successfully", Toast.LENGTH_SHORT).show();
                                Intent mainintent = new Intent(AddCompActivity.this, CatalogueActivity.class);
                                startActivity(mainintent);
                            }
                            else {
                                mprogressdialog.dismiss();
                                Toast.makeText(AddCompActivity.this, "An error occurred please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
// new comment
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
}

