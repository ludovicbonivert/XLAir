package com.ludovicsvetlana.xlair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ludovicbonivert on 18/12/15.
 */
public class RadioFragment extends Fragment{

    // What does the fragment look like
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        return view;
    }

}
