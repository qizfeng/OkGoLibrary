package com.project.community.ui.life.zhengwu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.library.okgo.utils.KeyBoardUtils;
import com.library.okgo.utils.LogUtils;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.CommentModel;
import com.project.community.model.CommentResponse;
import com.project.community.model.MenuModel;
import com.project.community.model.ModuleModel;
import com.project.community.model.ZhengwuIndexResponse;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.ArticlePageAdapter;
import com.project.community.ui.adapter.CommentsApdater;
import com.project.community.ui.adapter.ModuleAdapter;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.NavStaggeredGridLayoutManager;
import com.project.community.util.ScreenUtils;
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
 * Created by zipingfang on 17/10/23.
 */

public class ZhengwuFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    public static final int REQUEST_CODE_COMMENT=1000;
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
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_zhengwu, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }


    protected void initView() {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_zhengwu, null);
    }

    @Override
    protected void initData() {
        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
                //position = position - 1;//去掉头部
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("artId", mAdapter.getItem(position).id);
                bundle.putInt("index", position);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent,REQUEST_CODE_COMMENT);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }


            @Override
            public void onCommentClick(View view, int position) {//点击评论
                //  popAwindow(view);
                if (isLogin(getActivity())) {
                    commentPosition = position;
                    artId = mAdapter.getItem(position).id;
                    commentView = view;
                    getComments(artId, commentView, commentPosition);
                } else
                    showToast(getString(R.string.toast_no_login));

            }
        }, new
                DiggClickListener() {
                    @Override
                    public void onDiggClick(ImageView imageView, TextView textView, int position) {
                        if (isLogin(getActivity())) {
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
        recyclerView.setPadding(20, 20, 20, 20);

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

    //退出时的时间
    private long mExitTime;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent();
        String typeStr = moduleModels.get(position).title;
        if ("问卷".equals(typeStr)) {
            if (isLogin(getActivity())) {
                Bundle bundle = new Bundle();
                bundle.putString("url", AppConstants.URL_WENJUAN_LIST + "?uid=" + getUser(getActivity()).id);
                intent.putExtra("bundle", bundle);
                intent.putExtra("hideNavigation", true);
                intent.setClass(getActivity(), WenjuanActivity.class);
                startActivity(intent);
            } else {
                showToast(getString(R.string.toast_no_login));
            }
        } else if ("热线".equals(typeStr)) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
                Intent mIntent = new Intent();
                mIntent.putExtra("hasHeader", false);
                mIntent.putExtra("type", "1");
                mIntent.setClass(getActivity(), PhoneDialogActivity.class);
                startActivity(mIntent);
            }


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
            if (isLogin(getActivity())) {
                intent.setClass(getActivity(), SuggestionActivity.class);
                startActivity(intent);
            } else {
                showToast(getString(R.string.toast_no_login));
            }

        } else if ("指南".equals(typeStr)) {
            intent.setClass(getActivity(), CompanionActivity.class);
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
        serverDao.doCollectTopic(getUser(getActivity()).id, mAdapter.getItem(position).id, new DialogCallback<BaseResponse<List>>(getActivity()) {
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
        if (isLogin(getActivity()))
            userId = getUser(getActivity()).id;
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
        moduleModels = new ArrayList<>();
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
        int length = ScreenUtils.getScreenWidth(getActivity()) / 4;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int itemWidth = (int) (length * density);
        itemWidth = length;
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        ModuleAdapter adapter = new ModuleAdapter(getActivity(),
                moduleModels);
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
        if (isLogin(getActivity()))
            userId = getUser(getActivity()).id;
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
        serverDao.getComments(artId, pageComment, AppConstants.PAGE_SIZE, new DialogCallback<BaseResponse<CommentResponse>>(getActivity()) {
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
                        popupWindow = new CommentPopwindow(getActivity(), new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                KeyBoardUtils.closeKeybord(popupWindow.et_comment, getActivity());
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
                    popupWindow.lv_container.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(getActivity()) * 0.8);
                    popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
                    popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
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
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
                //发评论事件
                popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isLogin(getActivity())) {
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
        serverDao.doComment(getUser(getActivity()).id, artId, content, targetId, new DialogCallback<BaseResponse<List>>(getActivity()) {
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
        if (!isLogin(getActivity())) {
            showToast(getString(R.string.toast_no_login));
            return;
        }
        serverDao.doDeleteComment(getUser(getActivity()).id, commentId, type, new DialogCallback<BaseResponse<List>>(getActivity()) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
//                commentsPopwinAdapter.removeItem(position);
//                commentsPopwinAdapter.remove(position);
                getComments(artId, commentView, commentPosition);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    public void showAlertDialog(final int position) {
//        mDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.activity_dialog_common);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = getActivity().getWindowManager();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK){
            if(requestCode==REQUEST_CODE_COMMENT){
                mAdapter.getData().get(data.getIntExtra("index",0)).comments=data.getIntExtra("comment",0);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

