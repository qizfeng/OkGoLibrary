package com.project.community.ui.index;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.request.BaseRequest;
import com.library.okgo.utils.DateUtil;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.listener.DiggClickListener;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.BannerModel;
import com.project.community.model.BannerResponse;
import com.project.community.model.CommentModel;
import com.project.community.model.ModuleModel;
import com.project.community.model.NewsModel;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.CommentsPopwinAdapter;
import com.project.community.ui.adapter.NewsPageAdapter;
import com.project.community.ui.adapter.listener.IndexAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.ui.life.wuye.PayIndexActivity;
import com.project.community.ui.life.zhengwu.ZhengwuActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ScreenUtils;
import com.project.community.view.CommentPopWin;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;
import com.ryane.banner_lib.AdPageInfo;
import com.ryane.banner_lib.AdPlayBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/11.
 */

public class IndexFragment extends BaseFragment implements GestureDetector.OnGestureListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.fab)
    ImageView fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    AdPlayBanner adPlayBanner;
    private static final int FLING_MIN_DISTANCE = 100;
    private StaggeredGridLayoutManager layoutManager;

    private View header;
    private TableLayout tableLayout;
    private CommentPopWin popupWindow;

    private List<AdPageInfo> mDatas = new ArrayList<>();//模拟轮播图数据
    private int page = 1;//当前页码
    private NewsPageAdapter mAdapter;
    private boolean isInitCache = false;//是否缓存
    private List<CommentModel> comments = new ArrayList<>();//评论列表
    private CommentsPopwinAdapter commentsPopwinAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_index, container, false);
        ButterKnife.bind(this, view);
        initToolbar(toolbar, "");
        return view;
    }


    @Override
    protected void initData() {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_index, null);
        tableLayout = (TableLayout) header.findViewById(R.id.tableLayout);
        adPlayBanner = (AdPlayBanner) header.findViewById(R.id.adPlayBanner);
        adPlayBanner.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) * 1 / 2;
        adPlayBanner.getLayoutParams().width = ScreenUtils.getScreenWidth(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        getBannerData();
        mAdapter = new NewsPageAdapter(null, new IndexAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
                position = position - 1;//去掉头部
                String url = mAdapter.getData().get(position).url;
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                if (!TextUtils.isEmpty(url)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    intent.putExtra("bundle", bundle);
                }
                startActivity(intent);
            }

            @Override
            public void onTextClick(View view, int position) {//文字部分点击事件
            }


            @Override
            public void onCommentClick(View view, int position) {//点击评论
                if (isLogin(getActivity()))
                    popAwindow(view, position);
                else
                    showToast("沒有登录,无法进行此操作");
            }
        }, new DiggClickListener() {
            @Override
            public void onDiggClick(ImageView imageView, TextView textView, int position) {
                position = position - 1;//去掉头部
                mAdapter.getData().get(position).zanNum = mAdapter.getData().get(position).zanNum + 1;
                textView.setText(mAdapter.getData().get(position).zanNum + "");
                imageView.setImageResource(R.mipmap.c1_icon9_p);

            }
        });
