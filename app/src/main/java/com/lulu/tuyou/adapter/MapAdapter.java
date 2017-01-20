package com.lulu.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lulu on 2017/1/19.
 */

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mList;

    public MapAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text1.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text1;
        public MyViewHolder(View itemView) {
            super(itemView);
            text1 = ((TextView) itemView.findViewById(android.R.id.text1));
        }
    }

}
