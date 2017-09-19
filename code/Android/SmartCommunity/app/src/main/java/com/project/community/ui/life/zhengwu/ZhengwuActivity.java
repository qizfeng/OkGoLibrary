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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.request.BaseRequest;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.CommentModel;
import com.project.community.model.MenuModel;
import com.project.community.model.ModuleModel;
import com.project.community.model.ZhengwuIndexResponse;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.ArticlePageAdapter;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.adapter.CommentsPopwinAdapter;
import com.project.community.ui.adapter.ModuleAdapter;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CommentPopWin;
import com.project.community.view.CommentPopwindow;
import com.project.community.view.HorizaontalGridView;
import com.project.community.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/13.
 */

public class ZhengwuActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.fab)
    ImageView fab;
    @Bind(R.id.gridview)
    HorizaontalGridView gridView;

    private int pageIndex = 1;//当前页码
    private View header;
    private List<ModuleModel> moduleModels = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsApdater commentsPopwinAdapter;
    private CommentPopwindow popupWindow;
    private List<MenuModel> mMenuData = new ArrayList<>();
    private List<ArticleModel> mArticleDate = new ArrayList<>();
    private ZhengwuIndexResponse mResponseData = new ZhengwuIndexResponse();
    private ArticlePageAdapter mAdapter;
    private Dialog mDialog;
    private String recStr = "";//回复评论
    private String targetId;//回復人id
    private int type = 0;
    private int commentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhengwu);
        initView();
        initData();
    }

    protected void initView() {
        ButterKnife.bind(this);
        header = LayoutInflater.from(ZhengwuActivity.this).inflate(R.layout.layout_header_zhengwu, null);
    }

    protected void initData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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
                //position = position - 1;//去掉头部
                Intent intent = new Intent(ZhengwuActivity.this, TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("artId", mAdapter.getItem(position).id);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }


            @Override
            public void onCommentClick(View view, int position) {//点击评论
                //  popAwindow(view);
                if (isLogin(ZhengwuActivity.this)) {
                    commentPosition = position;
                    getComments(mAdapter.getItem(position).id, view, commentPosition);
                } else
                    showToast(getString(R.string.toast_no_login));

            }
        }, new DiggClickListener() {
            @Override
            public void onDiggClick(ImageView imageView, TextView textView, int position) {
                if (isLogin(ZhengwuActivity.this)) {
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

        SpacesItemDecoration decoration = new SpacesItemDecoration(20, false);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, recyclerView);


        //头部测试数据
//        mAdapter.addHeaderView(header);
        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
        fab.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        setData();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent();
        String typeStr = moduleModels.get(position).title;
        if ("问卷".equals(typeStr)) {
            if (isLogin(this)) {
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConstants.URL_WENJUAN_LIST + "?uid=" + getUser(this).id);
                intent.putExtra("bundle", bundle);
                intent.putExtra("hideNavigation", true);
                intent.setClass(ZhengwuActivity.this, WenjuanActivity.class);
                startActivity(intent);
            } else {
                showToast(getString(R.string.toast_no_login));
            }
        } else if ("热线".equals(typeStr)) {
            intent.putExtra("hasHeader", false);
            intent.putExtra("type", "1");
            intent.setClass(ZhengwuActivity.this, PhoneDialogActivity.class);
            startActivity(intent);
        } else if ("公告".equals(typeStr) || "宣传".equals(typeStr) || "就业".equals(typeStr)) {
            setRefreshing(true);
            if ("公告".equals(typeStr)) {
                pageIndex = 1;
                type = AppConstants.ZHENGWU_GONGGAO_TYPE;
                getTypeTopic(type + "");
            } else if ("宣传".equals(typeStr)) {
                pageIndex = 1;
                type = AppConstants.ZHENGWU_XUANCHUAN_TYPE;
                getTypeTopic(type + "");
            }
        } else if ("意见".equals(typeStr)) {
            if (isLogin(this)) {
                intent.setClass(ZhengwuActivity.this, SuggestionActivity.class);
                startActivity(intent);
            } else {
                showToast(getString(R.string.toast_no_login));
            }

        } else if ("指南".equals(typeStr)) {
            intent.setClass(ZhengwuActivity.this, CompanionActivity.class);
            startActivity(intent);
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
        if (type == 0)
            loadData();
        else
            getTypeTopic(type + "");
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

    /**
     * 加载数据
     */
    private void loadData() {
        String userId;
        if (isLogin(this))
            userId = getUser(this).id;
        else
            userId = "";
        serverDao.getZhengwuIndexData(userId, pageIndex, AppConstants.PAGE_SIZE, new JsonCallback<BaseResponse<ZhengwuIndexResponse>>() {
            @Override
            public void onSuccess(BaseResponse<ZhengwuIndexResponse> baseResponse, Call call, Response response) {
                mResponseData = baseResponse.retData;
                mMenuData = mResponseData.menu;
                if (pageIndex == 1) {
                    mArticleDate = new ArrayList<>();
                    mArticleDate.addAll(mResponseData.artList);
                    mAdapter.setNewData(mArticleDate);
                    mAdapter.setEnableLoadMore(true);
                } else {
                    mAdapter.addData(mResponseData.artList);
                    mAdapter.loadMoreComplete();
                }
                if (mResponseData.artList.size() < AppConstants.PAGE_SIZE) {
                    //显示没有更多数据
//                    mAdapter.loadMoreComplete();
//                    mAdapter.setEnableLoadMore(false);
                    mAdapter.loadMoreEnd();         //加载完成
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                    showToast(e.getMessage());
            }

            @Override
            public void onCacheSuccess(BaseResponse<ZhengwuIndexResponse> baseResponse, Call call) {
                //super.onCacheSuccess(baseResponse, call);
                //一般来说,只需要第一次初始化界面的时候需要使用缓存刷新界面,以后不需要,所以用一个变量标识
            }

            @Override
            public void onAfter(@Nullable BaseResponse<ZhengwuIndexResponse> baseResponse, @Nullable Exception e) {
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

    /**
     * 设置数据
     */
    private void setData() {
        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title = "公告";
        moduleModel1.res = R.mipmap.d2_icon1;
        moduleModel1.hasRedPoint = true;
        moduleModels.add(moduleModel1);

        ModuleModel moduleModel2 = new ModuleModel();
        moduleModel2.title = "宣传";
        moduleModel2.res = R.mipmap.d2_icon2;
        moduleModel2.hasRedPoint = false;
        moduleModels.add(moduleModel2);

        ModuleModel moduleModel3 = new ModuleModel();
        moduleModel3.title = "问卷";
        moduleModel3.res = R.mipmap.d2_icon3;
        moduleModel3.hasRedPoint = false;
        moduleModels.add(moduleModel3);

//        ModuleModel moduleModel4 = new ModuleModel();
//        moduleModel4.title = "就业";
//        moduleModel4.res = R.mipmap.d2_icon4;
//        moduleModel4.hasRedPoint = false;
//        moduleModels.add(moduleModel4);

        ModuleModel moduleModel5 = new ModuleModel();
        moduleModel5.title = "意见";
        moduleModel5.res = R.mipmap.d2_icon5;
        moduleModel5.hasRedPoint = false;
        moduleModels.add(moduleModel5);

        ModuleModel moduleModel6 = new ModuleModel();
        moduleModel6.title = "指南";
        moduleModel6.res = R.mipmap.d2_icon6;
        moduleModel6.hasRedPoint = false;
        moduleModels.add(moduleModel6);

        ModuleModel moduleModel7 = new ModuleModel();
        moduleModel7.title = "热线";
        moduleModel7.res = R.mipmap.d2_icon7;
        moduleModel7.hasRedPoint = false;
        moduleModels.add(moduleModel7);

        setGridView();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int length = ScreenUtils.getScreenWidth(this) / 4;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int itemWidth = (int) (length * density);
        itemWidth = length;
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        ModuleAdapter adapter = new ModuleAdapter(ZhengwuActivity.this,
                moduleModels);
//        MenuModuleAdapter adapter = new MenuModuleAdapter(ZhengwuActivity.this,mMenuData);
        int defaultRows = 2;
        int defaultColumns = 4;
        int count = adapter.getCount();
        gridView.setAdapter(adapter);
        int columns = 0;
        if (count >= 8) {
            //当count大于8时  如下排列
            //| 1 | 3 | 5 | 7 |
            //| 2 | 4 | 6 | 8 |
            columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        } else {
            //当count小于于8时  如下排列
            //| 1 | 2 | 3 | 4 |
            //| 5 | 6 | 7 | 8 |
            columns = (count % defaultRows == 0) ? count / defaultRows : count / defaultRows + 1;
            if (columns < defaultColumns) {
                columns = defaultColumns;
            }
        }
        int pages = count % 8 == 0 ? count / 8 : count / 8 + 1;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                pages * dm.widthPixels, LinearLayout.LayoutParams.MATCH_PARENT);
        int columnWidth = itemWidth;
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(columnWidth);
        // gridView.setHorizontalSpacing(hSpacing);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(columns);
    }

    /**
     * 获取分类文章
     *
     * @param type
     */
    private void getTypeTopic(String type) {
        String userId;
        if (isLogin(this))
            userId = getUser(this).id;
        else
            userId = "";
        serverDao.getTypeTopic(userId, pageIndex, AppConstants.PAGE_SIZE, type, new JsonCallback<BaseResponse<List<ArticleModel>>>() {
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
     * 获取评论列表
     *
     * @param artId
     * @param parent
     */
    private void getComments(final String artId, final View parent, final int position) {
        serverDao.getComments(artId, new DialogCallback<BaseResponse<List<CommentModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<CommentModel>> baseResponse, Call call, Response response) {
                comments = new ArrayList<>();
                comments = baseResponse.retData;
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
                    popupWindow = new CommentPopwindow(ZhengwuActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.et_comment.setText("");
                            popupWindow.dismiss();
                        }
                    });
                popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(ZhengwuActivity.this) * 0.8);
                popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
                commentsPopwinAdapter.bindToRecyclerView(popupWindow.lv_container);
                if (comments.size() > 0)
                    popupWindow.lv_container.smoothScrollToPosition(0);
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(ZhengwuActivity.this), 0);
                commentsPopwinAdapter.setNewData(comments);
                if (comments.size() == 0) {
                    commentsPopwinAdapter.setNewData(null);
                    commentsPopwinAdapter.setEmptyView(R.layout.empty_view);
                    TextView textView = (TextView) commentsPopwinAdapter.getEmptyView().findViewById(R.id.tv_tips);
                    textView.setText(getString(R.string.empty_no_comment));
                }
                //发评论事件
                popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isLogin(ZhengwuActivity.this)) {
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
                commentsPopwinAdapter.remove(position);
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
}
