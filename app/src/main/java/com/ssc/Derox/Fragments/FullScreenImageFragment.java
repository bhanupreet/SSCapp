package com.ssc.Derox.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.chrisbanes.photoview.PhotoView;
import com.jraska.falcon.Falcon;
import com.squareup.picasso.Picasso;
import com.ssc.Derox.Activities.ProductListActivity;
import com.ssc.Derox.Model.PartNo;
import com.ssc.Derox.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullScreenImageFragment extends Fragment {

    private PhotoView fullscreenphoto;
    private ImageView mWatermark;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private String partnorefstring, companynamestring, imagecoderefstring;
    private TextView mImagecode;
    private String name = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_image2, container, false);

        fullscreenphoto = view.findViewById(R.id.fullscreenimage);
        mWatermark = view.findViewById(R.id.fullscreenwatermark);
        mImagecode = view.findViewById(R.id.imagecode);

        if (ProductListActivity.getFab() != null) {
            ProductListActivity.getFab().setVisibility(View.GONE);
        }
        Bundle bundle = getArguments();
        PartNo partNo = bundle.getParcelable("object");
        imagecoderefstring = partNo.getSsc_code();
        name = partNo.getName();

        if (imagecoderefstring.contains("ssc")) {
            imagecoderefstring.replace("ssc", "");
        }

        if (!imagecoderefstring.equals("default ssc code")) {
            mImagecode.setText(imagecoderefstring);
        }

        ProductListFragment.setToolBarTitle(partNo.getName(), view);
        Picasso.get().load(partNo.getImage()).placeholder(R.drawable.ic_settings_black_24dp).error(R.drawable.noimage).into(fullscreenphoto);
        // mWatermark.setImageResource(R.drawable.watermark);
        mWatermark.setAlpha(0.65f);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
                        Toast.makeText(getContext(), "Whatsapp not installed", Toast.LENGTH_SHORT);
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

}
