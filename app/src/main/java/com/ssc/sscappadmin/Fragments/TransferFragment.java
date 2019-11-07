package com.ssc.sscappadmin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssc.sscappadmin.Adapter.CatalogueAdapter;
import com.ssc.sscappadmin.Model.Companies;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferFragment extends Fragment {
    private RecyclerView mRecycler;
    private CatalogueAdapter adapter;
    private List<Companies> mList = new ArrayList<>(), mAllList = new ArrayList<>(), mSearchList = new ArrayList<>();
    private Context mCtx;
    Query query;
    private List<String> mSelectedList;
    private TextView mNoParts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_catalogue_list, container, false);

        FindIds(view);
        mCtx = getContext();
        query = FirebaseDatabase.getInstance().getReference().child("Company").orderByChild("name");
        query.addListenerForSingleValueEvent(valueEventListener);

        Bundle bundle = getArguments();
        mSelectedList = new ArrayList<>();
        mSelectedList = bundle.getStringArrayList("objectList");
        Toast.makeText(getContext(),Integer.toString(mSelectedList.size()),Toast.LENGTH_LONG).show();
        adapter = new CatalogueAdapter(mList, mCtx);
        mRecycler.setLayoutManager(new LinearLayoutManager(mCtx));
        mRecycler.setAdapter(adapter);
        adapter.addItemClickListener((position, animatedview) -> {
            for (String key : mSelectedList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("companyname", mList.get(position).getName());
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("PartNoList")
                        .child(key)
                        .updateChildren(map).addOnSuccessListener(aVoid -> {
                    Toast.makeText(mCtx, mList.get(position).getName(), Toast.LENGTH_SHORT).show();
                });
                map.clear();
            }
            getFragmentManager().popBackStack();
            getFragmentManager().popBackStack();


        });

        ProductListFragment.setToolBarTitle("Products", view);

        return view;
    }

    private void FindIds(View view) {
        mRecycler = view.findViewById(R.id.cataloguelist_recycler);
        mNoParts = view.findViewById(R.id.noParts);
        mNoParts.setVisibility(View.GONE);

    }

    private ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mList.clear();
            mAllList.clear();
            if (dataSnapshot.exists()) {
                mNoParts.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mList.add(snapshot.getValue(Companies.class));
                    adapter.notifyItemInserted(mList.size() - 1);
                }
                mAllList.addAll(mList);
                adapter.notifyDataSetChanged();
            } else {
                mNoParts.setVisibility(View.VISIBLE);
                mNoParts.bringToFront();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the options menu from XML
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_actions, menu);

        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {

                if (newText.equals("")) {
                    mList.clear();
                    mList.addAll(mAllList);
                } else {
                    mSearchList.clear();
                    for (Companies profile : mAllList) {
                        if (!TextUtils.isEmpty(profile.getName())
                                && profile.getName().toLowerCase().contains(newText.toLowerCase())
                                && !mSearchList.contains(profile))
                            mSearchList.add(profile);
                    }
                    mList.clear();
                    mList.addAll(mSearchList);
                }
                if (mList.isEmpty()) {
                    mNoParts.setVisibility(View.VISIBLE);
                    mNoParts.bringToFront();
                } else
                    mNoParts.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                return true;
            }

            public boolean onQueryTextSubmit(final String newText) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }
}
