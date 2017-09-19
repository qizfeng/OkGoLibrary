package com.project.community.listener;

import android.view.View;

/**
 * Created by qizfeng on 17/6/22.
 * RecyclerAdapter item点击事件,可由需求自定义添加点击事件.
 */

public interface RecycleItemClickListener {

    /**
     * 整个item的点击事件
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);
    /**
     * 文字部分点击事件
     * @param view
     * @param position
     */
    void onCustomClick(View view, int position);


}
