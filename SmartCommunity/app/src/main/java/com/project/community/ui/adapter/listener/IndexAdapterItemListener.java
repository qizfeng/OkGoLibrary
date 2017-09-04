package com.project.community.ui.adapter.listener;

import android.view.View;

/**
 * Created by qizfeng on 17/7/10.
 * 首页RecycleView item点击事件
 */

public  interface IndexAdapterItemListener {
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
    void onTextClick(View view, int position);


    /**
     * 点赞
     * @param view
     * @param position
     */
   // void onPraiseClick(View view,int position);

    /**
     * 评论
     * @param view
     * @param position
     */
    void onCommentClick(View view,int position);

}
