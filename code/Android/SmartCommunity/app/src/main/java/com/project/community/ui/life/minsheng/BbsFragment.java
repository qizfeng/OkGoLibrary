package com.project.community.ui.life.minsheng;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.Event.BbsCollectEvent;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.bean.BbsBean;
import com.project.community.bean.CommentsListBean;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.ui.adapter.BbsApdater;
import com.project.community.ui.adapter.BbsCommentsApdater;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.util.ScreenUtils;
import com.project.community.util.ToastUtil;
import com.project.community.view.CommentPopwindow;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;

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
 * A simple {@link Fragment} subclass.
 */
public class BbsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    BbsApdater bbsApdater;

    //    List<CommentModel> data = new ArrayList<>();
    private List<CommentsListBean.CommentsBean> comments = new ArrayList<>();//评论列表
    private BbsCommentsApdater commentsPopwinAdapter;
    private CommentPopwindow popupWindow;
    private int commentPage = 1;

    private int page = 1;

    private String id;

    private List<BbsBean.ArtListBean> mData = new ArrayList<>();

    public static BbsFragment newInstance(String id) {
        final BbsFragment f = new BbsFragment();
        final Bundle args = new Bundle();
        args.putString("ncid", id);
        f.setArguments(args);
        return f;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {


        EventBus.getDefault().register(this);


        setAdapters();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);


        id = getArguments().getString("ncid");


        setRefreshing(true);

        getBbs();


    }

    private void setAdapters() {

        bbsApdater = new BbsApdater(mData, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.bbs_item_like_comment:
                        popAwindow(view, position);
                        break;
                    case R.id.bbs_item_like:
//                        if (data.get(position).id.equals("10")) data.get(position).id = "0";
//                        else data.get(position).id = "10";
//                        bbsApdater.notifyItemChanged(position, data.get(position));
                        break;
                }
            }
        });

        bbsApdater.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                getBbs();

            }
        });


        recyclerView.setAdapter(bbsApdater);


    }


    /**
     * 有添加地址刷新页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BbsCollectEvent event) {

        collect(event.getId());
    }


    /**
     * 添加收藏或者取消收藏
     *
     * @param id
     */
    private void collect(final String id) {
        progressDialog.show();
        serverDao.collectBbs(getUser(getActivity()).id, id, new JsonCallback<BaseResponse<Object>>() {
            @Override
            public void onSuccess(BaseResponse<Object> objectBaseResponse, Call call, Response response) {
                progressDialog.dismiss();
                ToastUtil.showToast(getActivity(), objectBaseResponse.message);
                if (objectBaseResponse.errNum.equals("0")) {

                    for (int i = 0; i < bbsApdater.getData().size(); i++) {

                        if (bbsApdater.getData().get(i).getId().equals(id)) {
                            if (bbsApdater.getData().get(i).getStatus() == 0) {
                                bbsApdater.getData().get(i).setStatus(999);
                                bbsApdater.getData().get(i).setCollections(bbsApdater.getData().get(i).getCollections() + 1);
                                bbsApdater.notifyDataSetChanged();
                            } else {

                                bbsApdater.getData().get(i).setStatus(0);
                                bbsApdater.getData().get(i).setCollections(bbsApdater.getData().get(i).getCollections() - 1);

                                bbsApdater.notifyDataSetChanged();
                            }
                        }


                    }


                } else {

                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
                Log.e("tag_f", e.getMessage().toString() + "");
            }
        });


    }


    /***
     * 获取数据
     */
    private void getBbs() {
        serverDao.getBbs(getUser(getActivity()).id, String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE), id.equals("全部") ? "" : id, "",
                new JsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(BaseResponse<Object> objectBaseResponse, Call call, Response response) {
                        setRefreshing(false);
                        String jsonObject = new Gson().toJson(objectBaseResponse.retData);

//                        Log.e("tag_f",objectBaseResponse.retData.toString()+"");
                        if (id.equals("全部")) {

                            BbsBean bbsBean = new Gson().fromJson(jsonObject, BbsBean.class);

                            mData.addAll(bbsBean.getArtList());
                            if (page == 1) {
                                bbsApdater.setNewData(mData);
                                bbsApdater.setEnableLoadMore(true);
                            } else {
                                bbsApdater.addData(bbsBean.getArtList());
                                bbsApdater.loadMoreComplete();
                            }

                            if (bbsBean.getArtList().size() < AppConstants.PAGE_SIZE)
                                bbsApdater.loadMoreEnd();
                        } else {

                            List<BbsBean.ArtListBean> artListBean = new Gson().
                                    fromJson(jsonObject, new TypeToken<List<BbsBean.ArtListBean>>() {
                                    }.getType());

                            mData.addAll(artListBean);
                            if (page == 1) {
                                bbsApdater.setNewData(mData);
                                bbsApdater.setEnableLoadMore(true);
                            } else {
                                bbsApdater.addData(artListBean);
                                bbsApdater.loadMoreComplete();
                            }
                            if (artListBean.size() < AppConstants.PAGE_SIZE)
                                bbsApdater.loadMoreEnd();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                    }
                });


    }


    @Override
    public void onRefresh() {
        setRefreshing(true);
        page = 1;
        mData.clear();
        bbsApdater.notifyDataSetChanged();
        getBbs();
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

    /**
     * 弹出评论列表
     *
     * @param parent
     */
    private void popAwindow(View parent, final int position) {
        commentPage = 1;

        comments.clear();

        commentsPopwinAdapter = new BbsCommentsApdater(comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                popupWindow.et_comment.setText(getString(R.string.txt_receive) + comments.get(position).userName + ":");
//                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        popupWindow = new CommentPopwindow(getActivity(), null);
        popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(getActivity()) * 0.8);
        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
        commentsPopwinAdapter.bindToRecyclerView(popupWindow.lv_container);
//        if (comments.size() > 0)
//            popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
//        if (comments.size() == 0) {
//            commentsPopwinAdapter.setNewData(null);
//            commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
//            TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
//            textView.setText(getString(R.string.empty_no_comment));
//        }


        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });


        //设置评论总数
        commentsPopwinAdapter.setTotalComments(bbsApdater.getData().get(position).getComments());


        //加载更多
        commentsPopwinAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                commentPage++;
                getComment(position);
            }
        });



        getComment(position);


