package com.project.community.ui.life.zhengwu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.request.BaseRequest;
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.CommentModel;
import com.project.community.model.CommentResponse;
import com.project.community.model.ZhengwuIndexResponse;
import com.project.community.ui.adapter.ArticlePageAdapter;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.NavStaggeredGridLayoutManager;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CommentPopwindow;
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
    public static final int REQUEST_CODE_COMMENT = 1000;
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
    private ArticlePageAdapter mAdapter;
    private ZhengwuIndexResponse mResponseData = new ZhengwuIndexResponse();
    private String type;
    private String title;
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsApdater commentsPopwinAdapter;
    private CommentPopwindow popupWindow;
    private Dialog mDialog;
    private String recStr = "";//回复评论
    private String targetId;//回復人id
    private int commentPosition = 0;
    private int pageIndex = 1;//当前页码
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
        initToolBar(mToolBar, mTvTitle, true, "就业", R.mipmap.iv_back);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final NavStaggeredGridLayoutManager layoutManager = new NavStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ArticlePageAdapter(null, new IndexAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
                Intent intent = new Intent(TypeNewsActivity.this, TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("artId", mAdapter.getItem(position).id);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent,REQUEST_CODE_COMMENT);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }


            @Override
            public void onCommentClick(View view, int position) {//点击评论
                //  popAwindow(view);
                if (isLogin(TypeNewsActivity.this)) {
                    commentPosition = position;
                    artId=mAdapter.getItem(position).id;
                    commentView=view;
                    getComments(artId,commentView, commentPosition);
                } else
                    showToast(getString(R.string.toast_no_login));

            }
        }, new
                DiggClickListener() {
                    @Override
                    public void onDiggClick(ImageView imageView, TextView textView, int position) {
                        if (isLogin(TypeNewsActivity.this)) {
                            if (mAdapter.getItem(position).categoryAllowCollection == 0 ||
                                    mAdapter.getItem(position).allowCollection == 0) {
                                showToast(getString(R.string.toast_no_collect));
                                return;
                            }
                            onCollect(textView, imageView, position);
                        } else
                            showToast(getString(R.string.toast_no_login));
                    }
                });

        SpacesItemDecoration decoration = new SpacesItemDecoration(20,false);
        recyclerView.setPadding(20,0,20,20);
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
        pageIndex++;
        loadData();
    }

    /**
     * 下拉刷新重新加载
     */
    @Override
    public void onRefresh() {
        pageIndex = 1;
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    /**
     * 点击收藏
     *
     * @param textView
     * @param imageView
     * @param position
     */

    private void onCollect(final TextView textView, final ImageView imageView, final int position) {
        serverDao.doCollectTopic(getUser(this).id, mAdapter.getItem(position).id, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                if ("收藏成功".equals(baseResponse.message)) {
                    mAdapter.getData().get(position).collections = mAdapter.getData().get(position).collections + 1;
                    textView.setText(mAdapter.getData().get(position).collections + "");
                    imageView.setImageResource(R.mipmap.c1_icon9_p);
                } else if ("取消收藏成功".equals(baseResponse.message)) {
                    mAdapter.getData().get(position).collections = mAdapter.getData().get(position).collections - 1;
                    textView.setText(mAdapter.getData().get(position).collections + "");
                    imageView.setImageResource(R.mipmap.c1_icon9);
                }
                showToast(baseResponse.message);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    private void loadData() {
        String userId;
        if (isLogin(this))
            userId = getUser(this).id;
        else
            userId = "";
        serverDao.getEmploymentData(userId, pageIndex, AppConstants.PAGE_SIZE, "9","2", new JsonCallback<BaseResponse<List<ArticleModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<ArticleModel>> baseResponse, Call call, Response response) {
                if (pageIndex == 1) {
                    mAdapter.setNewData(baseResponse.retData);
                    mAdapter.setEnableLoadMore(true);
                } else {
                    mAdapter.addData(mResponseData.artList);
                    mAdapter.loadMoreComplete();
                }
                if (baseResponse.retData.size() < AppConstants.PAGE_SIZE) {
                    //显示没有更多数据
                    mAdapter.loadMoreEnd();         //加载完成
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }

            @Override
            public void onAfter(@Nullable BaseResponse<List<ArticleModel>> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                //可能需要移除之前添加的布局
                mAdapter.removeAllFooterView();
                //结束刷新动画
                setRefreshing(false);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
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



    private int pageComment = 1;
    private String artId;
    private View commentView;
    /**
     * 获取评论列表
     *
     * @param artId
     * @param parent
     */
    private void getComments(final String artId, final View parent, final int position) {
        serverDao.getComments(artId, pageComment,AppConstants.PAGE_SIZE,new DialogCallback<BaseResponse<CommentResponse>>(this) {
            @Override
            public void onSuccess(BaseResponse<CommentResponse> baseResponse, Call call, Response response) {
                comments = new ArrayList<>();
                comments = baseResponse.retData.comments;
                if (pageComment == 1) {
                    commentsPopwinAdapter = new CommentsApdater(comments, new RecycleItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            recStr = getString(R.string.txt_receive) + commentsPopwinAdapter.getItem(position).userName + ":";
                            targetId = commentsPopwinAdapter.getItem(position).userId;
                            popupWindow.et_comment.setText(recStr);
                            popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
                        }

                        @Override
                        public void onCustomClick(View view, int position) {//自定义事件,此处做删除逻辑
                            showAlertDialog(position);
                        }
                    });
                    if (popupWindow == null)
                        popupWindow = new CommentPopwindow(TypeNewsActivity.this, new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                KeyBoardUtils.closeKeybord(popupWindow.et_comment,TypeNewsActivity.this);
                                popupWindow.dismiss();
                                popupWindow.et_comment.setText("");
                                pageComment = 1;
                                commentsPopwinAdapter = null;
                                return false;
                            }
                        });
                    commentsPopwinAdapter.setTotalComments(baseResponse.retData.total);
                    commentsPopwinAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            pageComment++;
                            getComments(artId, commentView, commentPosition);
                        }
                    }, popupWindow.lv_container);
                    popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(TypeNewsActivity.this) * 0.8);
                    popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
                    popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(TypeNewsActivity.this), 0);
                }
                if (pageComment == 1) {
                    commentsPopwinAdapter.setNewData(comments);
                    commentsPopwinAdapter.setEnableLoadMore(true);
                } else {
                    commentsPopwinAdapter.addData(comments);
                    commentsPopwinAdapter.loadMoreComplete();
                }
                if (comments.size() == 0 && pageComment == 1) {
                    commentsPopwinAdapter.setNewData(null);
                    commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_comment));
                } else if (comments.size() < AppConstants.PAGE_SIZE) {
                    commentsPopwinAdapter.loadMoreEnd();
                }
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(TypeNewsActivity.this), 0);
                //发评论事件
                popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isLogin(TypeNewsActivity.this)) {
                            showToast(getString(R.string.toast_no_login));
                            return;
                        }
                        if (mAdapter.getItem(position).categoryAllowComment == 0
                                || mAdapter.getItem(position).allowComment == 0) {
                            showToast(getString(R.string.toast_no_comment));
                            return;
                        }
                        if (TextUtils.isEmpty(recStr)) {
                            if (TextUtils.isEmpty(popupWindow.et_comment.getText().toString())) {
                                return;
                            }
                            doComment(parent, artId, popupWindow.et_comment.getText().toString(), "");
                        } else {
                            if (!popupWindow.et_comment.getText().toString().startsWith(recStr))
                                targetId = "";
                            String content = popupWindow.et_comment.getText().toString().replace(recStr, "");
                            if (TextUtils.isEmpty(content)) {
                                return;
                            }
                            doComment(parent, artId, content, targetId);
                        }
                    }
                });
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 发评论
     *
     * @param artId
     * @param content
     */
    private void doComment(final View view, final String artId, String content, final String targetId) {
        serverDao.doComment(getUser(this).id, artId, content, targetId, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                popupWindow.et_comment.setText("");
                getComments(artId, view, commentPosition);
                showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }


    /**
     * 删除评论
     */
    private void deleteComment(final int position, String commentId, int type) {
        if (!isLogin(this)) {
            showToast(getString(R.string.toast_no_login));
            return;
        }
        serverDao.doDeleteComment(getUser(this).id, commentId, type, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
//                commentsPopwinAdapter.removeItem(position);
//                commentsPopwinAdapter.remove(position);
                getComments(artId,commentView,commentPosition);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(this).create();
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        mDialog.show();
        TextView tv_content = (TextView) mDialog.findViewById(R.id.tv_content);
        tv_content.setText(R.string.txt_confirm_delete);
        Button btn_confirm = (Button) mDialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment(position, commentsPopwinAdapter.getItem(position).id, 1);
                mDialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_COMMENT) {
                LogUtils.e(">"+data.getIntExtra("index", 0)+","+data.getIntExtra("comment", 0));
                mAdapter.getData().get(data.getIntExtra("index", 0)).comments = data.getIntExtra("comment", 0);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
