package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.PageFragmentAdapter;
import com.project.community.ui.life.SearchActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BBSActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.bbs_tablayout)
    TabLayout bbsTablayout;
    @Bind(R.id.bbs_viewpager)
    ViewPager bbsViewpager;

    PageFragmentAdapter adapter;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs);
        ButterKnife.bind(this);
        ininData();
    }

    private void ininData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.bbs), R.mipmap.iv_back);
        fragmentList.add(BbsFragment.newInstance(1));
        fragmentList.add(BbsFragment.newInstance(2));
        fragmentList.add(BbsFragment.newInstance(3));
        fragmentList.add(BbsFragment.newInstance(4));
        fragmentList.add(BbsFragment.newInstance(5));
        fragmentList.add(BbsFragment.newInstance(6));
        fragmentList.add(BbsFragment.newInstance(7));
        fragmentList.add(BbsFragment.newInstance(8));

        bbsTablayout.addTab(bbsTablayout.newTab().setText("全部"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("资讯"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境资讯"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境资讯"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境"));
        bbsTablayout.addTab(bbsTablayout.newTab().setText("环境"));
        // 设置TabLayout模式
        bbsTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // 设置从PagerAdapter中获取Tab
        adapter = new PageFragmentAdapter(getSupportFragmentManager(), fragmentList);
        bbsViewpager.setAdapter(adapter);
        bbsViewpager.setOnPageChangeListener(this);
        bbsTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                bbsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        bbsTablayout.post(new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(bbsTablayout,10,10);
//            }
//        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bbsTablayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:

                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                Bundle bundle = new Bundle();
                bundle.putString("type", "mobile");
                bundle.putInt("index", 3);
                SearchActivity.startActivity(BBSActivity.this,bundle);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @OnClick(R.id.bbs_btn_send_message)
    public void onViewClicked() {
        Intent intent =new Intent(BBSActivity.this,SendMessageActivity.class);
        startActivity(intent);
    }
}
