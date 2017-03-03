package com.lulu.tuyou.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lulu.tuyou.R;
import com.lulu.tuyou.presenter.CirclePresenterImpl;
import com.lulu.tuyou.presenter.ICirclePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends Fragment implements ICircleView {
    private static CircleFragment instance;
    private ICirclePresenter mPresenter;
    private Context mContext;

    public static CircleFragment newInstance() {
        if (instance == null) {
            synchronized (CircleFragment.class) {
                if (instance == null) {
                    instance = new CircleFragment();
                }
            }
        }
        return instance;
    }

    public CircleFragment() {
        // Required empty public constructor
        throw new RuntimeException("不能使用构造方法创建Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mPresenter = new CirclePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circle, container, false);
    }

}
