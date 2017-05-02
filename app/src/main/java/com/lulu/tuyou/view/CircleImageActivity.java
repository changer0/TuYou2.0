package com.lulu.tuyou.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityCircleImageBinding;

public class CircleImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCircleImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_circle_image);
        Intent intent = getIntent();

    }
}
