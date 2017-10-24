package com.project.community.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.ui.adapter.IssueForumAdapter;
import com.project.community.ui.me.MyForumDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：fangkai on 2017/10/24 13:42
 * em：617716355@qq.com
 */
public class IssueFragment extends BaseFragment {

    @Bind(R.id.rv_government)
    RecyclerView rvGovernment;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;


    private IssueForumAdapter myForumAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        steAdapter();
    }

    private void steAdapter() {
        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mData.add("s");
        }

        myForumAdapter = new IssueForumAdapter(R.layout.item_issue_forum, mData);
        rvGovernment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGovernment.setAdapter(myForumAdapter);
        myForumAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        myForumAdapter.loadMoreEnd();

                    }

                }, 1000);
            }
        }, rvGovernment);


        myForumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    startActivity(new Intent(getActivity(), MyForumDetailsActivity.class));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

