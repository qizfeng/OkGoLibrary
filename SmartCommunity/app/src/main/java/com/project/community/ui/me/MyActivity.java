package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.my_name)
    TextView myName;
    @Bind(R.id.my_gonghao)
    TextView myGonghao;
    @Bind(R.id.my_jiedan)
    TextView myJiedan;
    @Bind(R.id.my_star_1)
    ImageView myStar1;
    @Bind(R.id.my_star_2)
    ImageView myStar2;
    @Bind(R.id.my_star_3)
    ImageView myStar3;
    @Bind(R.id.my_star_4)
    ImageView myStar4;
    @Bind(R.id.my_star_5)
    ImageView myStar5;
    @Bind(R.id.layout_stars)
    LinearLayout layoutStars;
    @Bind(R.id.my_pingtai)
    TextView myPingtai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, tvTitle, true, "", R.mipmap.iv_back);


    }

    @OnClick({R.id.rl_my_kefu, R.id.rl_my_fuwu, R.id.rl_my_zhinan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_my_kefu:
                break;
            case R.id.rl_my_fuwu:
                Intent intent
                        =new Intent(this,ServiesClauseActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_zhinan:
                break;
        }
    }
}
