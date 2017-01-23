package com.lulu.tuyou.common;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lulu on 2017/1/22.
 * 单类型 item 的 RecyclerView 对应的 Adapter
 */

public class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private static HashMap<String, CommonRecyclerViewHolder.ViewHolderCreator> creatorHashMap = new HashMap<>();
    private Class<?> vhClass;
    //数据源
    protected List<T> dataList = new ArrayList<>();

    public CommonRecyclerAdapter() {
        super();
    }

    public CommonRecyclerAdapter(Class<?> vhClass) {
        this.vhClass = vhClass;
    }

    /**
     * 设置数据，会清空之前的数据
     *
     * @param datas
     */
    public void setDataList(List<T> datas) {
        dataList.clear();
        if (datas != null) {
            dataList.addAll(datas);
        }
    }

    /**
     * 添加数据，默认在最后插入，以前数据保留
     *
     * @param datas
     */
    public void addDataList(List<T> datas) {
        dataList.addAll(datas);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (vhClass != null) {
            try {
                //不设置holder直接抛出异常
                throw new IllegalArgumentException("please use CommonListAdapter(Class<VH> vhClass)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CommonRecyclerViewHolder.ViewHolderCreator<?> creator = null;
        if (creatorHashMap.containsKey(vhClass.getName())) {
            creator = creatorHashMap.get(vhClass.getName());
        } else {
            try {
                creator = ((CommonRecyclerViewHolder.ViewHolderCreator) vhClass.getField("HOLDER_CREATOR").get(null));
                creatorHashMap.put(vhClass.getName(), creator);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (creator != null) {
            return creator.createByViewGroupAndType(parent, viewType);
        } else {
            throw new IllegalArgumentException(vhClass.getName() + " HOLDER_CREATOR should be instantiated");
        }
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        if (position >= 0 && position < dataList.size()) {
            holder.bindData(dataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
