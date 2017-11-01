package com.project.community.ui.me.all_order;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.GlideImageLoader;
import com.library.okgo.utils.LogUtils;
import com.library.okgo.utils.ToastUtils;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.R;
import com.project.community.base.BaseFragment;
import com.project.community.constants.AppConstants;
import com.project.community.model.CommentModel;
import com.project.community.model.GoodsModel;
import com.project.community.model.MerchantDeailModel;
import com.project.community.model.OrderModel;
import com.project.community.model.ShoppingCartModel;
import com.project.community.ui.PhoneDialogActivity;
import com.project.community.ui.adapter.AllOrderApdater;
import com.project.community.ui.adapter.MyOrderApdater;
import com.project.community.ui.adapter.ShoppingCartAdapter;
import com.project.community.ui.life.minsheng.MerchantDetailActivity;
import com.project.community.util.NetworkUtils;
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
public class MyOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;

    private MyOrderApdater mAdapter;
    private List<OrderModel> mData = new ArrayList<>();

    private String status;//0:未发货:1：已发货:2：已完成,3：退货申请,4：退货中,5：已退货,

    public static MyOrderFragment newInstance(String status) {
        final MyOrderFragment f = new MyOrderFragment();
        final Bundle args = new Bundle();
        args.putString("status", status);
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
        EventBus.getDefault().register(this);
        status = getArguments().getString("status");
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MyOrderApdater(getActivity(), mData,status, new MyOrderApdater.OnGoodsClickListener() {
            @Override
            public void onGoodsItemClick(View view,int parentPosition, int childPosition) {

            }
        }, new MyOrderApdater.OnFooterClickListener() {
            @Override
            public void onFooterDeleteClick(View view, int position) {
                Intent mIntent;
                switch (status){
                    case ""://"", #全部
                        switch (mData.get(position).orderStatus){
                            case "0"://"0", #未发货
                                switch (view.getId()){
                                    case R.id.item_foot_2:
                                        mIntent = new Intent(getActivity(), PhoneDialogActivity.class);
                                        mIntent.putExtra("hasHeader", false);
                                        mIntent.putExtra("type", mData.get(position).shopsPhone);
                                        startActivity(mIntent);
                                        break;
                                    case R.id.item_foot_3:
                                        showAlertDialog( position, 1);
                                        break;
                                }
                                break;
                            case "1"://:1：已发货
                                switch (view.getId()){
                                    case R.id.item_foot_2:
                                        break;
                                    case R.id.item_foot_3:
                                        showAlertDialog( position, 2);
                                        break;
                                }
                                break;
                            case "2"://2：已完成    (包括 已评价 与 待评价 )
                                if (mData.get(position).isComment==0)
                                    switch (view.getId()){
                                        case R.id.item_foot_1:
                                        TakeDeliveryOfGoodsActivity.startActivity(getActivity(),mData.get(position));
                                            break;
                                        case R.id.item_foot_2:
                                            showAlertDialog( position, 0);
                                            break;
                                        case R.id.item_foot_3:
                                            ApplySaleActivity.startActivity(getActivity(),mData.get(position));
                                            break;
                                    }
                                else
                                    switch (view.getId()){
                                        case R.id.item_foot_3:
                                            ApplySaleActivity.startActivity(getActivity(),mData.get(position));
                                            break;
                                    }
                                break;
                            default:
                                switch (view.getId()){
                                    case R.id.item_foot_3:

                                        break;
                                }
                                break;
                        }
                        break;
                    case "0"://"0", #未发货
                        switch (view.getId()){
                            case R.id.item_foot_2:
                                mIntent = new Intent(getActivity(), PhoneDialogActivity.class);
                                mIntent.putExtra("hasHeader", false);
                                mIntent.putExtra("type", mData.get(position).shopsPhone);
                                startActivity(mIntent);
                                break;
                            case R.id.item_foot_3:
                                showAlertDialog( position, 1);
                                break;
                        }
                        break;
                    case "1"://:1：已发货
                        switch (view.getId()){
                            case R.id.item_foot_2:
                                break;
                            case R.id.item_foot_3:
                                showAlertDialog( position, 2);
                                break;
                        }
                        break;
                    case "2"://2：已完成
                        if (mData.get(position).isComment==0)
                            switch (view.getId()){
                                case R.id.item_foot_1:
                                    TakeDeliveryOfGoodsActivity.startActivity(getActivity(),mData.get(position));
                                    break;
                                case R.id.item_foot_2:
                                    showAlertDialog( position, 0);
                                    break;
                                case R.id.item_foot_3:
                                    ApplySaleActivity.startActivity(getActivity(),mData.get(position));
                                    break;
                            }
                        else
                            switch (view.getId()){
                                case R.id.item_foot_3:
//                                    ApplySaleActivity.startActivity(getActivity());
                                    break;
                            }
                        break;
                    default:
                        switch (view.getId()){
                            case R.id.item_foot_3:
//                                ApplySaleActivity.startActivity(getActivity());
                                break;
                        }
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
                GoodsOrderActivity.startActivity(getActivity(),mData.get(groupPosition),0);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getData(status);
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
     * D57获取订单列表
     */

    private void getData(String status) {

        setRefreshing(true);
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            setRefreshing(false);
            return;
        }

        serverDao.getOrder(
                getUser(getActivity()).id,
                String.valueOf(status),
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


    /**
     * D55取消订单
     */

    private void cacelOrder(String orderNo, final int position) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.cacelOrder(
                getUser(getActivity()).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
                        EventBus.getDefault().post(new AddGoodsEvent(status));
                        mAdapter.removeGroup(position);
                        mData.remove(position);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }


    /**
     * D55删除订单
     */

    private void deleteOrder(String orderNo, final int position) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.deleteOrder(
                getUser(getActivity()).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
                        EventBus.getDefault().post(new AddGoodsEvent(status));
                        mAdapter.removeGroup(position);
                        mData.remove(position);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }

    /**
     * D55确认收货
     */

    private void complete(String orderNo, final int position) {

        showLoading();
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.showShortToast(getActivity(), R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.complete(
                getUser(getActivity()).id,
                orderNo,
                new JsonCallback<BaseResponse<List>>() {
                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
                        EventBus.getDefault().post(new AddGoodsEvent(status));
                        mData.get(position).orderStatus="2";
                        mData.get(position).isComment=0;
                        mAdapter.changeRangeGroup(position,1);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }


    /**
     * 0删除,1其他取消订单 2 确认收货 3删除订单 提示窗口
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
        if (code==0) tv_content.setText(R.string.txt_confirm_detlet_order);
        else if (code==1) tv_content.setText(R.string.txt_confirm_cancel);
        else if (code==2) tv_content.setText(R.string.txt_confirm_shouhuo);
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
                if (code==0){
                    deleteOrder(mData.get(position).orderNo,position);
                }else if (code==1){
                    cacelOrder(mData.get(position).orderNo,position);
                }else if (code==2){
                    complete(mData.get(position).orderNo,position);
                }


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAddGoodsEvent(AddGoodsEvent addGoodsEvent) {
        if (!addGoodsEvent.getItem().equals(status))
            onRefresh();
    }
}
