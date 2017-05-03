package com.lulu.tuyou.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.ActivityCircleImageBinding;
import com.lulu.tuyou.model.CircleImageShowBean;
import com.lulu.tuyou.utils.CircleImageLoader;
import com.yzs.imageshowpickerview.ImageShowPickerBean;
import com.yzs.imageshowpickerview.ImageShowPickerListener;
import com.yzs.imageshowpickerview.ImageShowPickerView;

import java.util.ArrayList;
import java.util.List;

public class CircleImageActivity extends AppCompatActivity {
    private List<CircleImageShowBean> mImageList = new ArrayList<>();
    private ImageShowPickerView mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCircleImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_circle_image);
        mPicker = binding.circleImageShow;
        binding.commonTitle.commonTitleHeader.setText("");
        binding.commonTitle.commonTitleBack.setVisibility(View.VISIBLE);
        binding.commonTitle.commonTitleBtn.setVisibility(View.VISIBLE);
        binding.commonTitle.commonTitleBtn.setText("发送");
        binding.commonTitle.commonTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToNet();
            }
        });
        binding.commonTitle.commonTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra(CircleFragment.CIRCLE_FRAGMENT_IMAGE_PATH);
        if (!TextUtils.isEmpty(imagePath)) {
            mImageList.add(new CircleImageShowBean(imagePath));
        }
        mPicker.setImageLoaderInterface(new CircleImageLoader());
        mPicker.setNewData(mImageList);
        mPicker.setPickerListener(new ImageShowPickerListener() {
            @Override
            public void addOnClickListener(int remainNum) {
                Toast.makeText(CircleImageActivity.this, "点击添加", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void picOnClickListener(List<ImageShowPickerBean> list, int position, int remainNum) {

            }

            @Override
            public void delOnClickListener(int position, int remainNum) {
                mImageList.remove(position);
            }
        });
        mPicker.show();
    }

    /**
      * 点击发送按钮将数据发送云端
     */
    private void sendDataToNet() {
        Toast.makeText(this, "点击发送", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
