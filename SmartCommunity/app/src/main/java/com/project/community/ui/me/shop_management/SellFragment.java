package com.project.community.ui.me.shop_management;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.ui.adapter.ProductSellApdater;
import com.project.community.ui.adapter.ServiesComApdater;
import com.project.community.ui.me.OrderDetailActivity;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    ProductSellApdater mAdapter;
    List<CommentModel> comments =new ArrayList<>();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();
        for (int i = 0; i < 10; i++) {
            CommentModel commentModel =new CommentModel();
            commentModel.id="0";
            comments.add(commentModel);
        }
        mAdapter = new ProductSellApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ShopDataActivity.startActivity(getActivity());
            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()){
                    case R.id.item_product_change:
                        if (comments.get(position).id.equals("0")){
                            comments.get(position).id="1";
                        }else {
                            comments.get(position).id="0";
                        }
                        mAdapter.notifyItemChanged(position,comments.get(position));
                        break;
                    case R.id.item_product_del:
                        mAdapter.notifyItemRemoved(position);
                        break;
                    case R.id.item_product_deit:
//                        mAdapter.notifyItemRemoved(position);
                        break;
                    case R.id.item_product_xia:
                        mAdapter.notifyItemRemoved(position);
                        break;
                }

            }
        });
        recyclerView.setAdapter(mAdapter);
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
}
