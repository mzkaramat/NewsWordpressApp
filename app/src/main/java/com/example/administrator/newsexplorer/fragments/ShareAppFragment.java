package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.newsexplorer.R;

public class ShareAppFragment extends Fragment {
	
	public ShareAppFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_share_app, container, false);
         
        return rootView;
    }
}
