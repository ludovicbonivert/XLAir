package com.ludovicsvetlana.xlair;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TwitterFragment extends Fragment {

    // What does the fragment look like
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(android.R.layout.fragment_twitterfeed, container, false);
        return view;
    }
}
