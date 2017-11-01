package com.project.community.ui.me.all_order;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.model.CommentModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.MerchantDeailModel;
import com.project.community.model.OrderModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.adapter.AllOrderApdater;
import com.project.community.ui.adapter.MyOrderApdater;
import com.project.community.ui.adapter.ShoppingCartAdapter;
import com.project.community.ui.life.minsheng.MerchantDetailActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    private MyOrderApdater mAdapter;
    private List<OrderModel> mData = new ArrayList<>();

    private int code;//0:全部,1:待发货2:已发货3:待评价4:售后

    public static MyOrderFragment newInstance(int id) {
        final MyOrderFragment f = new MyOrderFragment();
        final Bundle args = new Bundle();
        args.putInt("ncid", id);
        f.setArguments(args);
        return f;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        code = getArguments().getInt("ncid");
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        onRefresh();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MyOrderApdater(getActivity(), mData, new MyOrderApdater.OnGoodsClickListener() {
            @Override
            public void onGoodsItemClick(View view,int parentPosition, int childPosition) {

            }
        }, new MyOrderApdater.OnFooterClickListener() {
            @Override
            public void onFooterDeleteClick(View view, int position) {
                LogUtils.e("delete:"+position);
                switch (view.getId()){
                    case R.id.item_foot_1:
                        TakeDeliveryOfGoodsActivity.startActivity(getActivity());
                        break;
                    case R.id.item_foot_2:
                        break;
                    case R.id.item_foot_3:
                        ApplySaleActivity.startActivity(getActivity());
                        break;
                }
            }

            @Override
            public void onSettlementClick(View view, int position) {
                LogUtils.e("settlement:"+position);
            }
        });
        mAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                GoodsOrderActivity.startActivity(getActivity(),code,mData.get(groupPosition));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        getData(code);
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

    public ArrayList<ShoppingCartModel> getListData() {
        ArrayList<ShoppingCartModel> myParents = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ShoppingCartModel myParent = getShoppingCart();
            myParents.add(myParent);
        }
        return myParents;
    }

    public ShoppingCartModel getShoppingCart() {
        ShoppingCartModel myParent = new ShoppingCartModel();
        ArrayList<GoodsModel> myChildren = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            GoodsModel myChild = new GoodsModel();
            myChildren.add(myChild);
        }
        myParent.goods = myChildren;
        return myParent;
    }

    /**
     * D57获取订单列表
     */

    private void getData(int status) {
        setRefreshing(true);
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            setRefreshing(true);
            return;
        }
        serverDao.getOrder(
                getUser(getActivity()).id,
                status,
                new JsonCallback<BaseResponse<List<OrderModel>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<OrderModel>> listBaseResponse, Call call, Response response) {
                        setRefreshing(false);
                        mData.clear();
                        mData.addAll(listBaseResponse.retData);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        setRefreshing(false);
                        showToast(e.getMessage());
                    }
                });
    }

}
