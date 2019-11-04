package com.ssc.sscappadmin.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Collections;
import java.util.List;


public class ProductListFragment extends Fragment {
    private RecyclerView mRecycler;
    private ProductListAdapter adapter;
    private ShimmerRecyclerView shimmerAdapter;
    private List<PartNo> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

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

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mList.add(snapshot.getValue(PartNo.class));
                    }
                }
                Collections.sort(mList, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
                adapter.notifyDataSetChanged();
                shimmerAdapter.hideShimmerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

}
