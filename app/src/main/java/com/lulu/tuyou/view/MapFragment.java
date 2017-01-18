package com.lulu.tuyou.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lulu.tuyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private static MapFragment instance;

    public static MapFragment newInstance() {
        if (instance == null) {
            synchronized (MapFragment.class) {
                if (instance == null) {
                    instance = new MapFragment();
                }
            }
        }
        return instance;
    }

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

}
