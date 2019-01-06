package com.ssc.sscappadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {
    private TextView mPhone, mEmail,mphone2,mlandline;
    private androidx.appcompat.widget.Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mPhone = findViewById(R.id.phoneno);
        mphone2 = findViewById(R.id.phoneno2);
        mlandline = findViewById(R.id.landline);

        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9582426427"));
                startActivity(intent);
            }
        });

        mphone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 7982119819"));
                startActivity(intent);
            }
        });

        mlandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 28114314"));
                startActivity(intent);
            }
        });



        mToolbar = findViewById(R.id.contactusappbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Contact us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
