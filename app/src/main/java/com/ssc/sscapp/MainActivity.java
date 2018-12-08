package com.ssc.sscapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private Button catalogbtn;
    private android.support.v7.widget.Toolbar mainAppbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        catalogbtn = findViewById(R.id.main_cataloguebtn);
        mainAppbar = findViewById(R.id.mainAppbar);
        setSupportActionBar(mainAppbar);
        getSupportActionBar().setTitle("SSC");

        catalogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CatalogueIntent = new Intent(getApplicationContext(),CatalogueActivity.class);
                startActivity(CatalogueIntent);
            }
        });


    }
}
