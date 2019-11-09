package com.ssc.sscappadmin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionInflater;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssc.sscappadmin.Activities.AddProductActivity;
import com.ssc.sscappadmin.Activities.ProductListActivity;
import com.ssc.sscappadmin.Adapter.ProductListAdapter;
import com.ssc.sscappadmin.Model.PartNo;
import com.ssc.sscappadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.ssc.sscappadmin.Activities.ProductListActivity.getFab;

public class ProductListFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecycler;
    private ProductListAdapter adapter;
    private ShimmerRecyclerView shimmerAdapter;
    private List<PartNo> mList = new ArrayList<>(), mAllList = new ArrayList<>(), mSearchList = new ArrayList<>(), mSelectionList = new ArrayList<>();
    private boolean multiselect = false;
    private TextView mNoParts;
    private HashMap<String, Object> keyobject = new HashMap<>();
    private ActionMode mActionmode;
    private boolean isSelectAll = false;
    private String companyname;


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
        mAllList = bundle.getParcelableArrayList("objectlist");
        if (mAllList==null) {
            companyname = bundle.getString("company");
            Query query = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("PartNoList")
                    .orderByChild("companyname")
                    .equalTo(companyname);

            query.addListenerForSingleValueEvent(listener);
            query.keepSynced(true);
            setToolBarTitle("Products", view);


        } else {
            mList.addAll(mAllList);
            shimmerAdapter.hideShimmerAdapter();
        }

        if (ProductListActivity.getFab() != null) {
            ProductListActivity.getFab().setVisibility(View.VISIBLE);
            getFab().setImageResource(R.drawable.ic_add_black_24dp);
            getFab().setOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra("companyname", companyname);
                startActivity(intent);
            });
        }


        setToolBarTitle(companyname, view);

        adapter = new ProductListAdapter(mList, getContext());
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(adapter);

        adapter.addItemClickListener((position, animatedview) -> {
            if (multiselect) {
                PartNo selected = mList.get(position);
                selected.setSelected(!selected.isSelected());
                if (mSelectionList.contains(selected)) {
                    mSelectionList.remove(selected);
                } else
                    mSelectionList.add(selected);

                if (mSelectionList.isEmpty()) {
                    resetActionMode();
                }
                adapter.notifyItemChanged(position);
            } else {
                ProductPageFragment fragment = new ProductPageFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("objectList", (ArrayList<? extends Parcelable>) mList);
                bundle1.putParcelable("object", mList.get(position));
                fragment.setArguments(bundle1);
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack("profile")
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.productlist_container, fragment)
                        .commit();
                Toast.makeText(getContext(), mList.get(position).getCompanyname(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void itemLongClick(int position) {
        multiselect = !multiselect;
        ProductListActivity productListActivity = (ProductListActivity) getActivity();
        startActionMode(productListActivity);
        mSelectionList.add(mList.get(position));
        mList.get(position).setSelected(!mList.get(position).isSelected());
        adapter.notifyItemChanged(position);
    }


    private void startActionMode(ProductListActivity productListActivity) {
        productListActivity.mToolBar.startActionMode(actionMode);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(dpFromPx(getContext(), 10), 0, 0, 0);

        ((AppCompatImageView) getActivity().findViewById(R.id.action_mode_close_button)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.uncheck));
        getActivity().findViewById(R.id.action_mode_close_button).setLayoutParams(lp);
        getActivity().findViewById(R.id.action_mode_close_button).setPadding(dpFromPx(getContext(), 10), 0, dpFromPx(getContext(), 5), 0);

//        mainActivity.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                actionmodeSelectAll();
//            }
//        });

        getActivity().findViewById(R.id.action_mode_close_button).setOnClickListener(view -> {
            actionmodeSelectAll();
        });
    }

    private void actionmodeSelectAll() {
        if (!isSelectAll) {
            for (PartNo profile : mList) {
                profile.setSelected(true);
                if (!mSelectionList.contains(profile))
                    mSelectionList.add(profile);

            }
            adapter.notifyDataSetChanged();
            isSelectAll = true;
            ((AppCompatImageView) getActivity().findViewById(R.id.action_mode_close_button)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.checked));
        } else {
            for (PartNo profile : mList) {
                profile.setSelected(false);
                mSelectionList.clear();
            }
            adapter.notifyDataSetChanged();
            isSelectAll = false;
            ((AppCompatImageView) getActivity().findViewById(R.id.action_mode_close_button)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.uncheck));
        }
        setActionModeTitle();
    }

    public static int dpFromPx(final Context context, final float px) {

        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                mNoParts.setVisibility(View.GONE);
                mList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PartNo partNo = snapshot.getValue(PartNo.class);
                    partNo.setUid(snapshot.getKey());
                    if (!mList.contains(snapshot.getValue(PartNo.class))) {
                        mList.add(partNo);
                        keyobject.put(snapshot.getKey(), snapshot.getValue(PartNo.class));
                    }
                }
            } else {
                mNoParts.setVisibility(View.VISIBLE);
                mNoParts.bringToFront();
            }
            mAllList=new ArrayList<>(mList);
