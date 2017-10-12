package com.project.community.ui.life.minsheng;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.model.ShopModel;
import com.project.community.ui.adapter.MinshengAdapter;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/10/12.
 * 社區商圈
 */

public class ShopsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private int page = 1;//当前页码
    private MinshengAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_shop_list), R.mipmap.iv_back);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new MinshengAdapter(null, new MinshengAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);

        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    private void loadData() {
        String latitute = getLocation(this)[0];
        String longitute = getLocation(this)[1];
        String locData = "";
        if (!TextUtils.isEmpty(latitute) && !TextUtils.isEmpty(longitute)) {
            locData = longitute + "," + latitute;
        }
        serverDao.getMinshengIndexData(locData, page, AppConstants.PAGE_SIZE, new JsonCallback<BaseResponse<List<ShopModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<ShopModel>> baseResponse, Call call, Response response) {
                if (page == 1) {
                    List<ShopModel> data = new ArrayList<>();
                    data.addAll(baseResponse.retData);
                    mAdapter.setNewData(data);
                    mAdapter.setEnableLoadMore(true);
                } else {
                    //显示没有更多数据
                    LogUtils.e("page:" + page);
                    if (baseResponse.retData.size() < AppConstants.PAGE_SIZE && page != 1) {
                        List<ShopModel> data = new ArrayList<>();
                        data.addAll(baseResponse.retData);
                        mAdapter.addData(data);
                        mAdapter.loadMoreEnd();         //加载完成
                    } else {
                        List<ShopModel> data = new ArrayList<>();
                        data.addAll(baseResponse.retData);
                        mAdapter.addData(data);
                        mAdapter.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onAfter(@Nullable BaseResponse<List<ShopModel>> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                //可能需要移除之前添加的布局
                mAdapter.removeAllFooterView();
                //结束刷新动画
                setRefreshing(false);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

            }
        });
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
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        if (page == 1 && mAdapter.getData().size() < AppConstants.PAGE_SIZE) {
            mAdapter.setEnableLoadMore(false);
            return;
        }
        mAdapter.setEnableLoadMore(true);
        page++;
        loadData();
    }
}
