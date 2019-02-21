package com.ssc.sscappadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class addPartnoDetailsActivity extends AppCompatActivity {

    private TextInputLayout mSamplePartNo, mSSS_code, mreference, mModel, mCost_price, mNameinput;
    private String SSCcode, reference, model, partnorefstring, cost_price;
    private String ssccoderefstring, referencestring, costpricestring, modelstring, nameinput, companynamestring, imagestring = " ";
    private Button mAddBtn, mUploadimagebtn, mDeletebtn, mDeleteImagebtn;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private String downloadUrl = "";
    private TextView resultdisplay;


    // Done
    //retrieve part details
    //part no deletion
    //watermark
    //photo open in new activity
    //image compression
    //company deletion
    //company rename
    //company search
    //front page    (truck photo , truck parts in main acrtivity)  (change name to ssc truck parts (satnam sales corporation ) in main activity")
    //contact us page
    //part no search

    //TODO
    //Totitle case function have feature for 2 spacesw




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partno_details);

        partnorefstring = getIntent().getStringExtra("partnorefstring");
        ssccoderefstring = getIntent().getStringExtra("ssccoderefstring");
        referencestring = getIntent().getStringExtra("referencestring");
        costpricestring = getIntent().getStringExtra("costpricestring");
        modelstring = getIntent().getStringExtra("modelstring");
        companynamestring = getIntent().getStringExtra("Company name");
        imagestring = getIntent().getStringExtra("image");

        // mSamplePartNo = findViewById(R.id.samplepartno);
        mSSS_code = findViewById(R.id.ssc_code_input);
        mreference = findViewById(R.id.reference_input);
        mCost_price = findViewById(R.id.cost_price_input);
        mModel = findViewById(R.id.modelinput);
        mDeleteImagebtn = findViewById(R.id.deleteimagebtn);
        mNameinput = findViewById(R.id.name_input);

        mSSS_code.getEditText().setText(ssccoderefstring);
        mreference.getEditText().setText(referencestring);
        mCost_price.getEditText().setText(costpricestring);
        mModel.getEditText().setText(modelstring);
        mNameinput.getEditText().setText(partnorefstring);

        mModel = findViewById(R.id.modelinput);
        mAddBtn = findViewById(R.id.addpartnodetails_addbtn);
        mUploadimagebtn = findViewById(R.id.uploadimagebtn);
        resultdisplay = findViewById(R.id.result);
        mDeletebtn = findViewById(R.id.partnodetails_deletebtn);

        resultdisplay.setText(imagestring);

        mToolbar = findViewById(R.id.addpartnodetails_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(partnorefstring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainintent = new Intent(addPartnoDetailsActivity.this, PartNoDetails.class);
                mainintent.putExtra("Company name", companynamestring);
                mainintent.putExtra("partnorefstring", partnorefstring);
                startActivity(mainintent);
            }
        });


        mImageStorage = FirebaseStorage.getInstance().getReference();

        mDeleteImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagestring = "default image";

            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSCcode = mSSS_code.getEditText().getText().toString();
                reference = mreference.getEditText().getText().toString();
                cost_price = mCost_price.getEditText().getText().toString();
                model = mModel.getEditText().getText().toString();
                nameinput = mNameinput.getEditText().getText().toString();
                HashMap<String, Object> result = new HashMap<>();

                if (!SSCcode.equals("")) {
                    result.put("ssc_code", SSCcode);
                }

                if (!reference.equals("")) {
                    result.put("reference", reference);
                }

                if (!cost_price.equals("")) {
                    result.put("cost_price", cost_price);
                }

                if (!model.equals("")) {
                    result.put("model", model);
                }

                if (imagestring.equals("")) {
                    result.put("image", imagestring);
                } else {
                    result.put("image", imagestring);
                }
                result.put("name", nameinput);
                result.put("companyname", companynamestring);


                if (!partnorefstring.equals(nameinput)) {
                    FirebaseDatabase.getInstance().getReference().child("PartNo").child(partnorefstring).removeValue();

                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("PartNo").child(nameinput);
                myRef.updateChildren(result).

                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(addPartnoDetailsActivity.this, "details added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(addPartnoDetailsActivity.this, "an error occured while uploadig data", Toast.LENGTH_SHORT).show();

                                }
                                // mProgressDialaog.dismiss();

                            }
                        });


                Intent mainintent = new Intent(addPartnoDetailsActivity.this, PartNoDetails.class);
                mainintent.putExtra("Company name", companynamestring);
                mainintent.putExtra("partnorefstring", nameinput);
                startActivity(mainintent);
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

        mDeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alert = new AlertDialog.Builder(addPartnoDetailsActivity.this);

                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        FirebaseDatabase.getInstance().getReference().child("PartNo").child(partnorefstring).removeValue();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference desertRef = storageRef.child("itemimages/" + partnorefstring + ".jpg");
                        desertRef.delete();
                        Intent profileIntent = new Intent(addPartnoDetailsActivity.this, CompanyActivity.class);
                        profileIntent.putExtra("Company name", companynamestring);
                        startActivity(profileIntent);


                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
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

                File thumb_filepath = new File(resultUri.getPath());


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(70)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("itemimages").child(partnorefstring + ".jpg");

                UploadTask uploadTask = filepath.putBytes(thumb_byte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            mImageStorage.child("itemimages").child(partnorefstring + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl = uri.toString();
                                    if (!downloadUrl.equals("")) {
                                        imagestring = downloadUrl;
                                    }
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
