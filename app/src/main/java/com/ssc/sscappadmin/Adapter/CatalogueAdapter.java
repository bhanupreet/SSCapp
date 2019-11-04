package com.ssc.sscappadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssc.sscappadmin.Model.Companies;
import com.ssc.sscappadmin.R;

import java.util.List;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueViewHolder> {

    private List<Companies> mList;
    private Context mCtx;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListner mItemLongClickListner;

    public CatalogueAdapter(List<Companies> mList, Context mCtx) {
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
    public CatalogueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_name_layout, parent, false);
        return new CatalogueViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatalogueViewHolder holder, final int position) {
        final Companies companies = mList.get(position);
        holder.name.setText(companies.getName().toLowerCase().trim());
        ViewCompat.setTransitionName(holder.layout, companies.getUid());

        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position, holder.layout);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
