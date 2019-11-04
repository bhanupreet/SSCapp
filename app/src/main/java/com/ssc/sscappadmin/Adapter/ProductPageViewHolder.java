package com.ssc.sscappadmin.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.sscappadmin.R;

public class ProductPageViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageview;
    TextView mName, mSSC_Code, mModel, mReference;
    CardView mModelLayout, mReferenceLayout, mSSC_CodeLayout;
    ConstraintLayout mVisibility;
    SwitchCompat mVisibilitySwitch;

    ProductPageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageview = itemView.findViewById(R.id.item_page_image);
        mModel = itemView.findViewById(R.id.item_page_model);
        mName = itemView.findViewById(R.id.item_page_name);
        mSSC_Code = itemView.findViewById(R.id.item_page_ssc_code);
        mReference = itemView.findViewById(R.id.item_page_reference);

        mModelLayout = itemView.findViewById(R.id.item_page_model_layout);
        mReferenceLayout = itemView.findViewById(R.id.item_page_reference_layout);
        mSSC_CodeLayout = itemView.findViewById(R.id.item_page_ssc_Code_layout);
        mVisibility = itemView.findViewById(R.id.item_page_visibility_layout);
        mVisibilitySwitch = itemView.findViewById(R.id.item_page_visibility_switch);

    }
}
