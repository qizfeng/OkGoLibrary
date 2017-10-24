package com.project.community.ui.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.fragment.IssueFragment;
import com.project.community.fragment.MyReplyForumFragment;
import com.project.community.ui.adapter.BaseFragmentPageAdapter;
import com.project.community.util.TableLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/24 09:56
 * em：617716355@qq.com
 */
public class MyForumActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.bbs_tablayout)
    TabLayout bbsTablayout;
    @Bind(R.id.bbs_viewpager)
    ViewPager bbsViewpager;



    List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tablist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);
        setTitles();
        initFragment();
    }
    private void initFragment() {
        fragmentList.add(new IssueFragment());
        fragmentList.add(new MyReplyForumFragment());

        tablist.add("我的发帖");
        tablist.add("我的回帖");


        bbsTablayout.setTabMode(TabLayout.MODE_FIXED);
        BaseFragmentPageAdapter baseFragmentPageAdapter =
                new BaseFragmentPageAdapter(getSupportFragmentManager(), fragmentList, tablist);
        bbsViewpager.setOffscreenPageLimit(fragmentList.size());
        bbsViewpager.setAdapter(baseFragmentPageAdapter);
        this.bbsTablayout.setupWithViewPager(bbsViewpager);
        this.bbsTablayout.setTabsFromPagerAdapter(baseFragmentPageAdapter);
        TableLayoutHelper.setIndicator(this.bbsTablayout,55,55);
    }

    private void setTitles() {
        initToolBar(toolbar, tvTitle, true, "我的帖子", R.mipmap.iv_back);
    }
}
