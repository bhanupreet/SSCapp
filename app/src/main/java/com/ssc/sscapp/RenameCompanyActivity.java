package com.ssc.sscapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
                    FirebaseDatabase.getInstance().getReference().child("Companies").child(oldname).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("Companies").child(newname).child("name").setValue(newname).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference().child("PartNo").orderByChild("companyname").equalTo(oldname).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.exists()) {
                                HashMap<String, Object> result = new HashMap<>();
                                result.put("companyname", newname);
                                dataSnapshot.getRef().child("companyname").setValue(newname).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RenameCompanyActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                                            startActivity(CatalogueIntent);
                                            finish();
                                        } else {
                                            Toast.makeText(RenameCompanyActivity.this, "Error While Renaming", Toast.LENGTH_SHORT).show();
                                            Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                                            startActivity(CatalogueIntent);
                                            finish();
                                        }
                                    }
                                });
//
                            } else {
                                Intent CatalogueIntent = new Intent(RenameCompanyActivity.this, CatalogueActivity.class);
                                startActivity(CatalogueIntent);
                                finish();
                            }

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

                }

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
