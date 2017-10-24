package com.project.community.ui.me.shop_management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alanapi.switchbutton.SwitchButton;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.CommentModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 17/10/24.
 * 添加子账号
 */

public class AddSubdomainsAccountActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.add_subdomains_account_et_name)
    EditText addSubdomainsAccountEtName;
    @Bind(R.id.add_subdomains_account_et_phone)
    EditText addSubdomainsAccountEtPhone;
    @Bind(R.id.add_subdomains_account_sw_price)
    SwitchButton addSubdomainsAccountSwPrice;
    @Bind(R.id.add_subdomains_account_sw_order)
    SwitchButton addSubdomainsAccountSwOrder;
    @Bind(R.id.add_subdomains_account_cb_add_goods)
    CheckBox addSubdomainsAccountCbAddGoods;
    @Bind(R.id.add_subdomains_account_cb_goods_manager)
    CheckBox addSubdomainsAccountCbGoodsManager;
    @Bind(R.id.add_subdomains_account_cb_order_manager)
    CheckBox addSubdomainsAccountCbOrderManager;
    @Bind(R.id.add_subdomains_account_cb_shop_data)
    CheckBox addSubdomainsAccountCbShopData;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddSubdomainsAccountActivity.class);
        ((Activity)context).startActivityForResult(intent,100);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subdomains_account);
        ButterKnife.bind(this);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.add_subdomains_account_title), R.mipmap.iv_back);
        initData();
    }

    private void initData() {
        addSubdomainsAccountSwPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            }
        });
        addSubdomainsAccountSwOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            }
        });
    }

    @OnClick({R.id.add_subdomains_account_rl_add_goods, R.id.add_subdomains_account_rl_goods_manager, R.id.add_subdomains_account_rl_order_manager, R.id.add_subdomains_account_rl_shop_data, R.id.btn_sava})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_subdomains_account_rl_add_goods:
                if (addSubdomainsAccountCbAddGoods.isChecked()) addSubdomainsAccountCbAddGoods.setChecked(false);
                else addSubdomainsAccountCbAddGoods.setChecked(true);
                break;
            case R.id.add_subdomains_account_rl_goods_manager:
                if (addSubdomainsAccountCbGoodsManager.isChecked()) addSubdomainsAccountCbGoodsManager.setChecked(false);
                else addSubdomainsAccountCbGoodsManager.setChecked(true);
                break;
            case R.id.add_subdomains_account_rl_order_manager:
                if (addSubdomainsAccountCbOrderManager.isChecked()) addSubdomainsAccountCbOrderManager.setChecked(false);
                else addSubdomainsAccountCbOrderManager.setChecked(true);
                break;
            case R.id.add_subdomains_account_rl_shop_data:
                if (addSubdomainsAccountCbShopData.isChecked()) addSubdomainsAccountCbShopData.setChecked(false);
                else addSubdomainsAccountCbShopData.setChecked(true);
                break;
            case R.id.btn_sava:
                if (TextUtils.isEmpty(addSubdomainsAccountEtName.getText().toString())){
                    ToastUtils.showLongToast(this,getString(R.string.add_subdomains_account_name_hit));
                    return;
                }
                if (TextUtils.isEmpty(addSubdomainsAccountEtPhone.getText().toString())){
                    ToastUtils.showLongToast(this,getString(R.string.add_subdomains_account_phone_hit));
                    return;
                }
                if (!ValidateUtil.isPhone(addSubdomainsAccountEtPhone.getText().toString())) {
                    showToast(getString(R.string.toast_error_phone));
                    return;
                }
                CommentModel commentModel =new CommentModel();
                commentModel.content=addSubdomainsAccountEtName.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("save",commentModel);
                setResult(100,intent);
                finish();
                break;
        }
    }
}
