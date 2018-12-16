package com.ssc.sscapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class addPartnoDetailsActivity extends AppCompatActivity {

    private TextInputLayout mGPnumber, mOEMnumber, mApplication, mModel;
    private String gpnumber, oemnumner, application, model, partnorefstring;
    private Button mAddBtn, mUploadimagebtn;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private String downloadUrl="";
    private TextView resultdisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partno_details);

        partnorefstring = getIntent().getStringExtra("partnorefstring");

        mGPnumber = findViewById(R.id.gpnumberinput);
        mOEMnumber = findViewById(R.id.oemnumberinput);
        mApplication = findViewById(R.id.applicationinput);
        mModel = findViewById(R.id.modelinput);
        mAddBtn = findViewById(R.id.addpartnodetails_addbtn);
        mUploadimagebtn = findViewById(R.id.uploadimagebtn);
        resultdisplay = findViewById(R.id.result);

        mToolbar = findViewById(R.id.addpartnodetails_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(partnorefstring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mprogressdialog.show();
                gpnumber = mGPnumber.getEditText().getText().toString();
                oemnumner = mOEMnumber.getEditText().getText().toString();
                application = mApplication.getEditText().getText().toString();
                model = mModel.getEditText().getText().toString();
                HashMap<String, Object> result = new HashMap<>();
                if (!gpnumber.equals("")) {
                    result.put("gpNumber", gpnumber);
                }
                if (!oemnumner.equals("")) {
                    result.put("oemNumber", oemnumner);
                }
                if (!application.equals("")) {
                    result.put("application", application);
                }
                if (!model.equals("")) {
                    result.put("model", model);
                }
                if (!downloadUrl.equals("")) {
                    result.put("image", downloadUrl);
                }
//                    result.put("image",);

                // DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Comapnies").push();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("PartNo").child(partnorefstring);
                myRef.updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "details added successfully", Toast.LENGTH_SHORT).show();
                            // mprogressdialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "an error occured while uploadig data", Toast.LENGTH_SHORT).show();
                            // mprogressdialog.dismiss();
                        }

                    }
                });
            }

        });

        mUploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(addPartnoDetailsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialaog = new ProgressDialog(addPartnoDetailsActivity.this);
                mProgressDialaog.setMessage("Please wait while we upload and process the image");
                mProgressDialaog.setTitle("Uploading Image...");
                mProgressDialaog.setCanceledOnTouchOutside(false);
                mProgressDialaog.show();
                Uri resultUri = result.getUri();
                String display = resultUri.toString();


                StorageReference filepath = mImageStorage.child("itemimages").child(partnorefstring + ".jpg");

                UploadTask uploadTask = filepath.putFile(resultUri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            mImageStorage.child("itemimages").child(partnorefstring + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl = uri.toString();
                                    mProgressDialaog.dismiss();
                                    resultdisplay.setText(downloadUrl);
                                    //Toast.makeText(addPartnoDetailsActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
