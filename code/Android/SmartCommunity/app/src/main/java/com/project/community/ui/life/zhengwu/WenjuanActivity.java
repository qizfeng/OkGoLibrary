package com.project.community.ui.life.zhengwu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.WenjuanModel;
import com.project.community.ui.adapter.WenjuanAdapter;
import com.project.community.ui.life.SearchActivity;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qizfeng on 17/7/28.
 * 问卷
 */

public class WenjuanActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private WenjuanAdapter mAdapter;
    private List<WenjuanModel> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenjuan);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.title_wenjuan), R.mipmap.iv_back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        initData();
        mAdapter = new WenjuanAdapter(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(WenjuanActivity.this, WenjuanDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

    }


    private void initData() {
        mData = new ArrayList<>();
        WenjuanModel model1 = new WenjuanModel();
        for (int i = 0; i < 20; i++) {
            mData.add(model1);
        }
    }

    @Override
    public void onRefresh() {
        setRefreshing(false);
    }

    /**
     * 设置是否刷新动画
     *
     * @param refreshing true开始刷新动画 false结束刷新动画
     */
    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
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
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Bundle bundle = new Bundle();
                bundle.putString("type", "mobile");
                bundle.putInt("index", 0);
                SearchActivity.startActivity(WenjuanActivity.this, bundle);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
