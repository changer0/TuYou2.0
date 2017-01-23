package com.lulu.tuyou.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by lulu on 2017/1/22.
 * RecyclerView.ViewHolder 的公共类，做了一些封装。目的：
 * ViewHolder 与 Adapter 解耦，和 ViewHolder 相关的逻辑都放到 ViewHolder 里边，避免 Adapter 有相关逻辑
 */
public abstract class CommonRecyclerViewHolder<T> extends RecyclerView.ViewHolder {


    public CommonRecyclerViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
    }

    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 用给定的 data 对 holder 的 view 进行赋值
     */
    public abstract void bindData(T t);

    /**
     * 因为 CommonRecyclerAdapter 里边无
     * 法对于未知类型的 Class 进行实例化
     * 所以需要如果想用 CommonRecyclerAdapter，必须要在对应的 CommonViewHolder 实例化一个 HOLDER_CREATOR
     * 注意：public static ViewHolderCreator HOLDER_CREATOR，名字与修饰符都不能更改，否则有可能引发失败
     * 如果不使用 CommonListAdapter，则不需要实例化 ViewHolderCreator
     *
     * @param <VH>
     */
    public interface ViewHolderCreator<VH extends CommonRecyclerViewHolder> {
        VH createByViewGroupAndType(ViewGroup parent, int viewType);
    }
}
