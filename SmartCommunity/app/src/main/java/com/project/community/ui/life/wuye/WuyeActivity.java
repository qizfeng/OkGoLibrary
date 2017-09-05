package com.project.community.ui.life.wuye;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.DialogCallback;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.DateUtil;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.constants.AppConstants;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.ArticleModel;
import com.project.community.model.BannerResponse;
import com.project.community.model.CommentModel;
import com.project.community.model.MenuModel;
import com.project.community.model.ModuleModel;
import com.project.community.model.NewsModel;
import com.project.community.model.WuyeIndexResponse;
import com.project.community.model.ZhengwuIndexResponse;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.ArticlePageAdapter;
import com.project.community.ui.adapter.CommentsPopwinAdapter;
import com.project.community.ui.adapter.ModuleAdapter;
import com.project.community.ui.adapter.NewsPageAdapter;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.ui.life.family.FamilyInfoActivity;
import com.project.community.ui.life.zhengwu.CompanionActivity;
import com.project.community.ui.life.zhengwu.SuggestionActivity;
import com.project.community.ui.life.zhengwu.TypeNewsActivity;
import com.project.community.ui.life.zhengwu.WenjuanActivity;
import com.project.community.ui.life.zhengwu.ZhengwuActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.util.TablayoutLineReflex;
import com.project.community.view.CommentPopWin;
import com.project.community.view.HorizaontalGridView;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;
import com.ryane.banner_lib.AdPageInfo;
import com.ryane.banner_lib.AdPlayBanner;
import com.ryane.banner_lib.transformer.RotateDownTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/13.
 * 物业首页
 */

