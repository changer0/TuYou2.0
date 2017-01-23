package com.lulu.tuyou.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.ActivityTuYouBinding;
import com.lulu.tuyou.databinding.NavigationHeaderLoginBinding;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import cn.bmob.v3.BmobUser;

/**
 * 最基本的Activity
 * 这个Activity本身不需要Presenter
 */
public class TuYouActivity extends AppCompatActivity implements View.OnClickListener {
    private MessageFragment mMessageFragment;
    private MapFragment mMapFragment;
    private CircleFragment mCircleFragment;
    private Fragment currentFragment; //当前的Fragment
    private FragmentManager mManager;
    private TuYouUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lulu", "TuYouActivity-onCreate  执行");
        //设置沉浸式状态栏
        Utils.setTranslucentStatusBar(this, true);
        ActivityTuYouBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tu_you);
        mManager = getSupportFragmentManager();
        mCurrentUser = Constant.currentUser;
        //init presenter
        //init DataBinding
        initView(binding);
        //进入时手动点击 消息 的Fragment(暂时没有什么好的办法来解决这个问题)
        if (savedInstanceState == null) {
            RadioButton rbMsg = binding.mainRbMsg;
            rbMsg.setChecked(true);
            clickMessageFragment();
        }


    }

    private void initView(ActivityTuYouBinding binding) {
        NavigationView navigation = binding.mainNavigation;
        binding.mainRbMsg.setOnClickListener(this);
        binding.mainRbMap.setOnClickListener(this);
        binding.mainRbCircle.setOnClickListener(this);
        initNavigation(navigation);
    }

    private void initNavigation(NavigationView navigation) {
        View view = navigation.getHeaderView(0);
        if (view != null) {
            NavigationHeaderLoginBinding binding = DataBindingUtil.bind(view);
            binding.headerUserName.setText(mCurrentUser.getNickName());
            Glide.with(this)
                    .load(mCurrentUser.getIcon())
                    .transform(new GlideCircleTransform(this))
                    .into(binding.headerImg);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_rb_msg:
                clickMessageFragment();
                break;
            case R.id.main_rb_map:
                clickMapFragment();
                break;
            case R.id.main_rb_circle:
                clickCircleFragment();
                break;
        }
    }

    /**
     * 添加或显示Fragment
     *
     * @param fragment
     */
    private void addOrShowFragment(Fragment fragment, FragmentTransaction transaction) {

        if (currentFragment == null) {
            transaction.add(R.id.tuyou_container, fragment).commit();
            currentFragment = fragment;
            return;
        }
        if (currentFragment == fragment) {
            return;
        }
        // 如果当前的fragment没有被添加，则添加
        if (!fragment.isAdded()) {
            transaction.hide(currentFragment)
                    .add(R.id.tuyou_container, fragment)
                    .commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    public void clickMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = MessageFragment.newInstance();
        }

        addOrShowFragment(mMessageFragment, mManager.beginTransaction());
        Log.d("lulu", "TuYouPresenterImpl-clickMessageFragment  name： " + currentFragment.getClass().getName());
    }

    public void clickMapFragment() {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
        }
        addOrShowFragment(mMapFragment, mManager.beginTransaction());
    }

    public void clickCircleFragment() {
        if (mCircleFragment == null) {
            mCircleFragment = CircleFragment.newInstance();
        }
        addOrShowFragment(mCircleFragment, mManager.beginTransaction());
    }

}