package com.lulu.tuyou.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lulu.tuyou.R;


public class MineAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_about);
        ((TextView) findViewById(R.id.common_title_header)).setText("关于图友");
    }
}
