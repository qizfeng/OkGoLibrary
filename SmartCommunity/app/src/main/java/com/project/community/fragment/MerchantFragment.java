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
import com.project.community.aipay.PayResult;
import com.project.community.base.BaseFragment;
import com.project.community.bean.ForumListBean;
import com.project.community.bean.MerchantBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.adapter.MerchantAdapter;
import com.project.community.util.ToastUtil;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * author：fangkai on 2017/10/23 16:59
 * em：617716355@qq.com
 */
public class MerchantFragment extends BaseFragment {
    @Bind(R.id.rv_government)
    RecyclerView rvGovernment;
    @Bind(R.id.swipe_rl)
    SwipeRefreshLayout swipeRl;


    private MerchantAdapter merchantAdapter;

    private  List<MerchantBean> mData=new ArrayList<>();

    private  int page=1;

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

        merchantAdapter = new MerchantAdapter(R.layout.item_merchant, mData);
        merchantAdapter.setAddress(getLocation(getActivity()));
        rvGovernment.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration decoration = new SpacesItemDecoration(1, false);
        rvGovernment.addItemDecoration(decoration);
        rvGovernment.setAdapter(merchantAdapter);
        merchantAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getMerchant();

                    }



                }, 1000);
            }
        }, rvGovernment);


        merchantAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                MerchantDetailActivity.startActivity(getActivity(),null);
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
                        merchantAdapter.notifyDataSetChanged();
                        getMerchant();
                    }
                }, 1000);
            }
        });

        swipeRl.setRefreshing(true);
        getMerchant();
    }

    private void getMerchant() {

        serverDao.getMerchantList(getUser(getActivity()).id, "3", String.valueOf(page), String.valueOf(AppConstants.PAGE_SIZE), new JsonCallback<BaseResponse<List<MerchantBean>>>() {
            @Override
            public void onSuccess(BaseResponse<List<MerchantBean>> listBaseResponse, Call call, Response response) {

                swipeRl.setRefreshing(false);

                if (listBaseResponse.errNum.equals("0")) {

                    if (page == 1) {
                        mData.addAll(listBaseResponse.retData);
                        merchantAdapter.setNewData(mData);
                        merchantAdapter.setEnableLoadMore(true);
                    } else {
                        mData.addAll(listBaseResponse.retData);
                        merchantAdapter.addData(listBaseResponse.retData);
                        merchantAdapter.loadMoreComplete();
                    }

                    if (listBaseResponse.retData.size()< AppConstants.PAGE_SIZE)
                        merchantAdapter.loadMoreEnd();

                } else {
                    merchantAdapter.loadMoreEnd();
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
