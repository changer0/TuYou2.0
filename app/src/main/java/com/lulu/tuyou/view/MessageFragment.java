package com.lulu.tuyou.view;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.FragmentMessageBinding;

import org.kymjs.chat.view.ChatActivity;

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
        //Log.d("lulu", "MessageFragment-MessageFragment  ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        binding.msgJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                getContext().startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

        return binding.getRoot();
    }
}
