package com.project.community.ui.me.shop_management;

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
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.util.TablayoutLineReflex;
import com.project.community.view.MyButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by cj on 17/10/24.
 * 全部商品
 */

public class AllProductsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewpager;
    @Bind(R.id.product_add)
    MyButton productAdd;

    private List<Fragment> mFragmentsList;
    private MyFrageStatePagerAdapter mAdapter;

    private String shopId;


    public static void startActivity(Context context,String shopId){
        Intent intent = new Intent(context,AllProductsActivity.class);
        intent.putExtra("shopId",shopId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.products_manager_all), R.mipmap.iv_back);
        initTabLayout();
        initData();
    }

    private void initData() {
        shopId=getIntent().getStringExtra("shopId");
        mFragmentsList =new ArrayList<>();
        mFragmentsList.add(SellFragment.newInstance(0,shopId));
        mFragmentsList.add(SellFragment.newInstance(1,shopId));
        mFragmentsList.add(SellFragment.newInstance(2,shopId));
        mAdapter = new MyFrageStatePagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(mFragmentsList.size());

    }

    private void initTabLayout() {

        tabLayout.setupWithViewPager(mViewpager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineReflex.setTabLine(AllProductsActivity.this, tabLayout, 10, 10);
            }
        });
    }

    @OnClick(R.id.product_add)
    public void onViewClicked() {
        BuildNewGoodsActivity.startActivity(this,null,shopId);
    }
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm;
        private String[] mTitles = new String[]{
                getResources().getString(R.string.products_manager_sell_ing),
                getResources().getString(R.string.products_manager_sell_end),
                getResources().getString(R.string.products_manager_sell_xia)
        };

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

}
