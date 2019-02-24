package com.ssc.sscappadmin;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button catalogbtn,aboutusbtn, faqbtn;
    private androidx.appcompat.widget.Toolbar mainAppbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        catalogbtn = findViewById(R.id.main_cataloguebtn);
        aboutusbtn = findViewById(R.id.aboutusbtn);
        mainAppbar = findViewById(R.id.mainAppbar);

        setSupportActionBar(mainAppbar);
        getSupportActionBar().setTitle("Home");

        catalogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CatalogueIntent = new Intent(MainActivity.this,CatalogueActivity.class);
                startActivity(CatalogueIntent);
            }
        });

        aboutusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent CatalogueIntent = new Intent(MainActivity.this,ContactUsActivity.class);
                startActivity(CatalogueIntent);


            }
        });




    }
}
