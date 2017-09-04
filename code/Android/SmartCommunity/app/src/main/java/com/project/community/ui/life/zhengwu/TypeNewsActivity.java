package com.project.community.ui.life.zhengwu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.request.BaseRequest;
import com.library.okgo.utils.DateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.model.NewsModel;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.CommentsPopwinAdapter;
import com.project.community.ui.adapter.NewsPageAdapter;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CommentPopWin;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/8/2.
 * 根据传递类型显示新闻/帖子
 */

public class TypeNewsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.fab)
    ImageView fab;
    private int page = 1;//当前页码
    private NewsPageAdapter mAdapter;
    private String type;
    private String title;
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsPopwinAdapter commentsPopwinAdapter;
    private CommentPopWin popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_type);
        initData();
    }

    protected void initData() {
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        fab.setOnClickListener(this);
        initToolBar(mToolBar, mTvTitle, true, title, R.mipmap.iv_back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new NewsPageAdapter(null, new IndexAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
                String url = mAdapter.getData().get(position).url;
                Intent intent = new Intent(TypeNewsActivity.this, TopicDetailActivity.class);
                if (!TextUtils.isEmpty(url)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    bundle.putString("title",title);
                    intent.putExtra("bundle", bundle);
                }
                startActivity(intent);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }


            @Override
            public void onCommentClick(View view, int position) {//点击评论
                popAwindow(view,position);
            }
        }, new DiggClickListener() {
            @Override
            public void onDiggClick(ImageView imageView, TextView textView, int position) {
                mAdapter.getData().get(position).zanNum = mAdapter.getData().get(position).zanNum + 1;
                textView.setText(mAdapter.getData().get(position).zanNum + "");
                imageView.setImageResource(R.mipmap.c1_icon9_p);
            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(20,false);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, recyclerView);


        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        loadData();
    }

    /**
     * 下拉刷新重新加载
     */
    @Override
    public void onRefresh() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        loadData();
    }


    private void loadData() {
        serverDao.getNewsList(type, new JsonCallback<BaseResponse<List<NewsModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<NewsModel>> newsResponseBaseResponse, Call call, Response response) {
                    if (page == 1) {
                        List<NewsModel> data = new ArrayList<>();
                        data.addAll(newsResponseBaseResponse.newslist);
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).zanNum = i;
                        }
                        mAdapter.setNewData(data);
                        mAdapter.setEnableLoadMore(true);
                    } else {
                        //显示没有更多数据
                        if (page == 5) {
//                            mAdapter.loadMoreComplete();
//                            mAdapter.setEnableLoadMore(false);
                            mAdapter.loadMoreEnd();         //加载完成
                        } else {
                            List<NewsModel> data = new ArrayList<>();
                            data.addAll(newsResponseBaseResponse.newslist);
                            mAdapter.addData(data);
                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).zanNum = i;
                            }
                            mAdapter.loadMoreComplete();
                        }

                    }
            }

            @Override
            public void onCacheSuccess(BaseResponse<List<NewsModel>> baseResponse, Call call) {
                //super.onCacheSuccess(baseResponse, call);
                //一般来说,只需要第一次初始化界面的时候需要使用缓存刷新界面,以后不需要,所以用一个变量标识
            }

            @Override
            public void onAfter(@Nullable BaseResponse<List<NewsModel>> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                //可能需要移除之前添加的布局
                mAdapter.removeAllFooterView();
                //结束刷新动画
                setRefreshing(false);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                if (mAdapter != null)
                    mAdapter.setEnableLoadMore(true);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }

        });

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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_favorite).setIcon(R.mipmap.d2_sousuo);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 弹出评论列表
     *
     * @param parent
     */
    private void popAwindow(View parent, int position) {
        comments = new ArrayList<>();
        CommentModel comment1 = new CommentModel();
        comment1.userId = "張三";
        comment1.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment1.content = "張三:這個文章不錯喲";
        comment1.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484183249877077333.jpg";
        CommentModel comment2 = new CommentModel();
        comment2.userId = "李三";
        comment2.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment2.content = "李四 回复 张三:多谢支持";
        comment2.photo = "https://d-image.i4.cn/i4web/image//upload/20170111/1484114886498013658.jpg";
        CommentModel comment3 = new CommentModel();
        comment3.userId = "王五";
        comment3.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
        comment3.content = "王五:呵呵";
        comment3.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484185403611050214.jpg";
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        commentsPopwinAdapter = new CommentsPopwinAdapter(this, comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                popupWindow.et_comment.setText("回复 " + comments.get(position).userId + ":");
                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        popupWindow = new CommentPopWin(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        if (comments.size() > 5) {//超过5条评论,指定listView高度
            popupWindow.lv_container.getLayoutParams().height = ScreenUtils.getScreenHeight(this) / 2 + 100;
        }
        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
//        popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
//        popupWindow.lv_container.setSelection(comments.size() - 1);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
        popupWindow.btn_send.setOnClickListener(this);
    }
}
