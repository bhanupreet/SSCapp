package com.ssc.sscapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private Button catalogbtn,aboutusbtn, faqbtn;
    private androidx.appcompat.widget.Toolbar mainAppbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        catalogbtn = findViewById(R.id.main_cataloguebtn);
        aboutusbtn = findViewById(R.id.aboutusbtn);
        faqbtn = findViewById(R.id.faqbtn);
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

        aboutusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CatalogueIntent = new Intent(getApplicationContext(),AboutUsActivity.class);
                startActivity(CatalogueIntent);
            }
        });




    }
}
