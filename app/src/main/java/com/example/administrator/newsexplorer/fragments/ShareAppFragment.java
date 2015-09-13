package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.sections.AdvertisementNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ShareAppFragment extends Fragment {
    ImageView AdvImage;
    ImageLoader imageLoader;
    ImageButton ShareBut;
	public ShareAppFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_share_app, container, false);
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);
        AdvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AdvertisementNews.class);
                startActivity(i);
            }
        });
        ShareBut = (ImageButton) rootView.findViewById(R.id.share_but);
        ShareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "I find Ghanchi Darpan useful, so sharing with you. Do install and use that";

                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Share us");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(txtIntent ,"Share"));
            }
        });
        return rootView;
    }
}