//        /**
//         * 监听 AppBarLayout Offset 变化，动态设置 SwipeRefreshLayout 是否可用
//         */
//        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset >= 0) {
//                    refreshLayout.setEnabled(true);
//                } else {
//                    refreshLayout.setEnabled(false);
//                }
//            }
//
//        });


        SpacesItemDecoration decoration = new SpacesItemDecoration(20, true);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, recyclerView);


        //头部测试数据
        testAddHeader();

        mAdapter.addHeaderView(header);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (TextUtils.isEmpty(popupWindow.et_comment.getText().toString())) {
                    return;
                }
                CommentModel comment = new CommentModel();
                comment.userId = "斯巴达";
                comment.createDate = DateUtil.getCustomDateStr(DateUtil.millis(), "MM-dd HH:mm");
                comment.content = popupWindow.et_comment.getText().toString();
                comment.photo = "https://d-image.i4.cn/i4web/image//upload/20170112/1484183249877077333.jpg";
                comments.add(comment);
                commentsPopwinAdapter.setData(comments);
                popupWindow.et_comment.setText("");
                popupWindow.lv_container.setSelection(comments.size() - 1);
                break;
        }
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
                            .setInterval(5000)
                            .setOnPageClickListener(new AdPlayBanner.OnPageClickListener() {
                                @Override
                                public void onPageClick(AdPageInfo info, int postion) {
                                    String url = info.getLink();
                                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                    if (!TextUtils.isEmpty(url)) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("url", url);
                                        intent.putExtra("bundle", bundle);
                                    }
                                    startActivity(intent);
                                }
                            })
                            //.setPageTransfromer(new FadeInFadeOutTransformer())//淡入淡出
                            //.setPageTransfromer(new RotateDownTransformer())//旋转效果
                            //.setPageTransfromer(new ZoomOutPageTransformer())//空间切换
                            .setInfoList((ArrayList<AdPageInfo>) mDatas)
                            .setUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

        commentsPopwinAdapter = new CommentsPopwinAdapter(getActivity(), comments, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                popupWindow.et_comment.setText("回复 " + comments.get(position).userName + ":");
                popupWindow.et_comment.setSelection(popupWindow.et_comment.getText().length());
            }

            @Override
            public void onCustomClick(View view, int position) {

            }
        });
        popupWindow = new CommentPopWin(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        if (comments.size() > 5) {//超过5条评论,指定listView高度
            popupWindow.lv_container.getLayoutParams().height = ScreenUtils.getScreenHeight(getActivity()) / 2 + 100;
        }
        popupWindow.lv_container.setAdapter(commentsPopwinAdapter);
//        popupWindow.lv_container.smoothScrollToPosition(comments.size() - 1);
//        popupWindow.lv_container.setSelection(comments.size() - 1);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, ScreenUtils.getScreenWidth(getActivity()), 0);
        popupWindow.btn_send.setOnClickListener(this);
    }

    private void testAddHeader() {
        final List<ModuleModel> moduleModels = new ArrayList<>();
        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title = "即时聊天";
        moduleModel1.res = R.mipmap.c1_icon11;
        moduleModel1.hasRedPoint = true;
        moduleModels.add(moduleModel1);

        ModuleModel moduleModel2 = new ModuleModel();
        moduleModel2.title = "报修";
        moduleModel2.res = R.mipmap.c1_icon2;
        moduleModel2.hasRedPoint = false;
        moduleModels.add(moduleModel2);

        ModuleModel moduleModel3 = new ModuleModel();
        moduleModel3.title = "缴费";
        moduleModel3.res = R.mipmap.c1_icon3;
        moduleModel3.hasRedPoint = false;
        moduleModels.add(moduleModel3);

        ModuleModel moduleModel4 = new ModuleModel();
        moduleModel4.title = "公交";
        moduleModel4.res = R.mipmap.c1_icon4;
        moduleModel4.hasRedPoint = false;
        moduleModels.add(moduleModel4);

        ModuleModel moduleModel5 = new ModuleModel();
        moduleModel5.title = "社区论坛";
        moduleModel5.res = R.mipmap.c1_icon5;
        moduleModel5.hasRedPoint = false;
        moduleModels.add(moduleModel5);

        ModuleModel moduleModel6 = new ModuleModel();
        moduleModel6.title = "附近商家";
        moduleModel6.res = R.mipmap.c1_icon6;
        moduleModel6.hasRedPoint = false;
        moduleModels.add(moduleModel6);

        ModuleModel moduleModel7 = new ModuleModel();
        moduleModel7.title = "医院";
        moduleModel7.res = R.mipmap.c1_icon7;
        moduleModel7.hasRedPoint = false;
        moduleModels.add(moduleModel7);

        ModuleModel moduleModel8 = new ModuleModel();
        moduleModel8.title = "全部分类";
        moduleModel8.res = R.mipmap.c1_icon8;
        moduleModel8.hasRedPoint = false;
        moduleModels.add(moduleModel8);
        int count = moduleModels.size();

        int rowNum;
        if (count % 4 == 0)
            rowNum = count / 4;
        else
            rowNum = count / 4 + 1;
        for (int i = 0; i < rowNum; i++) {
            TableRow tableRow = new TableRow(tableLayout.getContext());
            for (int j = 0; j < 4; j++) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT);

                View rowView = LayoutInflater.from(tableRow.getContext()).inflate(R.layout.layout_item_index_top, null);
                ImageView iv_icon = (ImageView) rowView.findViewById(R.id.ivIcon);
                final TextView tv_title =(TextView) rowView.findViewById(R.id.tvCity);
                iv_icon.setImageResource(moduleModels.get(i * 4 + j).res);
                tv_title.setText(moduleModels.get(i * 4 + j).title);

                tv_title.setTextColor(getResources().getColor(R.color.txt_gray_color));
                TextView tvRedPoint = (TextView)rowView.findViewById(R.id.tv_red_point);
                if (moduleModels.get(i * 4 + j).hasRedPoint) {
                    tvRedPoint.setVisibility(View.VISIBLE);
                    //摇摆
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_anim);
                    shake.setDuration(3000);
                    shake.setRepeatCount(Integer.MAX_VALUE);
                    // tvRedPoint.startAnimation(shake);
                } else {
                    tvRedPoint.setVisibility(View.GONE);
                }
                //rowView.setPadding(0, 10, 0, 10);
                rowView.setLayoutParams(params);
                tableRow.addView(rowView, params);

                final int row = i;
                final int column = j;
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = tv_title.getText().toString();
                        if ("全部分类".equals(title)) {
//                            if (row * 4 + column == moduleModels.size() - 1) {//全部分类
                            Intent intent = new Intent(getActivity(), CategoryActivity.class);
                            startActivity(intent);
//                            }
                        } else if (getString(R.string.activity_payment).equals(title)) {
                            Intent intent = new Intent(getActivity(), PayIndexActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.MATCH_PARENT);
//            params.topMargin=20;
//            params.bottomMargin=20;
            tableLayout.addView(tableRow, params);
            tableLayout.setStretchAllColumns(true);
            tableRow.setBackgroundResource(R.color.white);
        }

    }

    private void animation(View view) {
        //摇摆
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_anim);
        shake.setDuration(3000);
        shake.setRepeatCount(Integer.MAX_VALUE);
        shake.start();
        view.startAnimation(shake);

    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            //跳转到下一个
            int index = adPlayBanner.getCurrentItem() + 1;
            if (index == mDatas.size())
                adPlayBanner.setCurrentItem(0);
            else
                adPlayBanner.setCurrentItem(index);
            return true;
        }
        if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
            //跳转到上一个
            int index = adPlayBanner.getCurrentItem() - 1;
            if (index == 0)
                adPlayBanner.setCurrentItem(mDatas.size() - 1);
            else
                adPlayBanner.setCurrentItem(index);
            return true;
        }
        return false;
    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {

        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
    }


    /**
     * 下拉刷新重新加载
     */
    @Override
    public void onRefresh() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            setRefreshing(false);
            return;
        }
        page = 1;
        loadData();
        getBannerData();
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
//        setRefreshing(false);
        page++;
        loadData();
    }


    private void loadData() {

        serverDao.getNewsList(new JsonCallback<BaseResponse<List<NewsModel>>>() {
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
                if (!isInitCache) {
                    //一般来说,缓存回调成功和网络回调成功做的事情是一样的,所以这里直接回调onSuccess
                   // onSuccess(baseResponse, call, null);
                    isInitCache = true;
                }
            }

            @Override
            public void onAfter(@Nullable BaseResponse<List<NewsModel>> baseResponse, @Nullable Exception e) {
                super.onAfter(baseResponse, e);
                //可能需要移除之前添加的布局
                mAdapter.removeAllFooterView();
                //结束刷新动画
                setRefreshing(false);
//                if (page == 3)
//                    mAdapter.setEnableLoadMore(false);
//                else
//                    mAdapter.setEnableLoadMore(true);
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
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //如果点击的是后退键  首先判断webView是否能够后退
//            //如果点击的是后退键  首先判断webView是否能够后退   返回值是boolean类型的
//            popupWindow.dismiss();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
