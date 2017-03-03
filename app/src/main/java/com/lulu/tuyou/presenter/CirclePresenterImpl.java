package com.lulu.tuyou.presenter;

import com.lulu.tuyou.view.ICircleView;

/**
 * Created by zhanglulu on 2017/3/3.
 *
 */

public class CirclePresenterImpl implements ICirclePresenter {
    private ICircleView mCircleView;

    public CirclePresenterImpl(ICircleView circleView) {
        mCircleView = circleView;
    }
}