//        comments = new ArrayList<>();
//        CommentModel comment1 = new CommentModel();
//        comment1.userId = "張三";
//        comment1.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
//        comment1.content = "張三:這個文章不錯喲";
//        comment1.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484183249877077333.jpg";
//        CommentModel comment2 = new CommentModel();
//        comment2.userId = "李三";
//        comment2.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
//        comment2.content = "李四 回复 张三:多谢支持";
//        comment2.photo = "https://d-image.i4.cn/i4web/image//upload/20170111/1484114886498013658.jpg";
//        CommentModel comment3 = new CommentModel();
//        comment3.userId = "王五";
//        comment3.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
//        comment3.content = "王五:呵呵";
//        comment3.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484185403611050214.jpg";
//        comments.add(comment1);
//        comments.add(comment2);
//        comments.add(comment3);


    }


    /**
     * 获取评论
     */
    private void getComment(int position) {
        progressDialog.show();
        serverDao.getCommentList(bbsApdater.getData().get(position).getId(), String.valueOf(page),
                String.valueOf(AppConstants.PAGE_SIZE),
                new JsonCallback<BaseResponse<CommentsListBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<CommentsListBean> commentsListBeanBaseResponse, Call call, Response response) {
                        progressDialog.dismiss();


                        Log.e("tag_f" ,commentsListBeanBaseResponse.toString()+"");

                        comments.addAll(commentsListBeanBaseResponse.retData.getComments());
                        if (page == 1) {
                            commentsPopwinAdapter.setNewData(comments);
                            commentsPopwinAdapter.setEnableLoadMore(true);
                        } else {
                            commentsPopwinAdapter.addData(commentsListBeanBaseResponse.retData.getComments());
                            commentsPopwinAdapter.loadMoreComplete();
                        }

                        if (commentsListBeanBaseResponse.retData.getComments().size() < AppConstants.PAGE_SIZE)
                            commentsPopwinAdapter.loadMoreEnd();

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("tag_f", e.getMessage().toString() + "");
                        progressDialog.dismiss();
                    }
                }
        );

        if (comments.size() == 0) {
            commentsPopwinAdapter.setNewData(null);
            commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
            TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
            textView.setText(getString(R.string.empty_no_comment));
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }


}
