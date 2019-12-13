package com.ssc.Derox.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ssc.Derox.Model.PartNo;
import com.ssc.Derox.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddProductActivity extends AppCompatActivity implements OnClickListener {

    private TextInputLayout mSSS_code, mNameinput;
    private Button mAddBtn, mUploadimagebtn, mDeletebtn, mDeleteImagebtn;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private String downloadUrl = "";
    private ImageView resultdisplay;
    PartNo partNo;
    private String companynamestring, imagestring;
    private String key;
    private DatabaseReference ref;
    HashMap<String, Object> result = new HashMap<>();
    private ProgressDialog mprogressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        FindIds();


        companynamestring = getIntent().getStringExtra("companyname");
        partNo = getIntent().getParcelableExtra("object");
        mDeleteImagebtn.setVisibility(View.GONE);
        if (partNo != null) {
            setViews();
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setCanceledOnTouchOutside(false);
        mprogressdialog.setTitle("Please wait while we upload the data");
        mprogressdialog.setMessage("Uploading data...");

        mAddBtn.setOnClickListener(this);
        mUploadimagebtn.setOnClickListener(this);
        mDeleteImagebtn.setOnClickListener(this);
        mDeletebtn.setOnClickListener(this);

    }

    private void setViews() {
        if (!partNo.getSsc_code().equals("default ssc code"))
            mSSS_code.getEditText().setText(partNo.getSsc_code());

        if (partNo.getImage().equals("default image")) {
            mDeleteImagebtn.setVisibility(View.GONE);
        } else {
            mDeleteImagebtn.setVisibility(View.VISIBLE);
            Picasso.get().load(partNo.getImage()).into(resultdisplay);
        }

        mNameinput.getEditText().setText(partNo.getName());
        mAddBtn.setText("Update");
        mDeletebtn.setVisibility(View.VISIBLE);
        companynamestring = partNo.getCompanyname();
        key = partNo.getUid();

    }

    private void FindIds() {
        mSSS_code = findViewById(R.id.ssc_code_input);
        mDeleteImagebtn = findViewById(R.id.deleteimagebtn);
        mNameinput = findViewById(R.id.name_input);
        mAddBtn = findViewById(R.id.addpartnodetails_addbtn);
        mUploadimagebtn = findViewById(R.id.uploadimagebtn);
        resultdisplay = findViewById(R.id.result);
        mDeletebtn = findViewById(R.id.partnodetails_deletebtn);
        mToolbar = findViewById(R.id.addpartnodetails_toolbar);
    }

    private String textof(TextInputLayout layout) {
        return layout.getEditText().getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addpartnodetails_addbtn:
                if (TextUtils.isEmpty(textof(mNameinput))) {
                    Toast.makeText(this, "name cannot be left blank", Toast.LENGTH_SHORT).show();
                } else
                    AddButton();
                break;

            case R.id.uploadimagebtn:
                uploadImage();
                break;
            case R.id.deleteimagebtn:
                AlertDialog.Builder alert = new AlertDialog.Builder(AddProductActivity.this);

                alert.setTitle("Delete Image");
                alert.setMessage("Are you sure you want to delete the image?");
                alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    imagestring = "default image";
                    key = partNo.getUid();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference desertRef = storageRef.child("itemimages/" + key + ".jpg");
                    desertRef.delete();
                    resultdisplay.setBackgroundResource(R.drawable.noimage);
                });
                alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // close dialog
                    dialog.dismiss();
                });
                alert.show();
                break;
            case R.id.partnodetails_deletebtn:
                Deletebtn();
                break;


        }
    }

    private void Deletebtn() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddProductActivity.this);

        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            // continue with delete
            FirebaseDatabase.getInstance().getReference().child("PartNoList").child(key).removeValue();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference desertRef = storageRef.child("itemimages/" + key + ".jpg");
            desertRef.delete();
            Intent profileIntent = new Intent(AddProductActivity.this, ProductListActivity.class);
            startActivity(profileIntent);
            finishAffinity();
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            // close dialog
            dialog.cancel();
        });
        alert.show();
    }

    private void uploadImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AddProductActivity.this);
    }

    private void AddButton() {
        mprogressdialog.show();

        result.put("companyname", companynamestring);
        result.put("name", textof(mNameinput));

        if (!TextUtils.isEmpty(textof(mSSS_code)))
            result.put("ssc_code", textof(mSSS_code));
        else
            result.put("ssc_code", "default ssc code");

        if (!TextUtils.isEmpty(downloadUrl)) {
            result.put("image", downloadUrl);
        } else {
            result.put("image", "default image");
        }
        result.put("visibility", true);

        if (TextUtils.isEmpty(key)) {
            ref = FirebaseDatabase.getInstance().getReference().child("PartNoList").push();
        } else
            ref = FirebaseDatabase.getInstance().getReference().child("PartNoList").child(key);


        updateDatabase();
    }

    private void updateDatabase() {
        ref.updateChildren(result).addOnSuccessListener(aVoid -> {
            mprogressdialog.dismiss();
            Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
            Intent mainintent = new Intent(AddProductActivity.this, ProductListActivity.class);
            startActivity(mainintent);
            finishAffinity();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialaog = new ProgressDialog(AddProductActivity.this);
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

                if (TextUtils.isEmpty(key)) {
                    DatabaseReference ref = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("PartNoList")
                            .push();
                    key = ref.getKey();
                }
                mImageStorage = FirebaseStorage.getInstance().getReference();
                StorageReference filepath = mImageStorage.child("itemimages").child(key + ".jpg");

                UploadTask uploadTask = filepath.putBytes(thumb_byte);
                uploadTask.addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        mImageStorage.child("itemimages").child(key + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = uri.toString();
                            if (TextUtils.isEmpty(downloadUrl)) {
                                imagestring = downloadUrl;
                            }
                            mProgressDialaog.dismiss();
                            Picasso.get().load(downloadUrl).error(R.drawable.noimage).into(resultdisplay);
                            //Toast.makeText(addPartnoDetailsActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                            if (partNo.getImage().equals("default image")) {
                                mDeleteImagebtn.setVisibility(View.GONE);
                            } else {
                                mDeleteImagebtn.setVisibility(View.VISIBLE);
                                Picasso.get().load(partNo.getImage()).into(resultdisplay);
                            }

                        });

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
