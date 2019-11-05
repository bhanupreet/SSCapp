package com.ssc.sscappadmin.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssc.sscappadmin.Adapter.ProductListAdapter;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;


public class ProductListFragment extends Fragment {
    private RecyclerView mRecycler;
    private ProductListAdapter adapter;
    private ShimmerRecyclerView shimmerAdapter;
    private List<PartNo> mList = new ArrayList<>(), mAllList = new ArrayList<>(), mSearchList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.fragment_catalogue_list, container, false);
        FindIds(view);
        shimmerAdapter.showShimmerAdapter();
        shimmerAdapter.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        String company = bundle.getString("company");
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("PartNoList")
                .orderByChild("companyname")
                .equalTo(company);

        query.addListenerForSingleValueEvent(listener);


        setToolBarTitle(company, view);

        adapter = new ProductListAdapter(mList, getContext());
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(adapter);

        adapter.addItemClickListener((position, animatedview) -> {
            ProductPageFragment fragment = new ProductPageFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putParcelableArrayList("objectList", (ArrayList<? extends Parcelable>) mList);
            bundle1.putParcelable("object", mList.get(position));
            fragment.setArguments(bundle1);
            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack("profile")
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.productlist_container, fragment)
                    .commit();
        });
        return view;
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                mList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mList.add(snapshot.getValue(PartNo.class));
                }
            }
//            Collections.sort(mList, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
            adapter.notifyDataSetChanged();
            shimmerAdapter.hideShimmerAdapter();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void FindIds(View view) {
        mRecycler = view.findViewById(R.id.cataloguelist_recycler);
        shimmerAdapter = view.findViewById(R.id.shimmer_recycler_view);
    }

    public static void setToolBarTitle(String title, View view) {
        Activity activity = (Activity) view.getContext();
        ((AppCompatActivity) activity).getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the options menu from XML
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_actions, menu);

        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("PartNoList");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mAllList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mAllList.add(snapshot.getValue(PartNo.class));
                    }
                }
                adapter.notifyDataSetChanged();
                shimmerAdapter.hideShimmerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {

                if (newText.equals("")) {
                    mList.clear();
                    mList.addAll(mAllList);

                } else {

                    mSearchList.clear();
                    for (PartNo profile : mAllList) {
                        if (!isEmpty(profile.getName()) && !isEmpty(profile.getSsc_code()) && !isEmpty(profile.getModel()) && !isEmpty(profile.getReference())) {
                            if (profile.getName().toLowerCase().contains(newText.toLowerCase())
                                    || profile.getSsc_code().toLowerCase().contains(newText.toLowerCase())
                                    || profile.getReference().toLowerCase().contains(newText.toLowerCase())
                                    || profile.getModel().toLowerCase().contains(newText.toLowerCase()))
                                mSearchList.add(profile);
                        }
                    }
                    mList.clear();
                    mList.addAll(mSearchList);
                }
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
