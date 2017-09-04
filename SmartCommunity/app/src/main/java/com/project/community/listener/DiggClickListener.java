package com.project.community.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by qizfeng on 17/7/24.
 * 自定义点赞接口
 */

public interface DiggClickListener {
    void onDiggClick(ImageView view, TextView textView , int position);
}
