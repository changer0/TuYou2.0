package com.lulu.tuyou.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lulu.tuyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private static MessageFragment instance;

    public static MessageFragment newInstance() {
        if (instance == null) {
            synchronized (MessageFragment.class) {
                if (instance == null) {
                    instance = new MessageFragment();
                }
            }
        }
        return instance;
    }

    public MessageFragment() {
        // Required empty public constructor
        Log.d("lulu", "MessageFragment-MessageFragment  ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lulu", "MessageFragment-onAttach  ");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lulu", "MessageFragment-onCreate  ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lulu", "MessageFragment-onCreateView  ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("lulu", "MessageFragment-onActivityCreated  ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("lulu", "MessageFragment-onStart  ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lulu", "MessageFragment-onResume  ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("lulu", "MessageFragment-onPause  ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("lulu", "MessageFragment-onStop  ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("lulu", "MessageFragment-onDestroyView  ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lulu", "MessageFragment-onDestroy  ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("lulu", "MessageFragment-onDetach  ");
    }
}
