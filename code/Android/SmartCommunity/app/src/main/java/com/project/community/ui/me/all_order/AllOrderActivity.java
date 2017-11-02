package com.project.community.ui.me.all_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.life.SearchActivity;
import com.project.community.util.TablayoutLineReflex;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cj on 17/10/24.
 * 所有订单
 */

public class AllOrderActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.bbs_tablayout)
    TabLayout tabLayout;
    @Bind(R.id.bbs_viewpager)
    ViewPager mViewpager;

    private String shopId;
    private MyFrageStatePagerAdapter mAdapter;
    List<Fragment> mFragmentsList = new ArrayList<>();

    public static void startActivity(Context context,String shopId){
        Intent intent = new Intent(context,AllOrderActivity.class);
        intent.putExtra("shopId",shopId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        ButterKnife.bind(this);
        ininData();
    }

    private void ininData() {
        shopId=getIntent().getStringExtra("shopId");
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.all_order_title), R.mipmap.iv_back);
        mFragmentsList.add(AllOrderFragment.newInstance("",shopId));
        mFragmentsList.add(AllOrderFragment.newInstance("0",shopId));
        mFragmentsList.add(AllOrderFragment.newInstance("1",shopId));
        mFragmentsList.add(AllOrderFragment.newInstance("2",shopId));
        mFragmentsList.add(AllOrderFragment.newInstance("3",shopId));//售后
        mFragmentsList.add(AllOrderFragment.newInstance("4",shopId));
        mAdapter = new MyFrageStatePagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(mFragmentsList.size());
        tabLayout.setupWithViewPager(mViewpager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineReflex.setTabLine(AllOrderActivity.this, tabLayout, 10, 10);
            }
        });
    }

    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm;
        private String[] mTitles = new String[]{"全部","待发货","已发货","已完成","售后","已关闭"};

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
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
                SearchActivity.startActivity(AllOrderActivity.this, bundle);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
