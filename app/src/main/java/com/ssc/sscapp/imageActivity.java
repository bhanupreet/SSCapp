package com.ssc.sscapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class imageActivity extends AppCompatActivity {

    private PhotoView fullscreenphoto;
    private ImageView mWatermark;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private String partnorefstring, companynamestring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);

        String imageurl = getIntent().getStringExtra("image");
        partnorefstring = getIntent().getStringExtra("partnorefstring");
        companynamestring = getIntent().getStringExtra("Company name");

        fullscreenphoto = findViewById(R.id.fullscreenimage);
        mToolbar = findViewById(R.id.fullscreenimagappbar);
        mWatermark = findViewById(R.id.fullscreenwatermark);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(partnorefstring);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainintent = new Intent(imageActivity.this, PartNoDetails.class);
                mainintent.putExtra("Company name", companynamestring);
                mainintent.putExtra("partnorefstring", partnorefstring);
                startActivity(mainintent);
            }
        });

        Picasso.get().load(imageurl).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.ic_settings_black_24dp).into(fullscreenphoto);
        mWatermark.setImageResource(R.drawable.watemark);
        mWatermark.setAlpha(0.5f);

    }
}
