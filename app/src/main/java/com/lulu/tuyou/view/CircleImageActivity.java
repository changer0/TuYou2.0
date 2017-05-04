package com.lulu.tuyou.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.ActivityCircleImageBinding;
import com.lulu.tuyou.model.CircleImageShowBean;
import com.lulu.tuyou.model.TuYouTrack;
import com.lulu.tuyou.model.TuYouVersion;
import com.lulu.tuyou.utils.CircleImageLoader;
import com.lulu.tuyou.utils.Utils;
import com.yzs.imageshowpickerview.ImageShowPickerBean;
import com.yzs.imageshowpickerview.ImageShowPickerListener;
import com.yzs.imageshowpickerview.ImageShowPickerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 图友圈发送承载类
 */
public class CircleImageActivity extends AppCompatActivity {
    private List<CircleImageShowBean> mImageList = new ArrayList<>();
    private ImageShowPickerView mPicker;
    private ActivityCircleImageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_circle_image);
        mPicker = mBinding.circleImageShow;
        mBinding.commonTitle.commonTitleHeader.setText("");
        mBinding.commonTitle.commonTitleBack.setVisibility(View.VISIBLE);
        mBinding.commonTitle.commonTitleBtn.setVisibility(View.VISIBLE);
        mBinding.commonTitle.commonTitleBtn.setText("发送");
        mBinding.commonTitle.commonTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToNet();
            }
        });
        mBinding.commonTitle.commonTitleBack.setOnClickListener(new View.OnClickListener() {
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
                //Toast.makeText(CircleImageActivity.this, "点击添加", Toast.LENGTH_SHORT).show();
                Utils.jumpToAlbum(CircleImageActivity.this, CircleFragment.REQUREST_CODE_ALBUM);
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
        //Toast.makeText(this, "点击发送", Toast.LENGTH_SHORT).show();
        final TuYouTrack track = new TuYouTrack();
        track.setUser(Constant.currentUser);
        String textContent = mBinding.circleText.getText().toString();
        track.setText(textContent);

//        if (mImageList.size() <= 0) {
//            //说明只有文字没有图片
//            trackUpload(track);
//            return;
//        }

        final String[] imagesUpload = new String[mImageList.size()];
        for (int i = 0; i < mImageList.size(); i++) {
            CircleImageShowBean imageBean = mImageList.get(i);
            String imagePath = imageBean.getImageShowPickerUrl();
            imagesUpload[i] = imagePath;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setTitle("正在上传，请稍后...");
        progressDialog.show();
        BmobFile.uploadBatch(imagesUpload, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == imagesUpload.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    Toast.makeText(CircleImageActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    track.setImages(urls);
                    trackUpload(track);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                progressDialog.incrementProgressBy(curPercent);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleImageActivity.this, s, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });




    }

    //上传TrackModel
    private void trackUpload(TuYouTrack track) {
        track.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    Toast.makeText(CircleImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("lulu", "CircleImageActivity-done  e:" + e.getMessage() + e.getErrorCode());
                } else {
                    TuYouVersion.versionAdd();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CircleFragment.REQUREST_CODE_ALBUM:
                if (resultCode == -1) {
                    //选择完成
                    if (data != null) {
                        Uri uri = data.getData();
                        String path = Utils.getFilePathFromUri(CircleImageActivity.this, uri);
                        CircleImageShowBean imageBean = new CircleImageShowBean(path);
                        mImageList.add(imageBean);
                        mPicker.addData(imageBean);
                    }
                } else {
                    Toast.makeText(CircleImageActivity.this, "取消选择", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
