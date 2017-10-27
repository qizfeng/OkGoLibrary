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
import com.library.okgo.callback.JsonCallback;
import com.library.okgo.model.BaseResponse;
import com.library.okgo.utils.ToastUtils;
import com.library.okgo.utils.ValidateUtil;
import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.model.CommentModel;
import com.project.community.model.SubdomainAccountModel;
import com.project.community.model.UserModel;
import com.project.community.util.NetworkUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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

    private String shopId;//店铺Id
    private String childId="";//子账号Id
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddSubdomainsAccountActivity.class);
        ((Activity)context).startActivityForResult(intent,100);
    }
    public static void startActivity(Context context, String childId ,String shopId) {
        Intent intent = new Intent(context, AddSubdomainsAccountActivity.class);
        intent.putExtra("cj",childId);
        intent.putExtra("shopId",shopId);
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
        if (getIntent().getExtras()!=null && !TextUtils.isEmpty(getIntent().getStringExtra("cj"))){
            childId=getIntent().getStringExtra("cj");
            mTvTitle.setText(getResources().getString(R.string.add_subdomains_account_title_edit));
            getData(childId);
        }
        shopId=getIntent().getStringExtra("shopId");
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
                AddData(childId,shopId);

                break;
        }
    }

    /**
     * 增加子账号
     */
    private void AddData(String id,String shopId){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }

        int addSubdomainsAccountSwPriceNum=0;
        int addSubdomainsAccountSwOrderNum=0;
        int addSubdomainsAccountCbAddGoodsNum=0;
        int addSubdomainsAccountCbGoodsManagerNum=0;
        int addSubdomainsAccountCbOrderManagerNum=0;
        int addSubdomainsAccountCbShopDataNum=0;
        if (addSubdomainsAccountSwPrice.isChecked()) addSubdomainsAccountSwPriceNum=0;
        else addSubdomainsAccountSwPriceNum=1;
        if (addSubdomainsAccountSwOrder.isChecked()) addSubdomainsAccountSwOrderNum=0;
        else addSubdomainsAccountSwOrderNum=1;
        if (addSubdomainsAccountCbAddGoods.isChecked()) addSubdomainsAccountCbAddGoodsNum=0;
        else addSubdomainsAccountCbAddGoodsNum=1;
        if (addSubdomainsAccountCbGoodsManager.isChecked()) addSubdomainsAccountCbGoodsManagerNum=0;
        else addSubdomainsAccountCbGoodsManagerNum=1;
        if (addSubdomainsAccountCbOrderManager.isChecked()) addSubdomainsAccountCbOrderManagerNum=0;
        else addSubdomainsAccountCbOrderManagerNum=1;
        if (addSubdomainsAccountCbShopData.isChecked()) addSubdomainsAccountCbShopDataNum=0;
        else addSubdomainsAccountCbShopDataNum=1;


        serverDao.addSubdomainsAccount(
                getUser(this).id,
                id,
                shopId,
                addSubdomainsAccountEtName.getText().toString(),
                addSubdomainsAccountEtPhone.getText().toString(),
                addSubdomainsAccountSwPriceNum,
                addSubdomainsAccountCbAddGoodsNum,
                addSubdomainsAccountCbGoodsManagerNum,
                addSubdomainsAccountCbOrderManagerNum,
                addSubdomainsAccountCbShopDataNum,
                addSubdomainsAccountSwOrderNum,
                new JsonCallback<BaseResponse<List>>() {

                    @Override
                    public void onSuccess(BaseResponse<List> listBaseResponse, Call call, Response response) {
                        dismissDialog();
                        showToast(listBaseResponse.message);
                        Intent intent = new Intent();
                        setResult(100,intent);
                        finish();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dismissDialog();
                        showToast(e.getMessage());
                    }
                });
    }

    private void getData(String childId){
        showLoading();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShortToast(this, R.string.network_error);
            dismissDialog();
            return;
        }
        serverDao.getSubdomainsAccountDetail(childId, new JsonCallback<BaseResponse<SubdomainAccountModel>>() {
            @Override
            public void onSuccess(BaseResponse<SubdomainAccountModel> subdomainAccountModelBaseResponse, Call call, Response response) {
                dismissDialog();
                addSubdomainsAccountEtName.setText(subdomainAccountModelBaseResponse.retData.name);
                if (!TextUtils.isEmpty(addSubdomainsAccountEtName.getText().toString())){
                    addSubdomainsAccountEtName.setSelection(addSubdomainsAccountEtName.getText().length());
                }
                addSubdomainsAccountEtPhone.setText(subdomainAccountModelBaseResponse.retData.phone);
                if (subdomainAccountModelBaseResponse.retData.sumMoney.equals("0")) addSubdomainsAccountSwPrice.setChecked(true);
                else addSubdomainsAccountSwPrice.setChecked(false);
                if (subdomainAccountModelBaseResponse.retData.sumOrder.equals("0")) addSubdomainsAccountSwOrder.setChecked(true);
                else addSubdomainsAccountSwOrder.setChecked(false);
                if (subdomainAccountModelBaseResponse.retData.addGoods.equals("0")) addSubdomainsAccountCbAddGoods.setChecked(true);
                else addSubdomainsAccountCbAddGoods.setChecked(false);
                if (subdomainAccountModelBaseResponse.retData.goodsMge.equals("0")) addSubdomainsAccountCbGoodsManager.setChecked(true);
                else addSubdomainsAccountCbGoodsManager.setChecked(false);
                if (subdomainAccountModelBaseResponse.retData.orderMge.equals("0")) addSubdomainsAccountCbOrderManager.setChecked(true);
                else addSubdomainsAccountCbOrderManager.setChecked(false);
                if (subdomainAccountModelBaseResponse.retData.shopInfo.equals("0")) addSubdomainsAccountCbShopData.setChecked(true);
                else addSubdomainsAccountCbShopData.setChecked(false);

            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                dismissDialog();
                showToast(e.getMessage());
            }
        });
    }

}
