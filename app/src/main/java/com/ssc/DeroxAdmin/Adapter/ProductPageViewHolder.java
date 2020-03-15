package com.ssc.DeroxAdmin.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.DeroxAdmin.R;

public class ProductPageViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageview;
    TextView mName, mSSC_Code, mGroup, mReference, mVisibility;
    CardView mGroupNameLayout, mSSC_CodeLayout;

    ProductPageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageview = itemView.findViewById(R.id.item_page_image);
        mGroup = itemView.findViewById(R.id.item_page_group);
        mName = itemView.findViewById(R.id.item_page_name);
        mVisibility = itemView.findViewById(R.id.item_page_visibility_value);
        mSSC_Code = itemView.findViewById(R.id.item_page_ssc_code);
        mGroupNameLayout = itemView.findViewById(R.id.item_page_group_name_layout);
        mSSC_CodeLayout = itemView.findViewById(R.id.item_page_ssc_Code_layout);

    }
}
