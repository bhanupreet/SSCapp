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

    private TextInputLayout mSamplePartNo, mSSS_code, mreference, mModel, msuitable_for, mPrice, mCost_price;
    private String SSCcode, reference, suitable_for, model, partnorefstring, price, cost_price;
    private String ssccoderefstring,referencestring,suitableforstring,pricestring,costpricestring,modelstring;
    private Button mAddBtn, mUploadimagebtn;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private String downloadUrl = "";
    private TextView resultdisplay;

    //retrieve part details also done
    //price change for all objects 
    //photo open in new activity
    //truck photo , truck parts in main acrtivity
    //contact us page
    //change name to ssc truck parts (satnam sales corporation ) in main activity
    //watermark

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partno_details);

        partnorefstring = getIntent().getStringExtra("partnorefstring");
        ssccoderefstring = getIntent().getStringExtra("ssccoderefstring");
        referencestring = getIntent().getStringExtra("referencestring");
        suitableforstring = getIntent().getStringExtra("suitableforstring");
        pricestring = getIntent().getStringExtra("pricestring");
        costpricestring = getIntent().getStringExtra("costpricestring");
        modelstring = getIntent().getStringExtra("modelstring");

        // mSamplePartNo = findViewById(R.id.samplepartno);
        mSSS_code = findViewById(R.id.ssc_code_input);
        mreference = findViewById(R.id.reference_input);
        msuitable_for = findViewById(R.id.suitablefor_input);
        mPrice = findViewById(R.id.price_input);
        mCost_price = findViewById(R.id.cost_price_input);
        mModel = findViewById(R.id.modelinput);

        mSSS_code.getEditText().setText(ssccoderefstring);
        mreference.getEditText().setText(referencestring);
        msuitable_for.getEditText().setText(suitableforstring);
        mPrice.getEditText().setText(pricestring);
        mCost_price.getEditText().setText(costpricestring);
        mModel.getEditText().setText(modelstring);

        mModel = findViewById(R.id.modelinput);
        mAddBtn = findViewById(R.id.addpartnodetails_addbtn);
        mUploadimagebtn = findViewById(R.id.uploadimagebtn);
        resultdisplay = findViewById(R.id.result);

        mToolbar = findViewById(R.id.addpartnodetails_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(partnorefstring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        //retreive details from firebase and set them here

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mprogressdialog.show();
                SSCcode = mSSS_code.getEditText().getText().toString();
                reference = mreference.getEditText().getText().toString();
                suitable_for = msuitable_for.getEditText().getText().toString();
                price = mPrice.getEditText().getText().toString();
                cost_price = mCost_price.getEditText().getText().toString();
                model = mModel.getEditText().getText().toString();

                HashMap<String, Object> result = new HashMap<>();

                if (!SSCcode.equals("")) {
                    result.put("ssc_code", SSCcode);
                }

                if (!reference.equals("")) {
                    result.put("reference", reference);
                }
                if (!suitable_for.equals("")) {
                    result.put("suitable_for", suitable_for);
                }

                if (!price.equals("")) {
                    result.put("price", "N.A");
                }

                if (!cost_price.equals("")) {
                    result.put("cost_price", "N.A.");
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
                myRef.updateChildren(result).

                        addOnCompleteListener(new OnCompleteListener<Void>() {
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

        mUploadimagebtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                CropImage.activity()
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
