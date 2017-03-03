package com.lulu.tuyou.view;

import android.app.ActionBar;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lulu.tuyou.R;
import com.lulu.tuyou.adapter.AttentionAdapter;
import com.lulu.tuyou.adapter.MapAdapter;
import com.lulu.tuyou.common.Constant;
import com.lulu.tuyou.databinding.ActivityAttentionBinding;
import com.lulu.tuyou.model.TuYouRelation;
import com.lulu.tuyou.model.TuYouUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class AttentionActivity
        extends AppCompatActivity {

    private AttentionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_attention);
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attention_recycler);
        mAdapter = new AttentionAdapter(this, new Vector<TuYouUser>());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //从网络中获取到 “我” 关注的人
        TuYouUser currentUser = Constant.currentUser;
        final BmobQuery<TuYouRelation> query = new BmobQuery<>();
        query.addWhereEqualTo("fromUser", currentUser.getObjectId());
        query.findObjects(new FindListener<TuYouRelation>() {
            @Override
            public void done(List<TuYouRelation> list, BmobException e) {
                if (e == null) {
                    int size = list.size();
                    if (list != null && size > 0) {
                        mAdapter.clearAll();

                        for (int i = 0; i < size; i++) {
                            TuYouRelation relation = list.get(i);
                            final TuYouUser toUser = relation.getToUser();
                            if (toUser != null) {
                                BmobQuery<TuYouUser> userQuery = new BmobQuery<>();
                                    final int indexI = i;
                                    userQuery.getObject(toUser.getObjectId(), new QueryListener<TuYouUser>() {
                                        @Override
                                        public void done(TuYouUser user, BmobException e) {
                                                mAdapter.add(user);
                                                Log.d("lulu", "AttentionActivity-done  index:" + indexI);
                                        }

                                    });
                            }
                        }

                    }
                } else {
                    Log.d("lulu", "AttentionActivity-done  出现问题：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });


    }
}
