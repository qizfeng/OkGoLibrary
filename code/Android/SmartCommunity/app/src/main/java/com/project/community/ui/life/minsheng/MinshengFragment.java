package com.project.community.ui.life.minsheng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.Event.CartRefreshEvent;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecyclerItemTouchHelperCallBack;
import com.project.community.model.ModuleModel;
import com.project.community.model.ShopModel;
import com.project.community.model.ShopResponse;
import com.project.community.ui.adapter.MinshengAdapter;
import com.project.community.ui.adapter.ModuleAdapter;
import com.project.community.ui.adapter.listener.MinshengAdapterItemListener;
import com.project.community.ui.life.zhengwu.TypeNewsActivity;
import com.project.community.util.NavLinearLayoutManager;
import com.project.community.util.ScreenUtils;
import com.project.community.view.HorizaontalGridView;
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
 * Created by zipingfang on 17/10/23.
 */

public class MinshengFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @Bind(R.id.iv_shop_cart)
    ImageView mIvShopCart;
    @Bind(R.id.tv_shop_cart)
    TextView mTvShopCart;
    private int page = 1;//当前页码
    private MinshengAdapter mAdapter;
    private View header;
    private HorizaontalGridView gridView;
    private List<ModuleModel> moduleModels = new ArrayList<>();
    private String locData="";

    public String getLocData() {
        return locData;
    }

    public void setLocData(String locData) {
        this.locData = locData;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_minsheng, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initData();
        return view;
    }

    @Override
    public void initData() {
        mIvShopCart.setOnClickListener(this);
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_minsheng, null);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApplyStoreActivity.class);
                startActivity(intent);
            }
        });
        gridView = header.findViewById(R.id.gridview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new NavLinearLayoutManager(getActivity()));


        mAdapter = new MinshengAdapter(null, new MinshengAdapterItemListener() {
            @Override
            public void onItemClick(View view, int position) {//整个item点击事件
//                if (mAdapter.getItem(position- 1).isOpen==1){
//                    showToast(getString(R.string.txt_shop_status));
//                    return;
//                }
                Bundle bundle = new Bundle();
                position = position - 1;//去掉头部
                bundle.putString("merchant_id", mAdapter.getItem(position).id);
                bundle.putString("merchant_distance", mAdapter.getItem(position).distance);
                MerchantDetailActivity.startActivity(getActivity(), bundle);
            }
        });

        SpacesItemDecoration decoration = new SpacesItemDecoration(1, true);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);

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


    private void loadData() {
        String latitute = getLocation(getActivity())[0];
        String longitute = getLocation(getActivity())[1];
//        String locData = "";
        if (!TextUtils.isEmpty(latitute) && !TextUtils.isEmpty(longitute)) {
            locData = longitute + "," + latitute;
        }
        String userId ="";
        if(isLogin(getActivity()))
            userId= getUser(getActivity()).id;
        serverDao.getMinshengIndexData(userId,locData, page, AppConstants.PAGE_SIZE, new JsonCallback<BaseResponse<ShopResponse>>() {
            @Override
            public void onSuccess(BaseResponse<ShopResponse> baseResponse, Call call, Response response) {
                if (baseResponse.retData.cartTotal>0){
                    mTvShopCart.setText(baseResponse.retData.cartTotal+"");
                    mTvShopCart.setVisibility(View.VISIBLE);
                } else
                    mTvShopCart.setVisibility(View.GONE);
                if (page == 1) {
                    List<ShopModel> data = new ArrayList<>();
                    data.addAll(baseResponse.retData.shopList);
                    mAdapter.setNewData(data);
                    mAdapter.setEnableLoadMore(true);
                } else {
                    //显示没有更多数据
                    if (baseResponse.retData.shopList.size() < AppConstants.PAGE_SIZE) {
                        List<ShopModel> data = new ArrayList<>();
                        data.addAll(baseResponse.retData.shopList);
                        mAdapter.addData(data);
                        mAdapter.loadMoreEnd();         //加载完成
                    } else {
                        List<ShopModel> data = new ArrayList<>();
                        data.addAll(baseResponse.retData.shopList);
                        mAdapter.addData(data);
                        mAdapter.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onAfter(@Nullable BaseResponse<ShopResponse> baseResponse, @Nullable Exception e) {
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
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shop_cart:
                ShoppingCartActivity.startActivity(getActivity(), null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0://社區商圈
                intent = new Intent(getActivity(), ShopsListActivity.class);
                startActivity(intent);
                break;
            case 1://家政
                showToast(getString(R.string.toast_online));
               /* intent = new Intent(getActivity(), OrderDetailActivity.class);
                startActivity(intent);*/
                break;
            case 2://醫院
                intent = new Intent(getActivity(), RegistrationAppActivity.class);
                startActivity(intent);
                break;
            case 3://公交
                intent = new Intent(getActivity(), PublicTransportationActivity.class);
                startActivity(intent);
                break;
            case 4://社區论坛
//                intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                intent = new Intent(getActivity(), BBSActivity.class);
                startActivity(intent);
                break;
            case 5://就业
                intent = new Intent(getActivity(), TypeNewsActivity.class);
                startActivity(intent);
                break;
            default://更多
                intent = new Intent(getActivity(), MerchantModuleMoreActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 设置数据
     */
    private void setGridData() {
        moduleModels = new ArrayList<>();
        ModuleModel moduleModel1 = new ModuleModel();
        moduleModel1.title = "社区商圈";
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
        moduleModels.add(moduleModel7);


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

    private void onDelete(List data) {
        ItemTouchHelper helper = new ItemTouchHelper(new RecyclerItemTouchHelperCallBack(mRecyclerView, data));
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAddGoodsEvent(CartRefreshEvent cartRefreshEvent) {
        onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
