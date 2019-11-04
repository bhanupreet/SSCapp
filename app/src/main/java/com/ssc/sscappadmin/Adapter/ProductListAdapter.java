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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {

    private List<PartNo> mList;
    private Context mCtx;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListner mItemLongClickListner;

    public ProductListAdapter(List<PartNo> mList, Context mCtx) {
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
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_product_layout, parent, false);
        return new ProductListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, final int position) {
        final PartNo partNo = mList.get(position);
        holder.mName.setText(partNo.getName());
        if (partNo.isSelected()) {
            holder.mSelecteTick.setVisibility(View.VISIBLE);
        } else
            holder.mSelecteTick.setVisibility(View.GONE);

        Picasso.get().load(partNo.getImage()).placeholder(R.drawable.ic_settings_black_24dp).into(holder.mImageview);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position, holder.mImageview);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mItemLongClickListner != null) {
                mItemLongClickListner.onItemLongClick(position);
            }
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}