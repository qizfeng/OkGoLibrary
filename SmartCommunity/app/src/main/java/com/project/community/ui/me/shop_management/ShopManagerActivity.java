package com.project.community.ui.me.shop_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alanapi.switchbutton.SwitchButton;
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.project.community.Event.AddGoodsEvent;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.ShopIndexModel;
import com.project.community.ui.me.all_order.AllOrderActivity;
import com.project.community.util.NetworkUtils;
import com.project.community.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by cj on 17/10/24.
 * 商品管理
 */
public class ShopManagerActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.shop_manager_liushui)
    TextView shopManagerLiushui;
    @Bind(R.id.shop_manager_order)
    TextView shopManagerOrder;
    @Bind(R.id.shop_manager_goods_manager)
    TextView shopManagerGoodsManager;
    @Bind(R.id.shop_manager_data_manager)
    TextView shopManagerDataManager;
    @Bind(R.id.shop_manager_order_num)
    TextView shopManagerOrderNum;
    @Bind(R.id.shop_manager_order_manager)
    FrameLayout shopManagerOrderManager;
    @Bind(R.id.shop_manager_zhanghao_manager)
    TextView shopManagerZhanghaoManager;
    @Bind(R.id.tv_push)
    TextView tvPush;
    @Bind(R.id.switchButton)
    SwitchButton mSwitchButton;

    private boolean iS_Swich=false;


    private String shopId="";

    /**
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopManagerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.shop_manager_title), R.mipmap.iv_back);

        getShopData();
        iS_Swich=mSwitchButton.isChecked();
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                showToast(getString(R.string.toast_online));
                serverDao.updateIsOpen(getUser(ShopManagerActivity.this).id, new JsonCallback<BaseResponse<List<List>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<List>> baseResponse, Call call, Response response) {
                        showToast(baseResponse.message);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showToast(e.getMessage());
                        mSwitchButton.setChecked(iS_Swich);
                    }
                });
            }
        });
    }

    @OnClick({R.id.shop_manager_goods_manager, R.id.shop_manager_data_manager, R.id.shop_manager_order_manager, R.id.shop_manager_zhanghao_manager})
    public void onViewClicked(View view) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            return;
        }
        switch (view.getId()) {
            case R.id.shop_manager_goods_manager://商品管理
                AllProductsActivity.startActivity(this,shopId);
                break;
            case R.id.shop_manager_data_manager://商铺资料
                ShopDataActivity.startActivity(this);
                break;
            case R.id.shop_manager_order_manager://订单管理
                AllOrderActivity.startActivity(this,shopId);
                break;
            case R.id.shop_manager_zhanghao_manager://账号管理
                SubdomainsAccountActivity.startActivity(this,shopId);
                break;
        }
    }


    /**
     * 获取店铺信息
     */
    private void getShopData(){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        serverDao.shopIndex(getUser(this).id,new JsonCallback<BaseResponse<ShopIndexModel>>() {
            @Override
            public void onSuccess(BaseResponse<ShopIndexModel> listBaseResponse, Call call, Response response) {
                dismissDialog();
                if (listBaseResponse.retData.isChild==1) {
                    mTvTitle.setText(Html.fromHtml("<font>"+getString(R.string.shop_manager_title)+"</font><font><small>"+getString(R.string.shop_manager_title_son)+"</small></font>"));
                    shopManagerZhanghaoManager.setVisibility(View.INVISIBLE);
                }
                shopManagerLiushui.setText(listBaseResponse.retData.todayMoney+"/"+listBaseResponse.retData.moneyTotal);
                shopManagerOrder.setText(listBaseResponse.retData.todayOrder+"/"+listBaseResponse.retData.orderTotal);
                if (listBaseResponse.retData.handleOrder>0) {
                    shopManagerOrderNum.setVisibility(View.VISIBLE);
                    shopManagerOrderNum.setText(listBaseResponse.retData.handleOrder+"");
                }
                else shopManagerOrderNum.setVisibility(View.GONE);
                if (listBaseResponse.retData.isOpen==0) mSwitchButton.setChecked(true);
                else mSwitchButton.setChecked(false);
                shopId=listBaseResponse.retData.shopId;

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                showToast(e.getMessage());
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
        if (addGoodsEvent.getItem().equals("0") || addGoodsEvent.getItem().equals("999"))
            getShopData();
    }
}
