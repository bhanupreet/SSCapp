package com.ssc.Derox.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.jraska.falcon.Falcon;
import com.ssc.Derox.Activities.AddProductActivity;
import com.ssc.Derox.Activities.ProductListActivity;
import com.ssc.Derox.Adapter.ProductPageAdapter;
import com.ssc.Derox.Model.PartNo;
import com.ssc.Derox.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ssc.Derox.Activities.ProductListActivity.getFab;


public class ProductPageFragment extends Fragment {
    private RecyclerView mRecycler;
    private ProductPageAdapter adapter;
    private List<PartNo> mList = new ArrayList<>();
    private String name = "";
    private boolean multiselect = false;
    private List<PartNo> mSelectionList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                Bitmap bitmap = Falcon.takeScreenshotBitmap(getActivity());

                try {

                    File cachePath = new File(getContext().getCacheDir(), "images");
                    cachePath.mkdirs(); // don't forget to make the directory
                    FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                File imagePath = new File(getContext().getCacheDir(), "images");
                File newFile = new File(imagePath, "image.png");
                Uri contentUri = FileProvider.getUriForFile(getContext(), "com.ssc.Derox", newFile);

                if (contentUri != null) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, name);
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_actions, menu);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_catalogue_list, container, false);

        Bundle bundle = getArguments();

        PartNo partNo = bundle.getParcelable("object");
        mList = bundle.getParcelableArrayList("objectList");

        name = partNo.getName();

        mRecycler = view.findViewById(R.id.cataloguelist_recycler);
        adapter = new ProductPageAdapter(mList, getContext());
        mRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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

        adapter.addItemClickListener((position, animatedview) -> {
            if (!mList.get(position).getImage().equals("default image")) {

                FullScreenImageFragment fragment = new FullScreenImageFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("objectList", (ArrayList<? extends Parcelable>) mList);
                bundle1.putParcelable("object", mList.get(position));
                fragment.setArguments(bundle1);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .addToBackStack("image")
                        .addSharedElement(animatedview, ViewCompat.getTransitionName(animatedview))

                        .replace(R.id.productlist_container, fragment)
                        .commit();
            }
        });

        if (ProductListActivity.getFab() != null) {
            ProductListActivity.getFab().setVisibility(View.VISIBLE);
            getFab().setImageResource(R.drawable.ic_edit);

            getFab().setOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra("object", mList.get(layoutManager.findFirstCompletelyVisibleItemPosition()));
                startActivity(intent);
            });
        }
        return view;
    }

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }

}
