package com.lulu.tuyou.view;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.FragmentMessageBinding;
import com.lulu.tuyou.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private static MessageFragment instance;
    private Context mContext;

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
        //Log.d("lulu", "MessageFragment-MessageFragment  ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);

        Toolbar toolbar = binding.msgToolBar;
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                toolbar.setPadding(toolbar.getPaddingLeft(),
                        Utils.getStatusBarHeight(mContext) + toolbar.getPaddingTop(),
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());
            }
            appCompatActivity.setSupportActionBar(toolbar);
        }


        binding.msgJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
