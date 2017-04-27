package com.lulu.tuyou.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lulu.tuyou.R;
import com.lulu.tuyou.databinding.CircleMenuBinding;

/**
 * Created by zhanglulu on 2017/4/27.
 * 创建用于显示图友圈添加信息
 */

public class CircleMenuDialog extends AppCompatDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CircleMenuBinding binding =  DataBindingUtil.inflate(inflater, R.layout.circle__menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.gravity = Gravity.RIGHT|Gravity.TOP;
        lp.x =  20;
        lp.y = 30;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

}
