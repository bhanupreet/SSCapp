package com.ssc.sscappadmin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.ssc.sscappadmin.Adapter.ProductPageAdapter;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.List;


public class ProductPageFragment extends Fragment {
    private RecyclerView mRecycler;
    private ProductPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_catalogue_list, container, false);

        Bundle bundle = getArguments();

        PartNo partNo = bundle.getParcelable("object");
        List<PartNo> mList = new ArrayList<>(bundle.getParcelableArrayList("objectList"));


        mRecycler = view.findViewById(R.id.cataloguelist_recycler);
        adapter = new ProductPageAdapter(mList, getContext());
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
        mRecycler.scrollToPosition(mList.indexOf(partNo));
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecycler);
        adapter.notifyDataSetChanged();

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstVisibleItemPosition() == mList.indexOf(partNo)) {
                    ProductListFragment.setToolBarTitle(partNo.getName(), view);
                } else {
                    int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (pos != -1)
                        ProductListFragment.setToolBarTitle(mList.get(pos).getName(), view);
                }
            }
        });

        return view;
    }
}
