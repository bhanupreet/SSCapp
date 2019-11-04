package com.ssc.sscappadmin.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.sscappadmin.R;

class ProductListViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageview, mSelecteTick;
    TextView mName;

    ProductListViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageview = itemView.findViewById(R.id.productimage);
        mName = itemView.findViewById(R.id.productname);
        mSelecteTick = itemView.findViewById(R.id.selectedTick);
    }
}
