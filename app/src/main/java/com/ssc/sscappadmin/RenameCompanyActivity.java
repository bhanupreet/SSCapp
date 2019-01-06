package com.ssc.sscappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RenameCompanyActivity extends AppCompatActivity {

    private TextInputLayout mNewCompanyname;
    private Button mRenamebtn;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_company);
        final String oldname = getIntent().getStringExtra("companyname");

        mToolbar = findViewById(R.id.renameCompanyappbar);
        mRenamebtn = findViewById(R.id.renameCompanybtn);
        mNewCompanyname = findViewById(R.id.renameCompanyinput);

        mNewCompanyname.getEditText().setText(oldname);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Rename Company");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                startActivity(CatalogueIntent);
                finish();
            }
        });

        mRenamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uneditname = mNewCompanyname.getEditText().getText().toString();
                final String newname = toTitleCase(uneditname);
                //newname = toTitleCase(newname);
                if (newname.equals("")) {
                    Toast.makeText(RenameCompanyActivity.this, "Please input a value", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Companies").child(oldname).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Companies").child(newname).child("name").setValue(newname);
                    Query query = FirebaseDatabase.getInstance()
                            .getReference().child("PartNo").orderByChild("companyname").equalTo(oldname);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                              for(  DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                  snapshot.getRef().child("companyname").setValue(newname);
                              }
                                Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                                startActivity(CatalogueIntent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                startActivity(CatalogueIntent);
            }


        });

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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
