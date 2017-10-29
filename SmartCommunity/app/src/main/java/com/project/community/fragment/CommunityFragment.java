package com.project.community.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.project.community.bean.CommunityBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.adapter.CommunityAdapter;
import com.project.community.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/23 17:00
 * em：617716355@qq.com
 */
public class CommunityFragment extends BaseFragment {
    @Bind(R.id.rv_government)
    RecyclerView rvGovernment;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;


    private CommunityAdapter communityAdapter;

    private int page = 1;

    private List<CommunityBean> mData = new ArrayList<>();


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

        communityAdapter = new CommunityAdapter(R.layout.item_community, mData);
        rvGovernment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGovernment.setAdapter(communityAdapter);
        communityAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        page++;
                        getCommunity();

                    }

                }, 1000);
            }
        }, rvGovernment);


        communityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                        page=1;
                        mData.clear();
                        communityAdapter.notifyDataSetChanged();
                        getCommunity();
                    }
                }, 1000);
            }
        });

        swipeRl.setRefreshing(true);
        getCommunity();
    }

    private void getCommunity() {

        serverDao.getCommunityList(getUser(getActivity()).id, "1", String.valueOf(page), String.valueOf(AppConstants.PAGE_SIZE), new JsonCallback<BaseResponse<List<CommunityBean>>>() {
            @Override
            public void onSuccess(BaseResponse<List<CommunityBean>> listBaseResponse, Call call, Response response) {

                swipeRl.setRefreshing(false);

                if (listBaseResponse.errNum.equals("0")) {

                    if (page == 1) {
                        mData.addAll(listBaseResponse.retData);
                        communityAdapter.setNewData(mData);
                        communityAdapter.setEnableLoadMore(true);
                    } else {
                        mData.addAll(listBaseResponse.retData);
                        communityAdapter.addData(listBaseResponse.retData);
                        communityAdapter.loadMoreComplete();
                    }

                    if (listBaseResponse.retData.size()< AppConstants.PAGE_SIZE)
                        communityAdapter.loadMoreEnd();

                } else {
                    communityAdapter.loadMoreEnd();
                    ToastUtil.showToast(getActivity(), listBaseResponse.message + "");

                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e("tag_f", e.getMessage() + "");
                swipeRl.setRefreshing(false);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
