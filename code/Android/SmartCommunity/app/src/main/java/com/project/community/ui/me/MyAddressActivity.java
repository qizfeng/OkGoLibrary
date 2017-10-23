package com.project.community.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.adapter.MyAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：fangkai on 2017/10/23 14:23
 * em：617716355@qq.com
 */
public class MyAddressActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.rv_address)
    RecyclerView rvAddress;
    @Bind(R.id.tv_new_address)
    TextView tvNewAddress;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;


    private MyAddressAdapter myAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);
        steTitle();

        steAdapter();
    }

    private void steTitle() {

        initToolBar(toolbar, tvTitle, true, "地址管理", R.mipmap.iv_back);

    }


    private void steAdapter() {

        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mData.add("s");
        }

        myAddressAdapter = new MyAddressAdapter(R.layout.item_my_address, mData);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(myAddressAdapter);
        myAddressAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        myAddressAdapter.loadMoreEnd();

                    }

                }, 1000);
            }
        }, rvAddress);


        myAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });


        swipeRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRl.setRefreshing(false);
                    }
                }, 1000);
            }
        });


    }


    @OnClick(R.id.tv_new_address)
    public void onViewClicked() {

        startActivity(new Intent(this,AddAddressActivity.class));
    }
}
