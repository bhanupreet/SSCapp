package com.ssc.sscapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

public class AboutUsActivity extends AppCompatActivity {
    private Button mUploadimagebtn;
    private Bitmap bitmap2;
    private ImageView testimage,tetwatermark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mUploadimagebtn = findViewById(R.id.testbtn);
        testimage = findViewById(R.id.testimage);
        tetwatermark  = findViewById(R.id.testwatermark);

        testimage.setImageResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
        tetwatermark.setImageResource(R.drawable.ic_settings_black_24dp);
        tetwatermark.setAlpha(0.4f);


    }


}
