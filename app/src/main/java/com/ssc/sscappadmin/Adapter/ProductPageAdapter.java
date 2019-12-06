package com.ssc.sscappadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.List;

public class ProductPageAdapter extends RecyclerView.Adapter<ProductPageViewHolder> {

    private List<PartNo> mList;
    private Context mCtx;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListner mItemLongClickListner;

    public ProductPageAdapter(List<PartNo> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;

    }


    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addItemLongClickListener(ItemLongClickListner listner) {
        mItemLongClickListner = listner;
    }

    @NonNull
    @Override
    public ProductPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_item_page_layout, parent, false);
        return new ProductPageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageViewHolder holder, final int position) {
        PartNo partNo = mList.get(position);
        if (partNo != null) {
            holder.mName.setText(partNo.getName());
            holder.mSSC_Code.setText(partNo.getSsc_code());
        }

        holder.mImageview.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position, holder.mImageview);
            }
        });


        Picasso.get().load(partNo.getImage()).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.noimage).into(holder.mImageview);

        if (partNo.getImage().equals("default image"))
            holder.mImageview.bringToFront();

        holder.mSSC_CodeLayout.setVisibility(View.VISIBLE);
        holder.mGroup.setText(partNo.getCompanyname());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
