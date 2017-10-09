package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ServiesClauseActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.serves_clause)
    TextView servesClause;

    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servies_clause);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        code=getIntent().getIntExtra("code",1);
        if (code == 1)
            initToolBar(mToolBar, tvTitle, true, getResources().getString(R.string.my_fuwu), R.mipmap.iv_back);
        else
            initToolBar(mToolBar, tvTitle, true, getResources().getString(R.string.my_zhinan), R.mipmap.iv_back);
    }
}
