package com.lulu.tuyou.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.services.nearby.NearbySearch;
import com.bumptech.glide.Glide;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.ActivityTuYouBinding;
import com.lulu.tuyou.databinding.NavigationContentBinding;
import com.lulu.tuyou.databinding.NavigationHeaderBinding;
import com.lulu.tuyou.model.CustomUserProvider;
import com.lulu.tuyou.model.TuYouUser;
import com.lulu.tuyou.utils.Utils;

import cn.bmob.v3.BmobUser;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;


/**
 * 最基本的Activity
 * 这个Activity本身不需要Presenter
 */
public class TuYouActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private LCIMConversationListFragment mMessageFragment;
    private MapFragment mMapFragment;
    private CircleFragment mCircleFragment;
    private Fragment currentFragment; //当前的Fragment
    private FragmentManager mManager;
    private TuYouUser mCurrentUser;
    private RadioGroup mRadioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("lulu", "TuYouActivity-onCreate  执行");
        ActivityTuYouBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tu_you);
        mManager = getSupportFragmentManager();
        mCurrentUser = Constant.currentUser;
        //init presenter
        //init DataBinding
        initView(binding);
        //进入时手动点击 消息 的Fragment(暂时没有什么好的办法来解决这个问题)
        if (savedInstanceState == null) {
            clickMapFragment();
            mRadioBtn.check(R.id.main_rb_msg);
        }

        //添加本身到KitUser上
        addToCurrentUserToKit();
    }

    private void addToCurrentUserToKit() {
        CustomUserProvider.getPartUsers().add(
                new LCChatKitUser(mCurrentUser.getObjectId(),
                        mCurrentUser.getNickName(),
                        mCurrentUser.getIcon()
                ));
    }

    private void initView(ActivityTuYouBinding binding) {
        mRadioBtn = binding.mainRg;
        mRadioBtn.setOnCheckedChangeListener(this);
        initNavigation(binding);
    }

    private void initNavigation(ActivityTuYouBinding rootBinding) {
        NavigationHeaderBinding header = rootBinding.mainNavHeader;
        NavigationContentBinding content = rootBinding.mainNavContent;
        Glide.with(this).load(mCurrentUser.getIcon())
                .transform(new GlideCircleTransform(this))
                .into(header.headerImg);
        header.headerUserName.setText(mCurrentUser.getNickName());
        header.navigationHeader.setOnClickListener(this);
        content.navigationLogout.setOnClickListener(this);
        content.itemNavigationAttention.setOnClickListener(this);
        content.itemNavigationAbout.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_logout:
                loginOut();
                break;
            case R.id.item_navigation_attention:
                //Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AttentionActivity.class);
                startActivity(intent);
                /**
                 * overridePendingTransition方法需要在startAtivity方法或者是finish方法调用之后立即执行

                 参数enterAnim表示的是从Activity a跳转到Activity b，进入b时的动画效果

                 参数exitAnim表示的是从Activity a跳转到Activity b，离开a时的动过效果

                 若进入b或者是离开a时不需要动画效果，则可以传值为0
                 */
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
                break;
            case R.id.item_navigation_about:
                startActivity(new Intent(this, MineAboutActivity.class));
                break;
            case R.id.navigation_header:
                startActivity(new Intent(this, MineUserActivity.class));
                break;

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 退出登录
    ///////////////////////////////////////////////////////////////////////////
    private void loginOut() {
        NearbySearch.getInstance(this).setUserID(Constant.currentUser.getObjectId());
        NearbySearch.getInstance(this).clearUserInfoAsyn();//异步清楚用户信息
        NearbySearch.destroy();//登出后记得销毁附近功能
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您确定要登出吗？点击确定将返回登录页面！")
                .setTitle("警告")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface anInterface, int i) {
                        BmobUser.logOut();
                        //删除Push中的Id
                        Utils.removeInstallationId();
                        startActivity(new Intent(TuYouActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface anInterface, int i) {
                anInterface.dismiss();
            }
        }).create().show();

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
            mMessageFragment = new LCIMConversationListFragment();
        }

        addOrShowFragment(mMessageFragment, mManager.beginTransaction());
        //Log.d("lulu", "TuYouPresenterImpl-clickMessageFragment  name： " + currentFragment.getClass().getName());
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

    private long prePressTime;
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - prePressTime < 500) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            prePressTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        String action = intent.getAction();
//        final TuYouUser user = (TuYouUser) intent.getSerializableExtra(Constant.PUSH_HI_START_ACTIVITY_USER);
//        if (user != null) {
//            if (Constant.PUSH_HI_START_ACTIVITY_ACTION.equals(action)) {
//
//
//            }
//        }

    }

}