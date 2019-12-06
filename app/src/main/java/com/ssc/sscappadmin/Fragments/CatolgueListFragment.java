package com.ssc.sscappadmin.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssc.sscappadmin.Activities.AddCompActivity;
import com.ssc.sscappadmin.Activities.ProductListActivity;
import com.ssc.sscappadmin.Adapter.CatalogueAdapter;
import com.ssc.sscappadmin.Model.Companies;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.List;

import static com.ssc.sscappadmin.Activities.ProductListActivity.getFab;


public class CatolgueListFragment extends Fragment {


    private RecyclerView mRecycler;
    private CatalogueAdapter adapter;
    private List<Companies> mList = new ArrayList<>(), mAllList = new ArrayList<>(), mSearchList = new ArrayList<>();
    private Context mCtx;
    Query query;
    private TextView mNoParts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryfunct();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_catalogue_list, container, false);

        FindIds(view);
        mCtx = getContext();
        queryfunct();

        ProductListActivity.getFab().setVisibility(View.VISIBLE);

        getFab().setImageResource(R.drawable.ic_add_black_24dp);
        getFab().setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddCompActivity.class);
            startActivity(intent);
        });

        adapter = new CatalogueAdapter(mList, mCtx);
        mRecycler.setLayoutManager(new LinearLayoutManager(mCtx));
        mRecycler.setAdapter(adapter);
        adapter.addItemClickListener((position, animatedview) -> {

            String companyName = mList.get(position).getName();
//            Toast.makeText(mCtx, companyName, Toast.LENGTH_SHORT).show();

            ProductListFragment fragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("company", companyName);
            fragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack("settings")
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.productlist_container, fragment)
                    .commit();

        });

        adapter.addItemLongClickListener(position -> {
            AlertDialog.Builder delete = new AlertDialog.Builder(getContext());
            CharSequence options[] = new CharSequence[]{"Delete", "Rename"};
            delete.setItems(options, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        deletedialog(mList.get(position));
                        break;
                    case 1:
                        renameCompany(position);
                }
            });
//                    CharSequence options[] = new CharSequence[]{"Delete","Rename"};
            delete.show();

        });

        ProductListFragment.setToolBarTitle("Products", view);

        return view;
    }

    private void deletedialog(Companies companyname) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            // continue with delete
            FirebaseDatabase.getInstance().getReference().child("Company").child(companyname.getUid()).removeValue();

            Query query = FirebaseDatabase.getInstance().getReference().child("PartNoList").orderByChild("companyname").equalTo(companyname.getName());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            snapshot.getRef().removeValue();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference desertRef = storageRef.child("itemimages/" + key + ".jpg");
                            desertRef.delete();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Intent profileIntent = new Intent(getContext(), ProductListActivity.class);
            startActivity(profileIntent);
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            // close dialog
            dialog.cancel();
        });
        alert.show();
    }

    private void renameCompany(int position) {
        Intent AddCompIntent = new Intent(getContext(), AddCompActivity.class);
        AddCompIntent.putExtra("object", mList.get(position));
        startActivity(AddCompIntent);
    }

    private void queryfunct() {
        query = FirebaseDatabase.getInstance().getReference().child("Company").orderByChild("name");
        query.addListenerForSingleValueEvent(valueEventListener);
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
//                    String name = snapshot.child("name").getValue(String.class);
                    Companies company = snapshot.getValue(Companies.class);
//                    company.setName(name);
                    company.setUid(snapshot.getKey());
                    mList.add(company);
                    adapter.notifyItemInserted(mList.size() - 1);
//                    if(TextUtils.isEmpty(company.getName())){
//                        FirebaseDatabase.getInstance().getReference().child("Company").child(company.getUid()).removeValue();
//                    }
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

                mList.clear();
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
