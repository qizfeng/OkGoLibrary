package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.adapter.TransportationApdater;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class PublicTransportationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private TransportationApdater mAdapter;
    private List<CommentModel> modelList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_transportation);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.public_transportation), R.mipmap.iv_back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        for (int i = 0; i < 10; i++) {
            CommentModel commentModel =new CommentModel();
            modelList.add(commentModel);
        }
        mAdapter = new TransportationApdater(modelList, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.showLongToast(PublicTransportationActivity.this,position+"");
                Intent intent =new Intent(PublicTransportationActivity.this,TransportationDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        setRefreshing(false);
        loadData();
    }
    private void loadData() {
//        String userId;
//        if (isLogin(this))
//            userId = getUser(this).id;
//        else
//            userId = "";
//        serverDao.getTopicDetail(userId, artId, new JsonCallback<BaseResponse<ArticleModel>>() {
//            @Override
//            public void onSuccess(final BaseResponse<ArticleModel> baseResponse, Call call, Response response) {
//                mData = baseResponse.retData;
//                try {
//                    if (0 == baseResponse.retData.status) {
//                        menuItem.setIcon(R.mipmap.d4_shoucang1);
//                    } else if (1 == baseResponse.retData.status) {
//                        menuItem.setIcon(R.mipmap.d4_shoucang1_p);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (baseResponse.retData.surveyInfo != null) {
//                    if (!TextUtils.isEmpty(baseResponse.retData.surveyInfo.id))
//                        mBtnWenjuan.setVisibility(View.VISIBLE);
//                    else
//                        mBtnWenjuan.setVisibility(View.GONE);
//                } else {
//                    mBtnWenjuan.setVisibility(View.GONE);
//                }
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            LogUtils.e("url:" + AppConstants.HOST + baseResponse.retData.url);
//                            mWebView.loadUrl(AppConstants.HOST + baseResponse.retData.url);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                getComments(artId);
//
//            }
//        });

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
}
