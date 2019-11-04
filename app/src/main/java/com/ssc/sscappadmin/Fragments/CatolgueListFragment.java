package com.ssc.sscappadmin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import java.util.List;

public class CatolgueListFragment extends Fragment {


    private RecyclerView mRecycler;
    private CatalogueAdapter adapter;
    private List<Companies> mList = new ArrayList<>();
    private Context mCtx;
    Query query;
    private CardView layout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
//        query.addListenerForSingleValueEvent(valueEventListener);
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

        adapter = new CatalogueAdapter(mList, mCtx);
        mRecycler.setLayoutManager(new LinearLayoutManager(mCtx));
        mRecycler.setAdapter(adapter);
        adapter.addItemClickListener((position, animatedview) -> {

            String companyName = mList.get(position).getName();
            Toast.makeText(mCtx, companyName, Toast.LENGTH_SHORT).show();

            ProductListFragment fragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("company", companyName);
            fragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack("settings")
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.productlist_container, fragment)
                    .commit();

        });

        ProductListFragment.setToolBarTitle("Products", view);

        return view;
    }

    private void FindIds(View view) {
        mRecycler = view.findViewById(R.id.cataloguelist_recycler);

    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mList.add(snapshot.getValue(Companies.class));
                    adapter.notifyItemInserted(mList.size() - 1);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