public class WuyeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.fab)
    ImageView fab;
    AdPlayBanner adPlayBanner;
    private CommentPopWin popupWindow;
    private List<AdPageInfo> mDatas = new ArrayList<>();//模拟轮播图数据
    private ArticlePageAdapter mAdapter;
    private View header;
    private TabLayout tabLayout;
    private HorizaontalGridView gridView;
    private int type = 7;
    private int pageIndex = 1;
    private List<ModuleModel> moduleModels = new ArrayList<>();
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsPopwinAdapter commentsPopwinAdapter;
    private List<ArticleModel> mArticleDate = new ArrayList<>();
    private WuyeIndexResponse mResponseData = new WuyeIndexResponse();
    private List<MenuModel> mMenuData = new ArrayList<>();
    private Dialog mDialog;
    private String recStr = "";//回复评论
    private String targetId;//回復人id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuye);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_wuye, null);
        tabLayout = (TabLayout) header.findViewById(R.id.tabLayout);
        gridView = (HorizaontalGridView) header.findViewById(R.id.gridview);
        adPlayBanner = (AdPlayBanner) header.findViewById(R.id.adPlayBanner);
        adPlayBanner.getLayoutParams().height = ScreenUtils.getScreenWidth(this) * 1 / 2;
        adPlayBanner.getLayoutParams().width = ScreenUtils.getScreenWidth(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        mAdapter = new ArticlePageAdapter(null, new IndexAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
                position = position - 1;//去掉头部
                Bundle bundle = new Bundle();
                if (tabLayout.getSelectedTabPosition() == 1) {
                    bundle.putString("title", getString(R.string.title_communication_notice));
                } else if (tabLayout.getSelectedTabPosition() == 0) {
                    bundle.putString("title", getString(R.string.tab_title_wuye_kuaixun));
                }
                bundle.putString("artId", mAdapter.getItem(position).id);
                TopicDetailActivity.startActivity(WuyeActivity.this, bundle);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }

            @Override
            public void onCommentClick(View view, int position) {//点击评论
//                popAwindow(view, position);
                position = position - 1;//去掉头部
                if (isLogin(WuyeActivity.this))
                    getComments(mAdapter.getItem(position).id, view);
                else
                    showToast(getString(R.string.toast_no_login));
            }
        }, new DiggClickListener() {
            @Override
            public void onDiggClick(ImageView imageView, TextView textView, int position) {
                position = position - 1;//去掉头部
                if (isLogin(WuyeActivity.this))
                    onCollect(textView, imageView, position);
                else
                    showToast(getString(R.string.toast_no_login));
            }
        });


        /**
         * 监听 AppBarLayout Offset 变化，动态设置 SwipeRefreshLayout 是否可用
         */

        SpacesItemDecoration decoration = new SpacesItemDecoration(20, true);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, recyclerView);

        //选项卡
        initTabLayout();

        //轮播图
        initBanner();

        //中部模块
        setGridData();
        setGridView();

        //头部测试数据
        mAdapter.addHeaderView(header);
        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
        fab.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    private void initBanner() {
//
        mDatas = new ArrayList<>();
        getBannerData();
    }

    private void getBannerData() {
        serverDao.getBannerData("1", "1", new JsonCallback<BaseResponse<BannerResponse>>() {
            @Override
            public void onSuccess(BaseResponse<BannerResponse> baseResponse, Call call, Response response) {
                mDatas = baseResponse.retData.imageList;
                try {
                    //开始轮播
                    adPlayBanner
                            .setImageLoadType(AdPlayBanner.ImageLoaderType.GLIDE)
                            .setAutoPlay(true)
                            .setIndicatorType(AdPlayBanner.IndicatorType.POINT_INDICATOR)
//                    .setNumberViewColor(0xffffffff, 0xccea0000, 0x103375)
                            .setInterval(5000)
                            .setOnPageClickListener(new AdPlayBanner.OnPageClickListener() {
                                @Override
                                public void onPageClick(AdPageInfo info, int postion) {
                                    String url = info.getImageUrl();
                                    Intent intent = new Intent(WuyeActivity.this, WebViewActivity.class);
                                    if (!TextUtils.isEmpty(url)) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("url", url);
                                        intent.putExtra("bundle", bundle);
                                    }
                                    startActivity(intent);
                                }
                            })
//                .setPageTransfromer(new FadeInFadeOutTransformer())//淡入淡出
//                    .setPageTransfromer(new RotateDownTransformer())//旋转效果
//                .setPageTransfromer(new ZoomOutPageTransformer())//空间切换
                            .setInfoList((ArrayList<AdPageInfo>) mDatas)
                            .setUp();
                } catch (Exception e) {
                    e.toString();
                }
            }
        });
    }

    /**
     * 初始化tabLayout
     */
    private void initTabLayout() {
        //  tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_title_wuye_all));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineReflex.setIndicator(tabLayout, 50, 50);
            }
        });
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_wuye_kuaixun)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_wuye_gonggao)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0)//全部
//                    type = "health";
//                else
                if (tab.getPosition() == 0)//快讯
                    type = AppConstants.WUYE_KUAIXUN;
                else if (tab.getPosition() == 1)//公告
                    type = AppConstants.WUYE_GONGGAO;
                setRefreshing(true);
                pageIndex = 1;
                loadData(type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        String type = moduleModels.get(position).title;
        if ("客服".equals(type)) {
            intent.setClass(this, PhoneDialogActivity.class);
            startActivity(intent);
        } else if (getString(R.string.activity_payment).equals(type)) {
            intent.setClass(this, PayIndexActivity.class);
            startActivity(intent);
        } else if (getString(R.string.activity_family_info).equals(type)) {
            FamilyInfoActivity.startActivity(this, null);
        }
    }

    private void loadData(int type) {
        String userId;
        if (isLogin(this))
            userId = getUser(this).id;
        else
            userId = "";
        serverDao.getWuyeIndexData(userId, pageIndex, AppConstants.PAGE_SIZE,type, new JsonCallback<BaseResponse<WuyeIndexResponse>>() {
            @Override
            public void onSuccess(BaseResponse<WuyeIndexResponse> baseResponse, Call call, Response response) {
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
            public void onAfter(@Nullable BaseResponse<WuyeIndexResponse> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                //可能需要移除之前添加的布局
                mAdapter.removeAllFooterView();
                //结束刷新动画
                setRefreshing(false);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());

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

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        adPlayBanner
                .setImageLoadType(AdPlayBanner.ImageLoaderType.GLIDE)
                .setAutoPlay(true)
                .setIndicatorType(AdPlayBanner.IndicatorType.POINT_INDICATOR)
                .setNumberViewColor(0xcc00A600, 0xccea0000, 0xffffffff)
                .setInterval(5000)
                .setOnPageClickListener(new AdPlayBanner.OnPageClickListener() {
                    @Override
                    public void onPageClick(AdPageInfo info, int postion) {

                    }
                })
//                .setPageTransfromer(new FadeInFadeOutTransformer())//淡入淡出
//                .setPageTransfromer(new RotateDownTransformer())//旋转效果
//                .setPageTransfromer(new ZoomOutPageTransformer())//空间切换
                .setInfoList((ArrayList<AdPageInfo>) mDatas)
                .setUp();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadData(type);
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        loadData(type);
    }


    /**
     * 设置数据
     */
    private void setGridData() {
        ModuleModel moduleModel2 = new ModuleModel();
        moduleModel2.title = "缴费";
        moduleModel2.res = R.mipmap.d17_icon2;
        moduleModel2.hasRedPoint = false;
        moduleModels.add(moduleModel2);

        ModuleModel moduleModel3 = new ModuleModel();
        moduleModel3.title = "报修";
        moduleModel3.res = R.mipmap.d17_icon3;
        moduleModel3.hasRedPoint = false;
        moduleModels.add(moduleModel3);

        ModuleModel moduleModel4 = new ModuleModel();
        moduleModel4.title = "家庭信息";
        moduleModel4.res = R.mipmap.d17_icon4;
        moduleModel4.hasRedPoint = false;
        moduleModels.add(moduleModel4);

        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title = "客服";
        moduleModel1.res = R.mipmap.d17_icon1;
        moduleModel1.hasRedPoint = true;
        moduleModels.add(moduleModel1);


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
        ModuleAdapter adapter = new ModuleAdapter(this,
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

//    /**
//     * 弹出评论列表
//     *
//     * @param parent
//     */
//    private void popAwindow(View parent, int position) {
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
//
//        commentsPopwinAdapter = new CommentsPopwinAdapter(this, comments, new RecycleItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                popupWindow.et_comment.setText("回复 " + comments.get(position).userId + ":");
//                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
//            }
//
//            @Override
//            public void onCustomClick(View view, int position) {
//
//            }
//        });
//        popupWindow = new CommentPopWin(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popupWindow.dismiss();
//            }
//        });
//        if (comments.size() > 5) {//超过5条评论,指定listView高度
//            popupWindow.lv_container.getLayoutParams().height = ScreenUtils.getScreenHeight(this) / 2 + 100;
//        }
//        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
////        popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
////        popupWindow.lv_container.setSelection(comments.size() - 1);
//        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(this), 0);
//        popupWindow.btn_send.setOnClickListener(this);
//    }


    /**
     * 获取评论列表
     *
     * @param artId
     * @param parent
     */
    private void getComments(final String artId, final View parent) {
        serverDao.getComments(artId, new DialogCallback<BaseResponse<List<CommentModel>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<CommentModel>> baseResponse, Call call, Response response) {
                comments = new ArrayList<>();
                comments = baseResponse.retData;
                commentsPopwinAdapter = new CommentsPopwinAdapter(WuyeActivity.this, comments, new RecycleItemClickListener() {
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
                    popupWindow = new CommentPopWin(WuyeActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                if (comments.size() > 5) {//超过5条评论,指定listView高度
                    popupWindow.lv_container.getLayoutParams().height = ScreenUtils.getScreenHeight(WuyeActivity.this) / 2 + 100;
                }
                popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(WuyeActivity.this), 0);
                //发评论事件
                popupWindow.btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                if (!e.getMessage().contains("No address"))
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
                getComments(artId, view);
                showToast(baseResponse.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }


    /**
     * 删除评论
     */
    private void deleteComment(final int position, String commentId, int type) {
        serverDao.doDeleteComment(getUser(this).id, commentId, type, new DialogCallback<BaseResponse<List>>(this) {
            @Override
            public void onSuccess(BaseResponse<List> baseResponse, Call call, Response response) {
                showToast(baseResponse.message);
                commentsPopwinAdapter.removeItem(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (!e.getMessage().contains("No address"))
                    showToast(e.getMessage());
            }
        });
    }

    /**
     * 点击收藏
     *
     * @param textView
     * @param imageView
     * @param position
     */
    private void onCollect(final TextView textView, final ImageView imageView, final int position) {
        serverDao.doCollectTopic(getUser(this).id, mAdapter.getItem(position).id, new JsonCallback<BaseResponse<List>>() {
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
                if (!e.getMessage().contains("No address"))
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
