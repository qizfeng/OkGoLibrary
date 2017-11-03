package com.project.community.ui.me.shop_management;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.Event.AddHouseEvent;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;
import com.project.community.model.GoodsManagerModel;
import com.project.community.ui.adapter.ProductSellApdater;
import com.project.community.ui.adapter.ServiesComApdater;
import com.project.community.ui.me.OrderDetailActivity;
import com.project.community.ui.me.all_order.AllOrderFragment;
import com.project.community.util.NetworkUtils;
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
public class SellFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    private ProductSellApdater mAdapter;
    private List<GoodsManagerModel> list =new ArrayList<>();;
    private int code=0;
    private String shopId;


    public static SellFragment newInstance(int code,String shopId) {
        final SellFragment f = new SellFragment();
        final Bundle args = new Bundle();
        args.putInt("cj", code);
        args.putString("shopId", shopId);
        f.setArguments(args);
        return f;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        code=getArguments().getInt("cj");
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, false);
        recyclerView.addItemDecoration(decoration);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mAdapter = new ProductSellApdater(list, new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ShopDataActivity.startActivity(getActivity());
            }

            @Override
            public void onCustomClick(View view, int position) {
                switch (view.getId()){
                    case R.id.item_product_change:
                        list.get(position).open=!list.get(position).open;
                        mAdapter.notifyItemChanged(position,list.get(position));
                        break;
                    case R.id.item_product_del:
                        showAlertDialog(position, 0);
                        break;
                    case R.id.item_product_deit:
                        BuildNewGoodsActivity.startActivity(getActivity(),list.get(position),shopId);
                        break;
                    case R.id.item_product_xia:
                        upDownGoods(list.get(position).goodId,position);
                        break;
                }

            }
        },code);
        recyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        shopId=getArguments().getString("shopId");
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


    /**
     * 获取列表
     */
    private void getData(int id){

        setRefreshing(true);
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            setRefreshing(false);
            return;
        }
        serverDao.getGoodsManagerList(
                getUser(getActivity()).id,
                id+"",
                new JsonCallback<BaseResponse<List<GoodsManagerModel>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<GoodsManagerModel>> listBaseResponse, Call call, Response response) {
                        setRefreshing(false);
                        list.clear();
                        list.addAll(listBaseResponse.retData);
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

    /**
     * 删除商品
     */
    private void delData(String goodId, final int position){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.delGoods(getUser(getActivity()).id, goodId,new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
                showToast(e.getMessage());
            }
        });
    }


    /**
     * D78上下架
     */
    private void upDownGoods(String goodId, final int position){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.upDownGoods(getUser(getActivity()).id, goodId,new JsonCallback<BaseResponse<List>>() {
            @Override
            public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                dismissDialog();
                showToast(listBaseResponse.message);
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
                EventBus.getDefault().post(new AddGoodsEvent(code+""));
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
                EventBus.getDefault().post(new AddGoodsEvent(code+""));
                showToast(e.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAddGoodsEvent(AddGoodsEvent addGoodsEvent) {
        if (!addGoodsEvent.getItem().equals(code+""))
            onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 发货pop 0 ,发货,  1处理 ,2 拒绝处理
     *
     * @param position
     */
    private Dialog mDialog;
    public void showAlertDialog(final int position, final int code) {
//        mDialog = new AlertDialog.Builder(this).create();
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
        if (code==0) tv_content.setText(R.string.txt_confirm_delete);

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
                mDialog.dismiss();
                if (code==0) delData(list.get(position).goodId,position);
            }
        });
    }

}
