package com.project.community.ui.life.minsheng;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.listener.RecyclerItemTouchHelperCallBack;
import com.project.community.model.ModuleModel;
import com.project.community.model.NewsModel;
import com.project.community.ui.WebViewActivity;
import com.project.community.ui.adapter.MinshengAdapter;
import com.project.community.ui.adapter.ModuleAdapter;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;
import com.project.community.ui.life.TopicDetailActivity;
import com.project.community.util.ScreenUtils;
import com.project.community.view.HorizaontalGridView;
import com.project.community.view.SpacesItemDecoration;
import com.project.community.view.VpSwipeRefreshLayout;
import com.ryane.banner_lib.AdPageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qizfeng on 17/7/17.
 * 民生首页
 */

public class MinshengActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    private int page = 1;//当前页码
    private MinshengAdapter mAdapter;
    private View header;
    private HorizaontalGridView gridView;
    private String type = "health";
    private List<ModuleModel> moduleModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minsheng);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_header_minsheng, null);
        gridView = (HorizaontalGridView) header.findViewById(R.id.gridview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new MinshengAdapter(null, new MinshengAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
                String url = mAdapter.getData().get(position).url;
                Intent intent = new Intent(MinshengActivity.this, TopicDetailActivity.class);
                if (!TextUtils.isEmpty(url)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    intent.putExtra("bundle", bundle);
                }
                startActivity(intent);
            }

        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        // mAdapter.setOnLoadMoreListener(this, recyclerView);


        //中部模块
        setGridData();
        setGridView();

        //头部测试数据
        mAdapter.addHeaderView(header);
        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
        gridView.setOnItemClickListener(this);

    }


    private void loadData(String type) {
        serverDao.getNewsList(type, new JsonCallback<BaseResponse<List<NewsModel>>>() {
            @Override
            public void onSuccess(BaseResponse<List<NewsModel>> baseResponse, Call call, Response response) {
                    if (page == 1) {
                        List<NewsModel> data = new ArrayList<>();
                        data.addAll(baseResponse.newslist);
                        mAdapter.setNewData(data);
                        mAdapter.setEnableLoadMore(true);
                    } else {
                        //显示没有更多数据
                        if (page == 3) {
                            mAdapter.loadMoreEnd();         //加载完成
                        } else {
                            List<NewsModel> data = new ArrayList<>();
                            data.addAll(baseResponse.newslist);
                            mAdapter.addData(data);
                            mAdapter.loadMoreComplete();
                        }
                    }
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
    public void onRefresh() {
        page = 1;
        loadData(type);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    }


    /**
     * 设置数据
     */
    private void setGridData() {

        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title = "附近商家";
        moduleModel1.res = R.mipmap.d27_icon1;
        moduleModel1.hasRedPoint = true;
        moduleModels.add(moduleModel1);

        ModuleModel moduleModel2 = new ModuleModel();
        moduleModel2.title = "家政";
        moduleModel2.res = R.mipmap.d27_icon2;
        moduleModel2.hasRedPoint = false;
        moduleModels.add(moduleModel2);

        ModuleModel moduleModel3 = new ModuleModel();
        moduleModel3.title = "医院";
        moduleModel3.res = R.mipmap.d27_icon3;
        moduleModel3.hasRedPoint = false;
        moduleModels.add(moduleModel3);

        ModuleModel moduleModel4 = new ModuleModel();
        moduleModel4.title = "公交";
        moduleModel4.res = R.mipmap.d27_icon4;
        moduleModel4.hasRedPoint = false;
        moduleModels.add(moduleModel4);

        ModuleModel moduleModel5 = new ModuleModel();
        moduleModel5.title = "社区论坛";
        moduleModel5.res = R.mipmap.d27_icon5;
        moduleModel5.hasRedPoint = false;
        moduleModels.add(moduleModel5);

        ModuleModel moduleModel6 = new ModuleModel();
        moduleModel6.title = "就业";
        moduleModel6.res = R.mipmap.d2_icon4;
        moduleModel6.hasRedPoint = false;
        moduleModels.add(moduleModel6);

        ModuleModel moduleModel7 = new ModuleModel();
        moduleModel7.title = "更多";
        moduleModel7.res = R.mipmap.d27_icon6;
        moduleModel7.hasRedPoint = false;
        moduleModels.add(moduleModel6);


    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        int length = ScreenUtils.getScreenWidth(this)/4;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int itemWidth = (int) (length * density);
        itemWidth=length;
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

    private void onDelete(List data){
        ItemTouchHelper helper = new ItemTouchHelper(new RecyclerItemTouchHelperCallBack(mRecyclerView,data));
        helper.attachToRecyclerView(mRecyclerView);
    }



}