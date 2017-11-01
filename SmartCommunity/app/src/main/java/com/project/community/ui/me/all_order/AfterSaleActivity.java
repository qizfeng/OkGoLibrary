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
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.GoodsModel;
import com.project.community.model.OrderModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.AfterSaleApdater;
import com.project.community.ui.adapter.MyOrderApdater;
import com.project.community.util.NetworkUtils;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

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
    private List<OrderModel> mData = new ArrayList<>();
    
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AfterSaleApdater(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                GoodsOrderActivity.startActivity(AfterSaleActivity.this,mData.get(groupPosition),0);
            }
        });
        onRefresh();

    }

    @Override
    public void onRefresh() {
        getData();
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


    /**
     * D57售后订单列表
     */

    private void getData() {

        setRefreshing(true);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            setRefreshing(false);
            return;
        }

        serverDao.getOrder(
                getUser(this).id,
                "3",
                new JsonCallback<BaseResponse<List<OrderModel>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<OrderModel>> listBaseResponse, Call call, Response response) {
                        setRefreshing(false);
                        mData.clear();
                        mData.addAll(listBaseResponse.retData);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        setRefreshing(false);
                        showToast(e.getMessage());
                    }
                });
    }


}
