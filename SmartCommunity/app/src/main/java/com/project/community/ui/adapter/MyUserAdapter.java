package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.base.BaseRecyclerAdapter;
import com.project.community.bean.MyUserBean;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/31 15:25
 * em：617716355@qq.com
 */
public class MyUserAdapter extends BaseRecyclerAdapter<MyUserBean, MyUserAdapter.ViewHolder> {


    private static HashMap<String, Integer> letterIndex = new HashMap<String, Integer>();

    public MyUserAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDatas.get(position).getName());

        if (position > 0) {
            if (mDatas.get(position).getPy().equals(mDatas.get(position - 1).getPy())) {
                holder.tvCityNameHead.setVisibility(View.GONE);
            } else {
                holder.tvCityNameHead.setVisibility(View.VISIBLE);
                holder.tvCityNameHead.setText(mDatas.get(position).getPy());
                letterIndex.put(mDatas.get(position).getPy(),position);
            }
        }else {
            holder.tvCityNameHead.setVisibility(View.VISIBLE);
            holder.tvCityNameHead.setText(mDatas.get(position).getPy());
            letterIndex.put(mDatas.get(position).getPy(),position);
        }


    }


    static class ViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_cityNameHead)
        TextView tvCityNameHead;
        @Bind(R.id.iv_header)
        ImageView ivHeader;
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static HashMap<String, Integer> getLetterIndex() {
        return letterIndex;
    }
}
