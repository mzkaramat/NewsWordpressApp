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
import com.example.administrator.newsexplorer.sections.EntertainmentNewsSec;
import com.example.administrator.newsexplorer.sections.MembersList;
import com.example.administrator.newsexplorer.sections.NewsSection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}

    ImageView AdvImage;
    ImageLoader imageLoader;

    ImageButton News, EnterNews, MembersSection;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);

        MembersSection = (ImageButton) rootView.findViewById(R.id.members_sec);
        News= (ImageButton) rootView.findViewById(R.id.news_sec);
        EnterNews = (ImageButton) rootView.findViewById(R.id.ent_news);


        MembersSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MembersList.class);
                startActivity(i);
            }
        });

        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewsSection.class);
                startActivity(i);
            }
        });

        EnterNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EntertainmentNewsSec.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}
