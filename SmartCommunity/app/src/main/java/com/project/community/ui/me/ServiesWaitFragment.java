package com.project.community.ui.me;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.bean.RepairListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.ui.adapter.ServiesWaitApdater;
import com.project.community.util.ToastUtil;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * 带服务
 */
public class ServiesWaitFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    ServiesWaitApdater mAdapter;
    List<RepairListBean> mData = new ArrayList<>();
    private int page = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servies_wait, container, false);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }
        });
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mAdapter = new ServiesWaitApdater(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                startActivity(intent);
//                ToastUtils.showLongToast(getActivity(),position);
            }

            @Override
            public void onCustomClick(View view, int position) {

//                ToastUtils.showLongToast(getActivity(),"删除"+position);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        });

        recyclerView.setAdapter(mAdapter);

        getData();
    }




    /**
     * 获取数据
     */
    private void getData() {
        serverDao.repairList(getUser(getActivity()).id, "0", String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<List<RepairListBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<RepairListBean>> listBaseResponse, Call call, Response response) {

                        refreshLayout.setRefreshing(false);


                        if (listBaseResponse.errNum.equals("0")) {
                            if (page == 1) {
                                mData.addAll(listBaseResponse.retData);
                                mAdapter.setNewData(mData);
                                mAdapter.setEnableLoadMore(true);
                            } else {
                                mData.addAll(listBaseResponse.retData);
                                mAdapter.addData(listBaseResponse.retData);
                                mAdapter.loadMoreComplete();
                            }

                            if (listBaseResponse.retData.size() < AppConstants.PAGE_SIZE)
                                mAdapter.loadMoreEnd();

                        } else {
                            mAdapter.loadMoreEnd();
                            ToastUtil.showToast(getActivity(), listBaseResponse.message + "");

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage() + "");
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                    }
                });

    }

}
