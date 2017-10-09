package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.ArticleDetailsImagsAdapter;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.adapter.TransportationDeailApdater;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TransportationDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
    private View header;
    private TextView mHeadName,mHeadBanci;
    private ImageView mHeadChangge;
    private TransportationDeailApdater mAdapter;
    private List<CommentModel> comments =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_details);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initToolBar(mToolBar, mTvTitle, true, "1路", R.mipmap.iv_back);
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_transit_detail, null);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransportationDetailsActivity.this,TransportationBaiduMapActivity.class);
                startActivity(intent);
            }
        });
        mHeadName = (TextView) header.findViewById(R.id.head_name);
        mHeadBanci = (TextView) header.findViewById(R.id.head_banci);
        mHeadChangge = (ImageView) header.findViewById(R.id.head_changge);
        mHeadChangge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comments.size()>0){
                    Collections.reverse(comments); // 倒序排列
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration = new SpacesItemDecoration(0, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        for (int i = 0; i < 10; i++) {
            CommentModel commentModel =new CommentModel();
            if (i==6){
                commentModel.id="1";
            }else {
                commentModel.id="0";
            }
            comments.add(commentModel);
        }
        mAdapter = new TransportationDeailApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                position = position - 1;//去掉头部

            }

            @Override
            public void onCustomClick(View view, int position) {
                position = position - 1;//去除头部
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(header);
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        setRefreshing(false);
        loadData();
    }
    private void loadData() {

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
