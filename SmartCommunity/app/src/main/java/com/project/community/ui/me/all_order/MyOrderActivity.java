package com.project.community.ui.me.all_order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.BaseFragmentPageAdapter;
import com.project.community.ui.me.MyAddressActivity;
import com.project.community.ui.me.MyForumActivity;
import com.project.community.util.TableLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 17/10/24.
 * 我的订单
 */

public class MyOrderActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tablist = new ArrayList<String>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initToolBar(toolbar, tvTitle, true, getString(R.string.my_order_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        fragmentList.add(MyOrderFragment.newInstance(""));
        fragmentList.add(MyOrderFragment.newInstance("0"));
        fragmentList.add(MyOrderFragment.newInstance("1"));
        fragmentList.add(MyOrderFragment.newInstance("2"));
        fragmentList.add(MyOrderFragment.newInstance("3"));

        tablist.add(getString(R.string.my_order_all));
        tablist.add(getString(R.string.my_order_wait_fahuo));
        tablist.add(getString(R.string.my_order_end));
        tablist.add(getString(R.string.my_order_wait_pingjia));
        tablist.add(getString(R.string.my_order_address_shouhou));

        BaseFragmentPageAdapter baseFragmentPageAdapter =
                new BaseFragmentPageAdapter(getSupportFragmentManager(), fragmentList, tablist);
        viewpager.setOffscreenPageLimit(fragmentList.size());
        viewpager.setAdapter(baseFragmentPageAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabsFromPagerAdapter(baseFragmentPageAdapter);
        TableLayoutHelper.setIndicator(this.tablayout, 12, 12);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setTitle(R.string.my_order_address);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/
            case R.id.action_favorite:
                if (isLogin(this))
                    startActivity(new Intent(MyOrderActivity.this, MyAddressActivity.class));
                else
                    showToast(getString(R.string.toast_no_login));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.shouhou)
    public void onViewClicked() {
        AfterSaleActivity.startActivity(this);
    }
}
