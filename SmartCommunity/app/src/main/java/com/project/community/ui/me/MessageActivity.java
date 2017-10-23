package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：fangkai on 2017/10/23 18:28
 * em：617716355@qq.com
 */
public class MessageActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.ll_set_message)
    LinearLayout llSetMessage;
    @Bind(R.id.ll_repairs)
    LinearLayout llRepairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        setTitles();
    }


    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "消息中心", R.mipmap.iv_back);
    }


    @OnClick({R.id.ll_set_message, R.id.ll_repairs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_set_message:
                startActivity(new Intent(this,MessageListActivity.class));
                break;
            case R.id.ll_repairs:
                startActivity(new Intent(this,MessageListActivity.class));
                break;
        }
    }
}
