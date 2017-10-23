package com.project.community.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.community.R;
import com.project.community.ui.me.MessageCenterActivity;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/23 19:12
 * em：617716355@qq.com
 */
public class MessageListAdapter extends SwipeMenuAdapter<MessageListAdapter.DefaultViewHolder> {


    private List<String> mDatas = new ArrayList<>();


//    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    public MessageListAdapter(Context context, List<String> data) {
        mContext = context;
        mDatas = data;

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DefaultViewHolder holder, final int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.startActivity(new Intent(mContext, MessageCenterActivity.class));
            }
        });

    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder {


        DefaultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
