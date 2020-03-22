package com.ssc.Derox.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.ssc.Derox.Model.Companies;
import com.ssc.Derox.Model.PartNo;
import com.ssc.Derox.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExcelActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GROUP_FILE_REQUEST_CODE = 1;
    private static final int PARTS_FILE_REQUEST_CODE = 2;
    private TextView mGroupFile, mAllPartsFile;
    private Toolbar mToolbar;
    private List<Companies> companiesList = new ArrayList<>(), mOldCompaniesList = new ArrayList<>();
    private Button mSave;
    private List<PartNo> PartNoList = new ArrayList<>(), mOldPartNoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);

        companiesList.clear();

        mGroupFile = findViewById(R.id.excel_groupfile);
        mAllPartsFile = findViewById(R.id.excel_allparts_file);
        mToolbar = findViewById(R.id.excel_toolbar);
        mSave = findViewById(R.id.excel_save);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Excel Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGroupFile.setOnClickListener(this);
        mAllPartsFile.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.excel_groupfile:
                companiesList.clear();
                openFileExplorer(GROUP_FILE_REQUEST_CODE);
                break;
            case R.id.excel_allparts_file:
                PartNoList.clear();
                openFileExplorer(PARTS_FILE_REQUEST_CODE);
                break;
            case R.id.excel_save:
                updateGroupList();
                updatePartNoList();
                break;
        }
    }

    private void updatePartNoList() {
        if (!PartNoList.isEmpty()) {
            FirebaseDatabase.getInstance().getReference().child("PartNoList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mOldPartNoList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PartNo partNo = snapshot.getValue(PartNo.class);
                            partNo.setUid(snapshot.getKey());
                            mOldPartNoList.add(partNo);
                        }
                    } else
                        Toast.makeText(ExcelActivity.this, "no items", Toast.LENGTH_SHORT).show();
                    for (PartNo partNo : PartNoList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", partNo.getName());
                        map.put("ssc_code", partNo.getSsc_code());
                        map.put("companyname", partNo.getCompanyname());
                        map.put("image", "default image");
                        map.put("visibility", partNo.isVisibility());
                        if (mOldPartNoList.contains(partNo)) {
                            PartNo mOldPartNo = mOldPartNoList.get(mOldPartNoList.indexOf(partNo));
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PartNoList").child(mOldPartNo.getUid());
                            if (!TextUtils.isEmpty(mOldPartNo.getImage()))
                                map.put("image", mOldPartNo.getImage());
                            reference.updateChildren(map);
                        } else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PartNoList").push();
                            reference.updateChildren(map);
                        }
                        map.clear();
                    }

                    for(PartNo partno: mOldPartNoList){
                        if(!PartNoList.contains(partno)){
                            FirebaseDatabase.getInstance().getReference().child("PartNoList").child(partno.getUid()).removeValue();
                        }
                    }

                    PartNoList.clear();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Toast.makeText(this, "data updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void updateGroupList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Company");
        reference.removeValue();
        List<String> companylist = new CopyOnWriteArrayList<>();
        for(PartNo partNo: PartNoList){
            if(!companylist.contains(partNo.getCompanyname()))
                companylist.add(partNo.getCompanyname());
        }

        for(String company : companylist){
            Map<String, Object> map = new HashMap<>();
            map.put("name", company);
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Company").push();
            reference2.updateChildren(map);
        }


//        if (!companiesList.isEmpty()) {
//            FirebaseDatabase.getInstance().getReference().child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        mOldCompaniesList.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Companies companies = snapshot.getValue(Companies.class);
//                            companies.setUid(snapshot.getKey());
//                            mOldCompaniesList.add(companies);
//                        }
//                    } else
//                        Toast.makeText(ExcelActivity.this, "no items", Toast.LENGTH_SHORT).show();
//                    for (Companies companies : companiesList) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("name", companies.getName());
//                        if (mOldCompaniesList.contains(companies)) {
//                            Companies mOldCompany = mOldCompaniesList.get(companiesList.indexOf(companies));
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Company").child(mOldCompany.getUid());
//                            reference.updateChildren(map);
//                        } else {
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Company").push();
//                            reference.updateChildren(map);
//                        }
//                        map.clear();
//                    }
//                    companiesList.clear();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
    }


    private void openFileExplorer(int REQUEST_CODE) {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowFiles(true)
                .setShowVideos(false)
                .setSingleChoiceMode(true)
                .setSuffixes("xlsx")
                .build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GROUP_FILE_REQUEST_CODE:
                readGroupListFile(data);
                break;
            case PARTS_FILE_REQUEST_CODE:
                readPartsList(data);

        }
    }

    private void readPartsList(Intent data) {
        ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
        File file = new File(files.get(0).getPath());
        mAllPartsFile.setText(file.getPath());
        try {
            InputStream targetStream = new FileInputStream(file);
            onPartsReadClick(targetStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onPartsReadClick(InputStream mystream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(mystream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                String ssc_code = getCellAsString(row, 0, formulaEvaluator);
                String name = getCellAsString(row, 1, formulaEvaluator);
                String companyName = getCellAsString(row, 2, formulaEvaluator);
                PartNo partNo = new PartNo();
                partNo.setSsc_code(ssc_code);
                partNo.setName(name);
                partNo.setCompanyname(companyName);
                partNo.setVisibility(true);
                if (!PartNoList.contains(partNo) && !TextUtils.isEmpty(partNo.getName()))
                    PartNoList.add(partNo);
            }
        } catch (Exception e) {
            /* proper exception handling to be here */
        }
    }

    private void readGroupListFile(Intent data) {
        ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
        File file = new File(files.get(0).getPath());
        mGroupFile.setText(file.getPath());
        try {
            InputStream targetStream = new FileInputStream(file);
            onGroupReadClick(targetStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onGroupReadClick(InputStream myStream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(myStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                String value = getCellAsString(row, 0, formulaEvaluator);
                Companies companies = new Companies();
                companies.setName(value);
                if (!companiesList.contains(companies) && !TextUtils.isEmpty(companies.getName()))
                    companiesList.add(companies);
            }
        } catch (Exception e) {
            /* proper exception handling to be here */
        }
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            return cellValue.getStringValue();
        } catch (NullPointerException e) {
            /* proper error handling should be here */
        }
        return value;
    }
}
