package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/23 19:53
 * em：617716355@qq.com
 */
public class MessageCenterActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        setTitles();
    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "消息中心", R.mipmap.iv_back);
    }
}
