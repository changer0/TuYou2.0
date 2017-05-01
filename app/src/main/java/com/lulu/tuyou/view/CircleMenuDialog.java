package com.lulu.tuyou.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.CircleMenuBinding;
import com.lulu.tuyou.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by zhanglulu on 2017/4/27.
 * 创建用于显示图友圈添加信息
 */

public class CircleMenuDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private CircleMenuBinding mBinding;
    private Context mContext;
    private static CircleMenuDialog sDialog = null;
//    private File mFile;
    private ICircleView mICircleView;

    public CircleMenuDialog(ICircleView ICircleView) {
        super();
        mICircleView = ICircleView;
    }

    public static CircleMenuDialog getInstance(ICircleView iCircleView) {
        if (sDialog == null) {
            synchronized (CircleMenuDialog.class) {
                if (sDialog == null) {
                    sDialog = new CircleMenuDialog(iCircleView);
                }
            }
        }
        return sDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CircleMenuBinding binding =  DataBindingUtil.inflate(inflater, R.layout.circle__menu, container, false);
        mBinding = binding;
        View rootView = binding.getRoot();
        mBinding.commonTitleMenuCamera.setOnClickListener(this);
        mBinding.commonTitleMenuPhotoText.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        //设置Dialog背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.gravity = Gravity.RIGHT|Gravity.TOP;
        lp.y = getResources().getDimensionPixelOffset(R.dimen.common_title_height);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        mICircleView.onClickMenu(v);
//        switch (v.getId()) {
//            case R.id.common_title_menu_camera:
//                //Toast.makeText(mContext, "相机", Toast.LENGTH_SHORT).show();
//                //跳转到相机进行拍照取图
//                mFile = Utils.jumpToCamera(this, REQUREST_CODE_CAMERA);
//                break;
//            case R.id.common_title_menu_photo_text:
//                Toast.makeText(mContext, "照片", Toast.LENGTH_SHORT).show();
//                break;
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);   //this
//        switch (requestCode) {
//            case REQUREST_CODE_CAMERA:
//                Utils.cropImage(this, mFile, REQUREST_CODE_CROP);
//                break;
//            case REQUREST_CODE_CROP:
//                Toast.makeText(mContext, "裁剪完成", Toast.LENGTH_SHORT).show();
//                break;
//        }


    }

}