//            Collections.sort(mList, (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
            adapter.notifyDataSetChanged();
            shimmerAdapter.hideShimmerAdapter();
            adapter.addItemLongClickListener(position -> {
//            makeText(getContext(),mList.get(position).getName(),Toast.LENGTH_SHORT).show();
//            multiselect = !multiselect;
                itemLongClick(position);


            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void FindIds(View view) {
        mRecycler = view.findViewById(R.id.cataloguelist_recycler);
        mNoParts = view.findViewById(R.id.noParts);
        mNoParts.setVisibility(View.GONE);
        shimmerAdapter = view.findViewById(R.id.shimmer_recycler_view);
    }

    public static void setToolBarTitle(String title, View view) {
        Activity activity = (Activity) view.getContext();
        ((AppCompatActivity) activity).getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
                return true;
            default:
                return false;
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
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {
                mList.clear();
                if (newText.equals("")) {
                    mList.clear();
                    mList.addAll(mAllList);

                } else {

                    mSearchList.clear();
                    for (PartNo profile : mAllList) {
                        if (!isEmpty(profile.getName())
                                && !isEmpty(profile.getSsc_code())
                                && !isEmpty(profile.getModel())
                                && !isEmpty(profile.getReference())
                                && !mSearchList.contains(profile)) {
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

    private ActionMode.Callback actionMode = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.selection_menu, menu);
            mActionmode = actionMode;
            setActionModeTitle();
            setHasOptionsMenu(true);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_cancel:
                    resetActionMode();
                    break;
                case R.id.action_select:
                    if (mSelectionList.isEmpty())
                        Toast.makeText(getContext(), "List is empty", Toast.LENGTH_SHORT).show();
                    else
                        sendToCompanyList(mSelectionList);
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionmode = null;
//            doBack();
        }
    };

    private void setActionModeTitle() {
        if (mActionmode != null) {
            if (mSelectionList.isEmpty())
                mActionmode.setTitle("Select");
            else
                mActionmode.setTitle("Selected: " + mSelectionList.size());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        resetActionMode();
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("PartNoList")
                .orderByChild("companyname")
                .equalTo(companyname);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                PartNo partNo = snapshot.getValue(PartNo.class);
                partNo.setUid(snapshot.getKey());
                if (!mList.contains(partNo) && partNo.isVisibility()) {
                    mList.add(partNo);
                    keyobject.put(snapshot.getKey(), snapshot.getValue(PartNo.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void resetActionMode() {
        multiselect = false;
        if (mActionmode != null)
            mActionmode.finish();
        for (PartNo profile : mList)
            profile.setSelected(false);

        mSelectionList.clear();
//        sort(filterlist);
        adapter.notifyDataSetChanged();
    }

    private void sendToCompanyList(List<PartNo> mSelectionList) {
        List<String> keyList = new ArrayList<>();
        keyList.clear();
        for (String key : keyobject.keySet()) {
            PartNo partNo = (PartNo) keyobject.get(key);
            if (mSelectionList.contains(partNo)) {
                keyList.add(key);
            }

        }

        TransferFragment fragment = new TransferFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putStringArrayList("objectList", (ArrayList<String>) keyList);
        fragment.setArguments(bundle1);
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("profile")
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.productlist_container, fragment)
                .commit();
        resetActionMode();


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.selection_menu, menu);

    }

    @Override
    public void onClick(View view) {

    }
}
