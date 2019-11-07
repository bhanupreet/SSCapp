package com.ssc.sscappadmin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity implements OnClickListener {

    private TextInputLayout mSSS_code, mreference, mModel, mNameinput;
    private Button mAddBtn, mUploadimagebtn, mDeletebtn, mDeleteImagebtn;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private String downloadUrl = "";
    private TextView resultdisplay;
    PartNo partNo;
    private String companynamestring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        FindIds();


        companynamestring = getIntent().getStringExtra("companyname");
        partNo = getIntent().getParcelableExtra("object");
        if (partNo != null)
            setViews();
        mAddBtn.setOnClickListener(this);

    }

    private void setViews() {
        mSSS_code.getEditText().setText(partNo.getSsc_code());
        mreference.getEditText().setText(partNo.getReference());
        mModel.getEditText().setText(partNo.getModel());
        mNameinput.getEditText().setText(partNo.getName());
    }

    private void FindIds() {
        mSSS_code = findViewById(R.id.ssc_code_input);
        mreference = findViewById(R.id.reference_input);
        mModel = findViewById(R.id.modelinput);
        mDeleteImagebtn = findViewById(R.id.deleteimagebtn);
        mNameinput = findViewById(R.id.name_input);
        mModel = findViewById(R.id.modelinput);
        mAddBtn = findViewById(R.id.addpartnodetails_addbtn);
        mUploadimagebtn = findViewById(R.id.uploadimagebtn);
        resultdisplay = findViewById(R.id.result);
        mDeletebtn = findViewById(R.id.partnodetails_deletebtn);
    }

    private String textof(TextInputLayout layout) {
        return layout.getEditText().getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addpartnodetails_addbtn:
                AddButton();
                break;



        }
    }

    private void AddButton() {
        HashMap<String, Object> result = new HashMap<>();

        if (!TextUtils.isEmpty(textof(mSSS_code)))
            result.put("ssc_code", textof(mSSS_code));
        else
            result.put("ssc_code", "default");


        if (!TextUtils.isEmpty(textof(mreference)))
            result.put("reference", textof(mreference));
        else
            result.put("reference", "default");


        if (!TextUtils.isEmpty(textof(mModel)))
            result.put("model", textof(mModel));
        else
            result.put("model", "default");


        if (!TextUtils.isEmpty(textof(mNameinput)))
            result.put("name", textof(mNameinput));
        else
            result.put("name", "no name");

        result.put("companyname", companynamestring);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("PartNo").push();
        myRef.updateChildren(result).addOnSuccessListener(aVoid -> {
            Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
            Intent mainintent = new Intent(AddProductActivity.this, ProductListActivity.class);
            mainintent.putExtra("Company name", companynamestring);
            startActivity(mainintent);
        });
    }
}
