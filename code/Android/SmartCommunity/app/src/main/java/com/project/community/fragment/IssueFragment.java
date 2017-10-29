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
import com.project.community.bean.ArticleIndexBean;
import com.project.community.constants.AppConstants;
import com.project.community.ui.adapter.IssueForumAdapter;
import com.project.community.ui.me.MyForumDetailsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

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
    private List<ArticleIndexBean> mData = new ArrayList<>();

    private int page = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

        EventBus.getDefault().register(this);
        steAdapter();
    }


    /**
     * 有添加地址刷新页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ArticleIndexBean event) {

        for (int i = 0; i < myForumAdapter.getData().size(); i++) {
            if (myForumAdapter.getData().get(i).getId().equals(event.getId()))
                myForumAdapter.remove(i);
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void steAdapter() {

        myForumAdapter = new IssueForumAdapter(R.layout.item_issue_forum, mData);
        rvGovernment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGovernment.setAdapter(myForumAdapter);
        myForumAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getData();

                    }

                }, 1000);
            }
        }, rvGovernment);


        myForumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    startActivity(new Intent(getActivity(), MyForumDetailsActivity.class));

                MyForumDetailsActivity.startActivity(getActivity(), myForumAdapter.getData().get(position));

            }
        });


        swipeRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        mData.clear();
                        myForumAdapter.notifyDataSetChanged();

                        getData();
                    }
                }, 1000);
            }
        });
        swipeRl.setRefreshing(true);

        getData();
    }


    /**
     * 获取数据
     */
    private void getData() {

        serverDao.getArticleIndex(getUser(getActivity()).id, String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<List<ArticleIndexBean>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<ArticleIndexBean>> objectBaseResponse, Call call, Response response) {
                        swipeRl.setRefreshing(false);

                        mData.addAll(objectBaseResponse.retData);
                        if (page == 1) {
                            myForumAdapter.setNewData(mData);
                            myForumAdapter.setEnableLoadMore(true);
                        } else {
                            myForumAdapter.addData(objectBaseResponse.retData);
                            myForumAdapter.loadMoreComplete();
                        }

                        if (objectBaseResponse.retData.size() < AppConstants.PAGE_SIZE)
                            myForumAdapter.loadMoreEnd();


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        if (swipeRl != null)
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

