package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.newsexplorer.R;

/**
 * Created by Sunny on 9/2/2015.
 */
public class TeamFragment extends Fragment {
    public TeamFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sponsers, container, false);

        return rootView;
    }
}
