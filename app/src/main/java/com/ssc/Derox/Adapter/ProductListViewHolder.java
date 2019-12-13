package com.ssc.Derox.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.Derox.R;

class ProductListViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageview, mSelecteTick;
    TextView mName, mSSC_Code;

    ProductListViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageview = itemView.findViewById(R.id.productimage);
        mName = itemView.findViewById(R.id.productname);
        mSelecteTick = itemView.findViewById(R.id.selectedTick);
        mSSC_Code = itemView.findViewById(R.id.ssc_code_new);
    }
}
