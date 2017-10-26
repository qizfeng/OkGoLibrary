package com.project.community.ui.me.all_order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.GoodsModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.AfterSaleApdater;
import com.project.community.ui.adapter.MyOrderApdater;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AfterSaleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;


    private AfterSaleApdater mAdapter;
    private List<ShoppingCartModel> mData = new ArrayList<>();
    
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AfterSaleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale);
        ButterKnife.bind(this);
        initToolBar(toolbar, tvTitle, true, getString(R.string.my_order_address_shouhou), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        mData = getListData();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AfterSaleApdater(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                GoodsOrderActivity.startActivity(AfterSaleActivity.this,4);
            }
        });

    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
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

    public ArrayList<ShoppingCartModel> getListData() {
        ArrayList<ShoppingCartModel> myParents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShoppingCartModel myParent = getShoppingCart();
            myParents.add(myParent);
        }
        return myParents;
    }

    public ShoppingCartModel getShoppingCart() {
        ShoppingCartModel myParent = new ShoppingCartModel();
        ArrayList<GoodsModel> myChildren = new ArrayList<>();
        for (int j = 0; j < 1; j++) {
            GoodsModel myChild = new GoodsModel();
            myChildren.add(myChild);
        }
        myParent.goods = myChildren;
        return myParent;
    }
}
