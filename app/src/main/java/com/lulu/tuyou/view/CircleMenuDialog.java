package com.lulu.tuyou.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.CircleMenuBinding;
import com.lulu.tuyou.utils.Utils;

/**
 * Created by zhanglulu on 2017/4/27.
 * 创建用于显示图友圈添加信息
 */

public class CircleMenuDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private CircleMenuBinding mBinding;
    private Context mContext;
    private static CircleMenuDialog sDialog = null;

    public static CircleMenuDialog getInstance() {
        if (sDialog == null) {
            synchronized (CircleMenuDialog.class) {
                if (sDialog == null) {
                    sDialog = new CircleMenuDialog();
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
        switch (v.getId()) {
            case R.id.common_title_menu_camera:
                Toast.makeText(mContext, "相机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.common_title_menu_photo_text:
                Toast.makeText(mContext, "照片", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
