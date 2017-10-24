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
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.ui.me.all_order.AllOrderActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopManagerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manager);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.shop_manager_goods), R.mipmap.iv_back);
//        mTvTitle.setText(Html.fromHtml(getString(R.string.shop_manager_goods_son)));
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                showToast(getString(R.string.toast_online));
            }
        });
    }

    @OnClick({R.id.shop_manager_goods_manager, R.id.shop_manager_data_manager, R.id.shop_manager_order_manager, R.id.shop_manager_zhanghao_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shop_manager_goods_manager://商品管理
                AllProductsActivity.startActivity(this);
                break;
            case R.id.shop_manager_data_manager:
                ShopDataActivity.startActivity(this);
                break;
            case R.id.shop_manager_order_manager:
                AllOrderActivity.startActivity(this);
                break;
            case R.id.shop_manager_zhanghao_manager:
                SubdomainsAccountActivity.startActivity(this);
                break;
        }
    }
}
