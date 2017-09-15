package com.project.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.community.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/8/13.
 */

public class SearchHistoryAdapter extends BaseAdapter {
    private List<String> mData = new ArrayList<>();
    private Context mContext;
    private OnDeleteClickListener mOnDeleteClickListener;

    public SearchHistoryAdapter(Context mContext, List<String> mData, OnDeleteClickListener onDeleteClickListener) {
        this.mData = mData;
        this.mContext = mContext;
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_search_history, viewGroup, false);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDeleteClickListener.onDeleteClick(view, i);
            }
        });

        TextView tv_content = (TextView)view.findViewById(R.id.contentTextView);
        tv_content.setText(mData.get(i));
        return view;
    }

    public interface OnDeleteClickListener {

        /**
         * 删除按钮点击事件
         *
         * @param view
         * @param position
         */
        void onDeleteClick(View view, int position);
    }
}
