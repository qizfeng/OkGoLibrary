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
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.me.all_order.AllOrderActivity;
import com.project.community.util.ToastUtil;

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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopManagerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.shop_manager_title), R.mipmap.iv_back);
//        mTvTitle.setText(Html.fromHtml(getString(R.string.shop_manager_goods_son)));
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
        switch (view.getId()) {
            case R.id.shop_manager_goods_manager://商品管理
                AllProductsActivity.startActivity(this);
                break;
            case R.id.shop_manager_data_manager://商铺资料
                ShopDataActivity.startActivity(this);
                break;
            case R.id.shop_manager_order_manager://订单管理
                AllOrderActivity.startActivity(this);
                break;
            case R.id.shop_manager_zhanghao_manager://账号管理
                SubdomainsAccountActivity.startActivity(this);
                break;
        }
    }
}
