package com.lulu.tuyou.viewholder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lulu.tuyou.R;
import com.lulu.tuyou.common.CommonRecyclerViewHolder;
import com.lulu.tuyou.databinding.MapItemBinding;

/**
 * Created by lulu on 2017/1/22.
 *
 */

public class MapViewHolder extends CommonRecyclerViewHolder<String> {
    private TextView mTextView;

    public MapViewHolder(ViewGroup root) {
        super(root.getContext(), root, R.layout.map_item);
        MapItemBinding binding = DataBindingUtil.bind(itemView);
        initView(binding);
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator() {
        @Override
        public CommonRecyclerViewHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
            return new MapViewHolder(parent);
        }
    };

    private void initView(MapItemBinding binding) {
        mTextView = binding.text;
    }

    @Override
    public void bindData(String string) {
        mTextView.setText(string);
    }
}
